package com.eps.module.api.epsone.site_category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteCategoryRequestDto {

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    private String categoryName;

    @Size(max = 20, message = "Category code cannot exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Category code must be uppercase alphanumeric with hyphens/underscores")
    private String categoryCode;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;
}
