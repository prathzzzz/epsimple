package com.eps.module.api.epsone.voucher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponseDto {

    private Long id;
    private String voucherNumber;
    private LocalDate voucherDate;
    private String orderNumber;
    private Long payeeId;
    private String payeeName;
    private String payeeTypeName;
    private Long paymentDetailsId;
    private String transactionNumber;
    private LocalDate paymentDueDate;
    private String paymentStatus;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal taxCgst;
    private BigDecimal taxSgst;
    private BigDecimal taxIgst;
    private BigDecimal amount1;
    private BigDecimal amount2;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
