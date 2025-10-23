package com.eps.module.api.epsone.sitetype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteTypeRequestDto {

    @NotBlank(message = "Site type name is required")
    @Size(max = 100, message = "Type name cannot exceed 100 characters")
    private String typeName;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;
}
