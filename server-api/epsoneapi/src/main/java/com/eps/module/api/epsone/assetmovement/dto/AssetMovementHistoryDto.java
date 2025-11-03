package com.eps.module.api.epsone.assetmovement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetMovementHistoryDto {

    private Long id;
    private Long assetId;
    private String assetTagId;
    private String assetName;

    private Long movementTypeId;
    private String movementType;
    private String movementDescription;

    // From location details
    private String fromFactory;
    private Long fromSiteId;
    private String fromSiteCode;
    private String fromSiteName;
    private Long fromWarehouseId;
    private String fromWarehouseCode;
    private String fromWarehouseName;
    private Long fromDatacenterId;
    private String fromDatacenterCode;
    private String fromDatacenterName;

    // To location details
    private Long toSiteId;
    private String toSiteCode;
    private String toSiteName;
    private Long toWarehouseId;
    private String toWarehouseCode;
    private String toWarehouseName;
    private Long toDatacenterId;
    private String toDatacenterCode;
    private String toDatacenterName;

    private LocalDateTime movedAt;
    private LocalDateTime createdAt;
}
