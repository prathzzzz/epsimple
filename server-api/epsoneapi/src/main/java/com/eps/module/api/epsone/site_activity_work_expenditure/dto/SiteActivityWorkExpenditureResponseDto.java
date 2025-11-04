package com.eps.module.api.epsone.site_activity_work_expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteActivityWorkExpenditureResponseDto {

    private Long id;
    
    // Site details
    private Long siteId;
    private String siteCode;
    private String siteName;
    
    // Activity Work details
    private Long activityWorkId;
    private String vendorOrderNumber;
    private String activityName;
    
    // Expenditure Invoice details
    private Long expendituresInvoiceId;
    private String invoiceNumber;
    private Double amount;
    private String costItemName;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
