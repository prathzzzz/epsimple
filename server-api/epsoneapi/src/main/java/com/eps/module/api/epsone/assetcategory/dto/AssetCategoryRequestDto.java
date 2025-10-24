package com.eps.module.api.epsone.assetcategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetCategoryRequestDto {

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String categoryName;

    @NotBlank(message = "Category code is required")
    @Size(max = 20, message = "Category code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Category code must contain only uppercase letters, numbers, hyphens and underscores")
    private String categoryCode;

    @NotNull(message = "Asset type is required")
    private Long assetTypeId;

    @NotBlank(message = "Asset code alt is required")
    @Size(max = 10, message = "Asset code alt must not exceed 10 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Asset code alt must contain only uppercase letters, numbers, hyphens and underscores")
    private String assetCodeAlt;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
