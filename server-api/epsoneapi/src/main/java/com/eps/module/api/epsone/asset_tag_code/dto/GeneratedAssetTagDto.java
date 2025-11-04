package com.eps.module.api.epsone.asset_tag_code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedAssetTagDto {

    private String assetTag;
    private String assetCategoryCode;
    private String vendorCode;
    private String bankCode;
    private Integer sequence;
    private Integer nextSequence;
}
