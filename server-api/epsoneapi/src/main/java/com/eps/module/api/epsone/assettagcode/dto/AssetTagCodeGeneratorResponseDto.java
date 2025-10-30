package com.eps.module.api.epsone.assettagcode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetTagCodeGeneratorResponseDto {

    private Long id;
    private Long assetCategoryId;
    private String assetCategoryName;
    private String assetCategoryCode;
    private Long vendorId;
    private String vendorName;
    private String vendorCodeAlt;
    private Long bankId;
    private String bankName;
    private String bankCode;
    private Integer maxSeqDigit;
    private Integer runningSeq;
}
