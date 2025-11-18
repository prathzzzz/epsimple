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
    private String categoryCode;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;
    
    // Automatically uppercase and remove hyphens from category code
    public void setCategoryCode(String categoryCode) {
        if (categoryCode != null) {
            this.categoryCode = categoryCode.toUpperCase().trim().replace("-", "");
        } else {
            this.categoryCode = null;
        }
    }
}
