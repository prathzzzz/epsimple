package com.eps.module.api.epsone.assetplacement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetsOnSiteRequestDto {

    @NotNull(message = "Asset ID is required")
    private Long assetId;

    @NotNull(message = "Site ID is required")
    private Long siteId;

    @NotNull(message = "Asset status ID is required")
    private Long assetStatusId;

    private Long activityWorkId;

    private Long assetMovementTrackerId;

    private LocalDate assignedOn;

    private LocalDate deliveredOn;

    private LocalDate deployedOn;

    private LocalDate activatedOn;

    private LocalDate decommissionedOn;

    private LocalDate vacatedOn;
}
