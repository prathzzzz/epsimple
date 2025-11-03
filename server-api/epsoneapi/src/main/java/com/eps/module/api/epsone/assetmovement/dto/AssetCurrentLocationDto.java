package com.eps.module.api.epsone.assetmovement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetCurrentLocationDto {

    private Long assetId;
    private String assetTagId;
    private String assetName;

    @JsonProperty("isPlaced")
    private boolean isPlaced;
    private String locationType; // "site", "warehouse", "datacenter"

    private Long locationId;
    private String locationCode;
    private String locationName;

    private Long placementId;
    private LocalDate assignedOn;
    private LocalDate deliveredOn;

    private String assetStatusName;
    private Long activityWorkId;
    private String activityWorkNumber;
}
