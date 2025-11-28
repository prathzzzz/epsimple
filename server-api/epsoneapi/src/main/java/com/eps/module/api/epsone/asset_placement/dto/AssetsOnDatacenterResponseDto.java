package com.eps.module.api.epsone.asset_placement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetsOnDatacenterResponseDto {

    private Long id;

    private Long assetId;
    private String assetTagId;
    private String assetName;
    private String assetTypeName;
    private String assetCategoryName;

    private Long datacenterId;
    private String datacenterCode;
    private String datacenterName;

    private Long assetStatusId;
    private String assetStatusName;

    private Long activityWorkId;
    private String activityWorkNumber;

    private Long assetMovementTrackerId;

    private LocalDate assignedOn;
    private LocalDate deliveredOn;
    private LocalDate commissionedOn;
    private LocalDate vacatedOn;
    private LocalDate disposedOn;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
