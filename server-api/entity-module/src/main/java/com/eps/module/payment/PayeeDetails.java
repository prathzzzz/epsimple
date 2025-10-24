package com.eps.module.payment;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.bank.Bank;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payee_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayeeDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payee_name", nullable = false, length = 255)
    private String payeeName;

    @Column(name = "pan_number", length = 255)
    private String panNumber;

    @Column(name = "aadhaar_number", length = 255)
    private String aadhaarNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "ifsc_code", length = 20)
    private String ifscCode;

    @Column(name = "beneficiary_name", length = 255)
    private String beneficiaryName;

    @Column(name = "account_number", length = 255)
    private String accountNumber;
}
