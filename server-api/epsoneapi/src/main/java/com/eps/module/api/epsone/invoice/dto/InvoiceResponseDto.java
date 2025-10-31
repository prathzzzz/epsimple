package com.eps.module.api.epsone.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {

    private Long id;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private LocalDate invoiceReceivedDate;
    private String orderNumber;
    private String vendorName;
    
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
    
    private BigDecimal taxCgstPercentage;
    private BigDecimal taxSgstPercentage;
    private BigDecimal taxIgstPercentage;
    
    private BigDecimal basicAmount;
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal igst;
    
    private BigDecimal amount1;
    private BigDecimal amount2;
    
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    
    private BigDecimal tds;
    private BigDecimal advanceAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalInvoiceValue;
    private BigDecimal netPayable;
    
    private LocalDate paidDate;
    
    private String machineSerialNumber;
    private String masterPoNumber;
    private LocalDate masterPoDate;
    private String dispatchOrderNumber;
    private LocalDate dispatchOrderDate;
    
    private String utrDetail;
    private String billedByVendorGst;
    private String billedToEpsGst;
    
    private String remarks;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
