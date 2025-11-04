package com.eps.module.api.epsone.asset_placement.dto;

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
public class AssetsOnDatacenterRequestDto {

    @NotNull(message = "Asset ID is required")
    private Long assetId;

    @NotNull(message = "Datacenter ID is required")
    private Long datacenterId;

    @NotNull(message = "Asset status ID is required")
    private Long assetStatusId;

    private Long activityWorkId;

    private Long assetMovementTrackerId;

    private LocalDate assignedOn;

    private LocalDate deliveredOn;

    private LocalDate commissionedOn;

    private LocalDate vacatedOn;

    private LocalDate disposedOn;

    private LocalDate scrappedOn;
}
