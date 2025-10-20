package com.eps.module.auth.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eps.module.auth.dto.AuthResponse;
import com.eps.module.auth.dto.ForgotPasswordRequest;
import com.eps.module.auth.dto.LoginRequest;
import com.eps.module.auth.dto.RegisterRequest;
import com.eps.module.auth.dto.ResetPasswordRequest;
import com.eps.module.auth.dto.UserDTO;
import com.eps.module.auth.service.AuthService;
import com.eps.module.auth.service.PasswordResetService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {
        log.info("Registration attempt for email: {}", request.getEmail());
        AuthResponse authResponse = authService.register(request, response);
        log.info("User registered successfully: {}", request.getEmail());
        return ResponseBuilder.success(authResponse, "User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        log.info("Login attempt for email: {}", request.getEmail());
        AuthResponse authResponse = authService.login(request, response);
        log.info("Login successful for email: {}", request.getEmail());
        return ResponseBuilder.success(authResponse, "Login successful", HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        log.info("Logout request received");
        authService.logout(response);
        log.info("Logout successful");
        return ResponseBuilder.success(null, "Logout successful", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(Authentication authentication) {
        log.debug("Get current user request");
        UserDTO userDTO = authService.getCurrentUser(authentication);
        return ResponseBuilder.success(userDTO, "User retrieved successfully", HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());
        passwordResetService.initiatePasswordReset(request);
        log.info("Password reset email sent to: {}", request.getEmail());
        return ResponseBuilder.success(null, "Password reset email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Password reset confirmation received");
        passwordResetService.resetPassword(request);
        log.info("Password reset completed successfully");
        return ResponseBuilder.success(null, "Password reset successfully", HttpStatus.OK);
    }
}
