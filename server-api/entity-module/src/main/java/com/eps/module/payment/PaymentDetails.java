package com.eps.module.payment;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_amount", precision = 12, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "transaction_number", length = 255)
    private String transactionNumber;

    @Column(length = 255)
    private String vpa;

    @Column(name = "beneficiary_name", length = 255)
    private String beneficiaryName;

    @Column(name = "beneficiary_account_number", length = 255)
    private String beneficiaryAccountNumber;

    @Column(name = "payment_remarks", columnDefinition = "TEXT")
    private String paymentRemarks;
}
