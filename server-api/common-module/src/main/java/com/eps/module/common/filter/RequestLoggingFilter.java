package com.eps.module.common.filter;

import com.eps.module.common.util.SensitiveDataMasker;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Production-grade request logging filter that logs all HTTP requests with:
 * - Unique requestId for tracing
 * - HTTP method and URI
 * - Masked query parameters (hides sensitive data)
 * - HTTP status code
 * - Request duration in milliseconds
 * - Request/Response body (conditionally based on profile and status)
 * 
 * Body Logging Strategy:
 * - DEV: Always log request/response body (for debugging)
 * - PROD: Log body ONLY for failures (status >= 400) - masked and truncated
 * 
 * Uses MDC (Mapped Diagnostic Context) for thread-safe logging context.
 * Runs before Spring Security to capture all requests including auth failures.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 1) // Run after CORS, before Security
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final SensitiveDataMasker sensitiveDataMasker;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${logging.request.body.enabled:true}")
    private boolean logRequestBody;

    @Value("${logging.request.body.max-length:5000}")
    private int maxBodyLength;

    // In production, only log body for failures
    @Value("${logging.request.body.only-on-error:false}")
    private boolean logBodyOnlyOnError;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Generate unique request ID for tracing
        String requestId = UUID.randomUUID().toString();
        
        // Set MDC context for this request (available in all logs during this request)
        MDC.put("requestId", requestId);
        MDC.put("httpMethod", request.getMethod());
        MDC.put("requestURI", request.getRequestURI());
        
        // Get client IP (useful for security audits)
        String clientIp = getClientIpAddress(request);
        if (clientIp != null && !clientIp.isEmpty()) {
            MDC.put("clientIP", clientIp);
        }

        // Wrap request and response to cache body for logging
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        // Start timer for duration calculation
        long startTime = System.currentTimeMillis();

        try {
            // Log incoming request (without body initially)
            logRequest(wrappedRequest, requestId, false);

            // Continue with the filter chain
            filterChain.doFilter(wrappedRequest, wrappedResponse);

        } finally {
            // Calculate request duration
            long duration = System.currentTimeMillis() - startTime;

            // Log response with body if needed
            logResponse(wrappedRequest, wrappedResponse, requestId, duration);

            // Copy cached response body back to actual response
            wrappedResponse.copyBodyToResponse();

            // Clear MDC to prevent memory leaks
            MDC.clear();
        }
    }

    /**
     * Logs the incoming HTTP request with masked sensitive data.
     */
    private void logRequest(ContentCachingRequestWrapper request, String requestId, boolean includeBody) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        // Mask sensitive query parameters
        String maskedQuery = "";
        if (queryString != null && !queryString.isEmpty()) {
            maskedQuery = "?" + sensitiveDataMasker.maskQueryParams(queryString);
        }

        log.info("[REQUEST] {} | {} | {}{}", requestId, method, uri, maskedQuery);

        // Log request body if enabled and it's a JSON/text request
        if (includeBody && logRequestBody && shouldLogBody(request)) {
            String requestBody = getRequestBody(request);
            if (requestBody != null && !requestBody.isEmpty()) {
                String maskedBody = sensitiveDataMasker.maskRequestBody(requestBody);
                String truncatedBody = truncateIfNeeded(maskedBody);
                log.info("[REQUEST BODY] {} | {}", requestId, truncatedBody);
            }
        }
    }

    /**
     * Logs the HTTP response with status code and duration.
     * In production, logs request/response body ONLY for failures (status >= 400).
     * In development, always logs body.
     */
    private void logResponse(ContentCachingRequestWrapper request,
                            ContentCachingResponseWrapper response,
                            String requestId,
                            long duration) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        log.info("[RESPONSE] {} | {} | {} | {} | {}ms", requestId, method, uri, status, duration);

        // Determine if we should log body
        boolean isError = status >= 400;
        boolean shouldLogBodyNow = logRequestBody && (!logBodyOnlyOnError || isError);

        // Log request body for errors (if we didn't log it earlier)
        if (shouldLogBodyNow && shouldLogBody(request)) {
            String requestBody = getRequestBody(request);
            if (requestBody != null && !requestBody.isEmpty()) {
                String maskedBody = sensitiveDataMasker.maskRequestBody(requestBody);
                String truncatedBody = truncateIfNeeded(maskedBody);
                
                if (isError) {
                    log.error("[REQUEST BODY] {} | {}", requestId, truncatedBody);
                } else {
                    log.info("[REQUEST BODY] {} | {}", requestId, truncatedBody);
                }
            }
        }

        // Log response body for errors or in dev mode
        if (shouldLogBodyNow && shouldLogBody(response)) {
            String responseBody = getResponseBody(response);
            if (responseBody != null && !responseBody.isEmpty()) {
                String maskedBody = sensitiveDataMasker.maskRequestBody(responseBody);
                String truncatedBody = truncateIfNeeded(maskedBody);
                
                if (isError) {
                    log.error("[RESPONSE BODY] {} | {}", requestId, truncatedBody);
                } else {
                    log.info("[RESPONSE BODY] {} | {}", requestId, truncatedBody);
                }
            }
        }

        // Log warning for slow requests (> 1 second)
        if (duration > 1000) {
            log.warn("[SLOW REQUEST] {} | {} | {} took {}ms", requestId, method, uri, duration);
        }

        // Log error status codes
        if (status >= 500) {
            log.error("[SERVER ERROR] {} | {} | {} returned {}", requestId, method, uri, status);
        } else if (status == 401 || status == 403) {
            log.warn("[AUTH FAILURE] {} | {} | {} returned {}", requestId, method, uri, status);
        } else if (status >= 400) {
            log.warn("[CLIENT ERROR] {} | {} | {} returned {}", requestId, method, uri, status);
        }
    }

    /**
     * Extract request body from cached wrapper.
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * Extract response body from cached wrapper.
     */
    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * Check if we should log the body based on content type.
     */
    private boolean shouldLogBody(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        
        // Only log JSON, XML, text content types
        return contentType.contains(MediaType.APPLICATION_JSON_VALUE) ||
               contentType.contains(MediaType.APPLICATION_XML_VALUE) ||
               contentType.contains(MediaType.TEXT_PLAIN_VALUE) ||
               contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

    /**
     * Check if we should log the response body based on content type.
     */
    private boolean shouldLogBody(ContentCachingResponseWrapper response) {
        String contentType = response.getContentType();
        if (contentType == null) {
            return false;
        }
        
        // Only log JSON, XML, text content types
        return contentType.contains(MediaType.APPLICATION_JSON_VALUE) ||
               contentType.contains(MediaType.APPLICATION_XML_VALUE) ||
               contentType.contains(MediaType.TEXT_PLAIN_VALUE);
    }

    /**
     * Truncate body if it exceeds max length to prevent log bloat.
     */
    private String truncateIfNeeded(String body) {
        if (body == null) {
            return "";
        }
        
        if (body.length() > maxBodyLength) {
            return body.substring(0, maxBodyLength) + "... [TRUNCATED - " + body.length() + " total chars]";
        }
        
        return body;
    }

    /**
     * Extracts the client IP address from the request.
     * Checks common proxy headers first, falls back to remote address.
     */
    private String getClientIpAddress(HttpServletRequest request) {
        // Check common proxy headers
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For can contain multiple IPs, take the first one
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        // Fallback to remote address
        return request.getRemoteAddr();
    }

    /**
     * Skip logging for static resources and health checks to reduce log noise.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Skip static resources
        if (path.startsWith("/static/") || 
            path.startsWith("/public/") ||
            path.endsWith(".js") || 
            path.endsWith(".css") || 
            path.endsWith(".ico") ||
            path.endsWith(".png") ||
            path.endsWith(".jpg") ||
            path.endsWith(".jpeg") ||
            path.endsWith(".gif") ||
            path.endsWith(".svg")) {
            return true;
        }

        // Skip actuator health checks (too noisy)
        if (path.contains("/actuator/health") || path.contains("/actuator/prometheus")) {
            return true;
        }

        // Skip bulk upload endpoints (SSE streaming) to prevent response buffering
        // ContentCachingResponseWrapper buffers the response which breaks SSE
        if (path.contains("/bulk-upload")) {
            return true;
        }

        return false;
    }
}
