package com.eps.module.status;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "generic_status_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericStatusType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Status name is required")
    @Size(max = 100, message = "Status name cannot exceed 100 characters")
    @Column(name = "status_name", nullable = false, unique = true, length = 100)
    private String statusName;

    @Size(max = 20, message = "Status code cannot exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Status code must be uppercase alphanumeric with hyphens/underscores")
    @Column(name = "status_code", unique = true, length = 20)
    private String statusCode;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
}
