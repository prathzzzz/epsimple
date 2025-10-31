package com.eps.module.api.epsone.voucher.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRequestDto {

    @NotBlank(message = "Voucher number is required")
    @Size(max = 100, message = "Voucher number must not exceed 100 characters")
    private String voucherNumber;

    @NotNull(message = "Voucher date is required")
    private LocalDate voucherDate;

    @Size(max = 100, message = "Order number must not exceed 100 characters")
    private String orderNumber;

    @NotNull(message = "Payee ID is required")
    private Long payeeId;

    private Long paymentDetailsId;

    private LocalDate paymentDueDate;

    @Size(max = 20, message = "Payment status must not exceed 20 characters")
    private String paymentStatus;

    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Quantity must have at most 10 integer digits and 2 decimal places")
    private BigDecimal quantity;

    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit;

    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Digits(integer = 12, fraction = 2, message = "Unit price must have at most 12 integer digits and 2 decimal places")
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.0", inclusive = true, message = "CGST must be 0 or greater")
    @Digits(integer = 12, fraction = 2, message = "CGST must have at most 12 integer digits and 2 decimal places")
    private BigDecimal taxCgst;

    @DecimalMin(value = "0.0", inclusive = true, message = "SGST must be 0 or greater")
    @Digits(integer = 12, fraction = 2, message = "SGST must have at most 12 integer digits and 2 decimal places")
    private BigDecimal taxSgst;

    @DecimalMin(value = "0.0", inclusive = true, message = "IGST must be 0 or greater")
    @Digits(integer = 12, fraction = 2, message = "IGST must have at most 12 integer digits and 2 decimal places")
    private BigDecimal taxIgst;

    @DecimalMin(value = "0.0", inclusive = true, message = "Amount1 must be 0 or greater")
    @Digits(integer = 12, fraction = 2, message = "Amount1 must have at most 12 integer digits and 2 decimal places")
    private BigDecimal amount1;

    @DecimalMin(value = "0.0", inclusive = true, message = "Amount2 must be 0 or greater")
    @Digits(integer = 12, fraction = 2, message = "Amount2 must have at most 12 integer digits and 2 decimal places")
    private BigDecimal amount2;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount percentage must be 0 or greater")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount percentage must not exceed 100")
    @Digits(integer = 5, fraction = 2, message = "Discount percentage must have at most 5 integer digits and 2 decimal places")
    private BigDecimal discountPercentage;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount amount must be 0 or greater")
    @Digits(integer = 12, fraction = 2, message = "Discount amount must have at most 12 integer digits and 2 decimal places")
    private BigDecimal discountAmount;

    @DecimalMin(value = "0.0", inclusive = true, message = "Final amount must be 0 or greater")
    @Digits(integer = 12, fraction = 2, message = "Final amount must have at most 12 integer digits and 2 decimal places")
    private BigDecimal finalAmount;

    private String remarks;
}
