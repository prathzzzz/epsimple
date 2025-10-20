package com.eps.module.auth.service;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eps.module.auth.dto.ForgotPasswordRequest;
import com.eps.module.auth.dto.ResetPasswordRequest;
import com.eps.module.auth.entity.PasswordResetToken;
import com.eps.module.auth.entity.User;
import com.eps.module.auth.repository.PasswordResetTokenRepository;
import com.eps.module.auth.repository.UserRepository;
import com.eps.module.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${password.reset.token.expiration}")
    private Long tokenExpiration;

    @Transactional
    public void initiatePasswordReset(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Delete any existing tokens for this user
        tokenRepository.deleteByUser(user);

        // Generate reset token
        String resetToken = generateResetToken();

        // Create and save password reset token
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(resetToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plusSeconds(tokenExpiration / 1000))
                .isUsed(false)
                .build();

        tokenRepository.save(passwordResetToken);

        // Send email
        emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getName() != null ? user.getName() : "User",
                resetToken
        );

        log.info("Password reset initiated for user: {}", user.getEmail());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        // Validate token
        if (resetToken.getIsUsed()) {
            throw new RuntimeException("Reset token has already been used");
        }

        if (resetToken.isExpired()) {
            throw new RuntimeException("Reset token has expired");
        }

        // Update user password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        resetToken.setIsUsed(true);
        tokenRepository.save(resetToken);

        log.info("Password reset successfully for user: {}", user.getEmail());
    }

    private String generateResetToken() {
        // Generate a 6-digit token
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}
