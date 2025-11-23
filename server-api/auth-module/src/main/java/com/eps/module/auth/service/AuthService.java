package com.eps.module.auth.service;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eps.module.auth.config.security.JwtUtil;
import com.eps.module.auth.dto.AuthResponse;
import com.eps.module.auth.dto.ChangePasswordRequest;
import com.eps.module.auth.dto.LoginRequest;
import com.eps.module.auth.dto.RegisterRequest;
import com.eps.module.auth.dto.UserDTO;
import com.eps.module.auth.entity.Role;
import com.eps.module.auth.entity.User;
import com.eps.module.auth.mapper.UserMapper;
import com.eps.module.auth.repository.RoleRepository;
import com.eps.module.auth.repository.UserRepository;
import com.eps.module.common.exception.BadRequestException;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.common.exception.UnauthorizedException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Value("${jwt.cookie-name}")
    private String cookieName;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
        log.debug("Starting registration process for email: {}", request.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already exists: {}", request.getEmail());
            throw new ConflictException("Email already exists");
        }

        // Get default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> {
                    log.error("Default USER role not found in database");
                    return new ResourceNotFoundException("Default USER role not found");
                });

        // Create new user
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .isActive(true)
                .roles(roles)
                .build();

        user = userRepository.save(user);
        log.info("User created with ID: {} for email: {}", user.getId(), user.getEmail());

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());
        log.debug("JWT token generated for user: {}", user.getEmail());

        // Set JWT in cookie
        setJwtCookie(response, token);

        // Map user to DTO
        UserDTO userDTO = userMapper.toDTO(user);

        return AuthResponse.builder()
                .message("User registered successfully")
                .user(userDTO)
                .build();
    }

    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        log.debug("Attempting authentication for user: {}", request.getEmail());
        
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            if (!authentication.isAuthenticated()) {
                log.warn("Authentication failed for user: {}", request.getEmail());
                throw new UnauthorizedException("Invalid credentials");
            }
            
            log.debug("Authentication successful for user: {}", request.getEmail());
        } catch (Exception e) {
            log.error("Authentication error for user: {}", request.getEmail(), e);
            throw e;
        }

        // Get user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found after successful authentication: {}", request.getEmail());
                    return new ResourceNotFoundException("User not found");
                });

        // Check if user is active
        if (!user.getIsActive()) {
            log.warn("Login attempt for deactivated account: {}", request.getEmail());
            throw new UnauthorizedException("User account is deactivated");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());
        log.debug("JWT token generated for user: {}", user.getEmail());

        // Set JWT in cookie
        setJwtCookie(response, token);
        log.info("User logged in successfully: {} (ID: {})", user.getEmail(), user.getId());

        // Map user to DTO
        UserDTO userDTO = userMapper.toDTO(user);

        return AuthResponse.builder()
                .message("Login successful")
                .user(userDTO)
                .build();
    }

    public UserDTO getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Attempt to get current user without authentication");
            throw new UnauthorizedException("User not authenticated");
        }

        String email = authentication.getName();
        log.debug("Fetching current user data for: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Authenticated user not found in database: {}", email);
                    return new ResourceNotFoundException("User not found");
                });

        if (!user.getIsActive()) {
            log.warn("Attempt to access deactivated account: {}", email);
            throw new UnauthorizedException("User account is deactivated");
        }

        return userMapper.toDTO(user);
    }

    public void logout(HttpServletResponse response) {
        log.debug("Clearing JWT cookie for logout");
        // Clear JWT cookie
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        log.info("User logged out successfully");
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(expiration.intValue() / 1000); // Convert to seconds
        response.addCookie(cookie);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Attempt to change password without authentication");
            throw new UnauthorizedException("User not authenticated");
        }

        // Validate that new password matches confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        String email = authentication.getName();
        log.debug("Password change requested for user: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found during password change: {}", email);
                    return new ResourceNotFoundException("User not found");
                });

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            log.warn("Invalid current password provided for user: {}", email);
            throw new UnauthorizedException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        log.info("Password changed successfully for user: {}", email);
    }
}
