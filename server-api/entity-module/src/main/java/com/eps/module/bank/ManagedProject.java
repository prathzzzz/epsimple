package com.eps.module.bank;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "managed_project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagedProject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Size(max = 50, message = "Project type cannot exceed 50 characters")
    @Column(name = "project_type", length = 50)
    private String projectType;

    @NotBlank(message = "Project name is required")
    @Size(max = 255, message = "Project name cannot exceed 255 characters")
    @Column(name = "project_name", nullable = false, length = 255)
    private String projectName;

    @NotBlank(message = "Project code is required")
    @Size(max = 50, message = "Project code cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Project code can only contain letters, numbers, hyphens and underscores")
    @Column(name = "project_code", nullable = false, unique = true, length = 50)
    private String projectCode;

    @Size(max = 5000, message = "Project description cannot exceed 5000 characters")
    @Column(name = "project_description", columnDefinition = "TEXT")
    private String projectDescription;
}
