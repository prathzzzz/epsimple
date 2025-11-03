package com.eps.module.api.epsone.expendituresvoucher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpendituresVoucherResponseDto {

    private Long id;

    // Cost Item details
    private Long costItemId;
    private String costItemFor;
    private String costTypeName;
    private String costCategoryName;

    // Voucher details
    private Long voucherId;
    private String voucherNumber;
    private LocalDate voucherDate;
    private String payeeName;

    // Managed Project details
    private Long managedProjectId;
    private String projectName;
    private String projectCode;
    private String bankName;

    // Expenditure specific fields
    private LocalDate incurredDate;
    private String description;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
