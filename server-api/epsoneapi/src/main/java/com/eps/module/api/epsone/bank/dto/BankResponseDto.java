package com.eps.module.api.epsone.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponseDto {

    private Long id;
    private String bankName;
    private String rbiBankCode;
    private String epsBankCode;
    private String bankCodeAlt;
    private String bankLogo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
