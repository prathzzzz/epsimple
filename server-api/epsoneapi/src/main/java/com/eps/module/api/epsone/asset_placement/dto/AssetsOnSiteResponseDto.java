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
public class AssetsOnSiteResponseDto {

    private Long id;

    private Long assetId;
    private String assetTagId;
    private String assetName;
    private String assetTypeName;
    private String assetCategoryName;

    private Long siteId;
    private String siteCode;
    private String siteName;

    private Long assetStatusId;
    private String assetStatusName;

    private Long activityWorkId;
    private String activityWorkNumber;

    private Long assetMovementTrackerId;

    private LocalDate assignedOn;
    private LocalDate deliveredOn;
    private LocalDate deployedOn;
    private LocalDate activatedOn;
    private LocalDate decommissionedOn;
    private LocalDate vacatedOn;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
