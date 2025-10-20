package com.eps.module.site;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "site_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    private String categoryName;

    @Size(max = 20, message = "Category code cannot exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Category code must be uppercase alphanumeric with hyphens/underscores")
    @Column(name = "category_code", unique = true, length = 20)
    private String categoryCode;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
}
