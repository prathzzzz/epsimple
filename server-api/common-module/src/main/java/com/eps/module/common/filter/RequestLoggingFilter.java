package com.eps.module.common.filter;

import com.eps.module.common.util.SensitiveDataMasker;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Production-grade request logging filter that logs all HTTP requests with:
 * - Unique requestId for tracing
 * - HTTP method and URI
 * - Masked query parameters (hides sensitive data)
 * - HTTP status code
 * - Request duration in milliseconds
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

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

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

        // Start timer for duration calculation
        long startTime = System.currentTimeMillis();

        try {
            // Log incoming request
            logRequest(request, requestId);

            // Continue with the filter chain
            filterChain.doFilter(request, response);

        } finally {
            // Calculate request duration
            long duration = System.currentTimeMillis() - startTime;

            // Log response (always executed, even if exception occurs)
            logResponse(request, response, requestId, duration);

            // Clear MDC to prevent memory leaks
            MDC.clear();
        }
    }

    /**
     * Logs the incoming HTTP request with masked sensitive data.
     */
    private void logRequest(HttpServletRequest request, String requestId) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        // Mask sensitive query parameters
        String maskedQuery = "";
        if (queryString != null && !queryString.isEmpty()) {
            maskedQuery = "?" + sensitiveDataMasker.maskQueryParams(queryString);
        }

        log.info("[REQUEST] {} | {} | {}{}", requestId, method, uri, maskedQuery);
    }

    /**
     * Logs the HTTP response with status code and duration.
     */
    private void logResponse(HttpServletRequest request,
                            HttpServletResponse response,
                            String requestId,
                            long duration) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();

        log.info("[RESPONSE] {} | {} | {} | {} | {}ms", requestId, method, uri, status, duration);

        // Log warning for slow requests (> 1 second)
        if (duration > 1000) {
            log.warn("[SLOW REQUEST] {} | {} | {} took {}ms", requestId, method, uri, duration);
        }

        // Log error for failed requests
        if (status >= 500) {
            log.error("[SERVER ERROR] {} | {} | {} returned {}", requestId, method, uri, status);
        } else if (status == 401 || status == 403) {
            log.warn("[AUTH FAILURE] {} | {} | {} returned {}", requestId, method, uri, status);
        }
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
        if (path.startsWith("/actuator/health")) {
            return true;
        }

        return false;
    }
}
