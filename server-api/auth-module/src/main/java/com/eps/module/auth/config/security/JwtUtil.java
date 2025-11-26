package com.eps.module.auth.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        
        if (!isValid) {
            log.warn("JWT token validation failed for user: {}", username);
            if (isTokenExpired(token)) {
                log.warn("JWT token expired for user: {}", username);
            }
            if (!username.equals(userDetails.getUsername())) {
                log.warn("JWT token username mismatch: {} vs {}", username, userDetails.getUsername());
            }
        } else {
            log.debug("JWT token validation successful for user: {}", username);
        }
        
        return isValid;
    }

    public String generateToken(String username) {
        log.debug("Generating JWT token for user: {}", username);
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, username);
        log.debug("JWT token generated successfully for user: {}", username);
        return token;
    }

    public String generateToken(String username, Long userId) {
        log.debug("Generating JWT token for user: {} with ID: {}", username, userId);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        String token = createToken(claims, username);
        log.debug("JWT token generated successfully for user: {}", username);
        return token;
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object userId = claims.get("userId");
            if (userId != null) {
                if (userId instanceof Integer) {
                    return ((Integer) userId).longValue();
                } else if (userId instanceof Long) {
                    return (Long) userId;
                } else if (userId instanceof Number) {
                    return ((Number) userId).longValue();
                }
            }
            return null;
        } catch (Exception e) {
            log.warn("Failed to extract userId from token: {}", e.getMessage());
            return null;
        }
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
