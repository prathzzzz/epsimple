package com.eps.module.auth.service;

import com.eps.module.auth.entity.User;
import com.eps.module.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", username);
                    return new UsernameNotFoundException("User not found with email: " + username);
                });

        log.debug("User loaded successfully: {} with {} roles", username, user.getRoles().size());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getIsActive(),
                true,
                true,
                true,
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add roles and permissions (only active ones)
        user.getRoles().stream()
                .filter(role -> role.getIsActive() != null && role.getIsActive())
                .forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

                    // Add permissions from roles (only active ones)
                    role.getPermissions().stream()
                            .filter(permission -> permission.getIsActive() != null && permission.getIsActive())
                            .forEach(permission ->
                                    authorities.add(new SimpleGrantedAuthority(permission.getName()))
                            );
                });

        return authorities;
    }
}
