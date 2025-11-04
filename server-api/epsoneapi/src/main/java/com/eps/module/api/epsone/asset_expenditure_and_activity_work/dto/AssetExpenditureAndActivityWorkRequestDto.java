package com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetExpenditureAndActivityWorkRequestDto {

    @NotNull(message = "Asset ID is required")
    private Long assetId;

    private Long expendituresInvoiceId;

    private Long activityWorkId;
}
