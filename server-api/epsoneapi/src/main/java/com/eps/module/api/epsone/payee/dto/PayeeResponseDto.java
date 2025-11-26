package com.eps.module.api.epsone.payee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayeeResponseDto {

    private Long id;
    private Long payeeTypeId;
    private String payeeTypeName;
    private Long payeeDetailsId;
    private String payeeName;
    private String accountNumber;
    private String bankName;
    private Long vendorId;
    private String vendorName;
    private Long landlordId;
    private String landlordName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
