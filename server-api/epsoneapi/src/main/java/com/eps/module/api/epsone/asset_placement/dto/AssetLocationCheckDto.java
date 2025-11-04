package com.eps.module.api.epsone.asset_placement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetLocationCheckDto {
    private boolean isPlaced;
    private String locationType; // "site", "warehouse", "datacenter"
    private Long locationId;
    private String locationName;
    private String locationCode;
    private String assetTagId;
}
