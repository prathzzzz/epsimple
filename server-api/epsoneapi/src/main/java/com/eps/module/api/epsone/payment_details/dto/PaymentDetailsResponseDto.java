package com.eps.module.api.epsone.payment_details.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsResponseDto {

    private Long id;
    private Long paymentMethodId;
    private String paymentMethodName;
    private LocalDate paymentDate;
    private BigDecimal paymentAmount;
    private String transactionNumber;
    private String vpa;
    private String beneficiaryName;
    private String beneficiaryAccountNumber;
    private String paymentRemarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
