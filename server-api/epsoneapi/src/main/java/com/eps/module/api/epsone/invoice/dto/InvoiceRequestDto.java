package com.eps.module.api.epsone.invoice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestDto {

    @NotBlank(message = "Invoice number is required")
    @Size(max = 100, message = "Invoice number must not exceed 100 characters")
    private String invoiceNumber;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    private LocalDate invoiceReceivedDate;

    @Size(max = 100, message = "Order number must not exceed 100 characters")
    private String orderNumber;

    @Size(max = 255, message = "Vendor name must not exceed 255 characters")
    private String vendorName;

    @NotNull(message = "Payee ID is required")
    private Long payeeId;

    private Long paymentDetailsId;

    private LocalDate paymentDueDate;

    @Size(max = 20, message = "Payment status must not exceed 20 characters")
    private String paymentStatus;

    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Quantity must have at most 10 digits and 2 decimal places")
    private BigDecimal quantity;

    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit;

    @DecimalMin(value = "0.0", message = "Unit price must be positive")
    @Digits(integer = 12, fraction = 2, message = "Unit price must have at most 12 digits and 2 decimal places")
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.0", message = "Tax CGST percentage must be positive")
    @DecimalMax(value = "100.0", message = "Tax CGST percentage must not exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Tax CGST percentage must have at most 3 digits and 2 decimal places")
    private BigDecimal taxCgstPercentage;

    @DecimalMin(value = "0.0", message = "Tax SGST percentage must be positive")
    @DecimalMax(value = "100.0", message = "Tax SGST percentage must not exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Tax SGST percentage must have at most 3 digits and 2 decimal places")
    private BigDecimal taxSgstPercentage;

    @DecimalMin(value = "0.0", message = "Tax IGST percentage must be positive")
    @DecimalMax(value = "100.0", message = "Tax IGST percentage must not exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Tax IGST percentage must have at most 3 digits and 2 decimal places")
    private BigDecimal taxIgstPercentage;

    @DecimalMin(value = "0.0", message = "Basic amount must be positive")
    @Digits(integer = 12, fraction = 2, message = "Basic amount must have at most 12 digits and 2 decimal places")
    private BigDecimal basicAmount;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal cgst;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal sgst;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal igst;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount1;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount2;

    @DecimalMin(value = "0.0", message = "Discount percentage must be positive")
    @DecimalMax(value = "100.0", message = "Discount percentage must not exceed 100")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal discountPercentage;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal discountAmount;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal tds;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal advanceAmount;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal totalAmount;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal totalInvoiceValue;

    @Digits(integer = 12, fraction = 2)
    private BigDecimal netPayable;

    private LocalDate paidDate;

    @Size(max = 100, message = "Machine serial number must not exceed 100 characters")
    private String machineSerialNumber;

    @Size(max = 100, message = "Master PO number must not exceed 100 characters")
    private String masterPoNumber;

    private LocalDate masterPoDate;

    @Size(max = 100, message = "Dispatch order number must not exceed 100 characters")
    private String dispatchOrderNumber;

    private LocalDate dispatchOrderDate;

    @Size(max = 255, message = "UTR detail must not exceed 255 characters")
    private String utrDetail;

    @Size(max = 100, message = "Billed by vendor GST must not exceed 100 characters")
    private String billedByVendorGst;

    @Size(max = 100, message = "Billed to EPS GST must not exceed 100 characters")
    private String billedToEpsGst;

    @Size(max = 5000, message = "Remarks must not exceed 5000 characters")
    private String remarks;
}
