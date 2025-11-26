package com.eps.module.auth.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Custom authentication details that includes the user ID for auditing purposes.
 * This allows the AuditorAware to retrieve the user ID without making a database query,
 * which would otherwise cause infinite recursion during entity flush operations.
 */
@Getter
public class AuditingAuthenticationDetails extends WebAuthenticationDetails {

    private final Long userId;

    public AuditingAuthenticationDetails(HttpServletRequest request, Long userId) {
        super(request);
        this.userId = userId;
    }
}
