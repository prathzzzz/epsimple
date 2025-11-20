package com.eps.module.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import com.eps.module.common.entity.BaseEntity;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String scope; // Resource scope (e.g., ASSET, SITE, BANK)

    @Column(length = 50)
    private String action; // Action type (CREATE, READ, UPDATE, DELETE, BULK_UPLOAD, EXPORT)

    @Column(nullable = false)
    @Builder.Default
    private Boolean isSystemPermission = true; // Distinguish pre-seeded vs custom permissions

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true; // Enable/disable permissions

    @Column(length = 100)
    private String category; // UI grouping (e.g., "Core Masters", "Operations", "Financial")

    @ManyToMany(mappedBy = "permissions")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
