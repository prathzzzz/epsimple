package com.eps.module.api.epsone.payee_details.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayeeDetailsResponseDto {

    private Long id;
    private String payeeName;
    private String panNumber;
    private String aadhaarNumber;
    private Long bankId;
    private String bankName;
    private String ifscCode;
    private String beneficiaryName;
    private String accountNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
