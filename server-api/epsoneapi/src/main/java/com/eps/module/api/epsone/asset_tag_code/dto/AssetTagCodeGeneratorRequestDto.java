package com.eps.module.api.epsone.asset_tag_code.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetTagCodeGeneratorRequestDto {

    @NotNull(message = "Asset category ID is required")
    private Long assetCategoryId;

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotNull(message = "Bank ID is required")
    private Long bankId;

    @Builder.Default
    @Min(value = 1, message = "Max sequence digits must be at least 1")
    @Max(value = 10, message = "Max sequence digits cannot exceed 10")
    private Integer maxSeqDigit = 5;

    @Builder.Default
    @Min(value = 1, message = "Running sequence must be at least 1")
    private Integer runningSeq = 1;
}
