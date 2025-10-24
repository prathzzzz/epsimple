package com.eps.module.payment;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 100)
    private String invoiceNumber;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "invoice_received_date")
    private LocalDate invoiceReceivedDate;

    @Column(name = "order_number", length = 100)
    private String orderNumber;

    @Column(name = "vendor_name", length = 255)
    private String vendorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payee_id", nullable = false)
    private Payee payee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_details_id")
    private PaymentDetails paymentDetails;

    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(length = 50)
    private String unit;

    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "tax_cgst_percentage", precision = 5, scale = 2)
    private BigDecimal taxCgstPercentage;

    @Column(name = "tax_sgst_percentage", precision = 5, scale = 2)
    private BigDecimal taxSgstPercentage;

    @Column(name = "tax_igst_percentage", precision = 5, scale = 2)
    private BigDecimal taxIgstPercentage;

    @Column(name = "basic_amount", precision = 12, scale = 2)
    private BigDecimal basicAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal cgst;

    @Column(precision = 12, scale = 2)
    private BigDecimal sgst;

    @Column(precision = 12, scale = 2)
    private BigDecimal igst;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount1;

    @Column(precision = 12, scale = 2)
    private BigDecimal amount2;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "discount_amount", precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal tds;

    @Column(name = "advance_amount", precision = 12, scale = 2)
    private BigDecimal advanceAmount;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "total_invoice_value", precision = 12, scale = 2)
    private BigDecimal totalInvoiceValue;

    @Column(name = "net_payable", precision = 12, scale = 2)
    private BigDecimal netPayable;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "machine_serial_number", length = 100)
    private String machineSerialNumber;

    @Column(name = "master_po_number", length = 100)
    private String masterPoNumber;

    @Column(name = "master_po_date")
    private LocalDate masterPoDate;

    @Column(name = "dispatch_order_number", length = 100)
    private String dispatchOrderNumber;

    @Column(name = "dispatch_order_date")
    private LocalDate dispatchOrderDate;

    @Column(name = "utr_detail", length = 255)
    private String utrDetail;

    @Column(name = "billed_by_vendor_gst", length = 100)
    private String billedByVendorGst;

    @Column(name = "billed_to_eps_gst", length = 100)
    private String billedToEpsGst;

    @Column(columnDefinition = "TEXT")
    private String remarks;
}
