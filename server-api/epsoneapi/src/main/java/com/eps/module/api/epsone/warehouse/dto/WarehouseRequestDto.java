package com.eps.module.api.epsone.warehouse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequestDto {

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 255, message = "Warehouse name cannot exceed 255 characters")
    private String warehouseName;

    @Size(max = 50, message = "Warehouse code cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]*$", message = "Warehouse code must contain only uppercase letters, numbers, hyphens, and underscores")
    private String warehouseCode;

    @Size(max = 100, message = "Warehouse type cannot exceed 100 characters")
    private String warehouseType;

    @NotNull(message = "Location is required")
    private Long locationId;
}
