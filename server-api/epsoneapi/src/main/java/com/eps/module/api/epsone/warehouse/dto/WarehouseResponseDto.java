package com.eps.module.api.epsone.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponseDto {

    private Long id;
    private String warehouseName;
    private String warehouseCode;
    private String warehouseType;
    private Long locationId;
    private String locationName;
    private String cityName;
    private String stateName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
