package com.eps.module.api.epsone.data_center.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatacenterRequestDto {

    @NotBlank(message = "Datacenter name is required")
    @Size(max = 255, message = "Datacenter name cannot exceed 255 characters")
    private String datacenterName;

    @Size(max = 50, message = "Datacenter code cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]*$", message = "Datacenter code must contain only uppercase letters, numbers, hyphens, and underscores")
    private String datacenterCode;

    @Size(max = 100, message = "Datacenter type cannot exceed 100 characters")
    private String datacenterType;

    @NotNull(message = "Location is required")
    private Long locationId;
}
