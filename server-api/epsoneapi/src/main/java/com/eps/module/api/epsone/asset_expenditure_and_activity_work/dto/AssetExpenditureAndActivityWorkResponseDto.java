package com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetExpenditureAndActivityWorkResponseDto {

    private Long id;
    
    // Asset details
    private Long assetId;
    private String assetTagId;
    private String assetName;
    
    // Activity Work details (optional)
    private Long activityWorkId;
    private String vendorOrderNumber;
    private String activityName;
    
    // Expenditure Invoice details (optional)
    private Long expendituresInvoiceId;
    private String invoiceNumber;
    private Double amount;
    private String costItemName;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
