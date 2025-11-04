package com.eps.module.api.epsone.payment_details.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsRequestDto {

    @NotNull(message = "Payment method ID is required")
    private Long paymentMethodId;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Payment amount must have at most 10 digits and 2 decimal places")
    private BigDecimal paymentAmount;

    @Size(max = 255, message = "Transaction number must not exceed 255 characters")
    private String transactionNumber;

    @Size(max = 255, message = "VPA must not exceed 255 characters")
    private String vpa;

    @Size(max = 255, message = "Beneficiary name must not exceed 255 characters")
    private String beneficiaryName;

    @Size(max = 255, message = "Beneficiary account number must not exceed 255 characters")
    private String beneficiaryAccountNumber;

    @Size(max = 5000, message = "Payment remarks must not exceed 5000 characters")
    private String paymentRemarks;
}
