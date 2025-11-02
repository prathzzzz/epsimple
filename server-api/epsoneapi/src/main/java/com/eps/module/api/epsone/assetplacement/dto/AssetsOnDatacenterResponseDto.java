package com.eps.module.api.epsone.assetplacement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate scrappedOn;
}
