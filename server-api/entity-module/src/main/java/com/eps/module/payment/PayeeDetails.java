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

    @Column(name = "pan_number", length = 512)
    private String panNumber;

    @Column(name = "pan_number_hash", length = 64)
    private String panNumberHash;

    @Column(name = "aadhaar_number", length = 512)
    private String aadhaarNumber;

    @Column(name = "aadhaar_number_hash", length = 64)
    private String aadhaarNumberHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "ifsc_code", length = 20)
    private String ifscCode;

    @Column(name = "beneficiary_name", length = 512)
    private String beneficiaryName;

    @Column(name = "beneficiary_name_hash", length = 64)
    private String beneficiaryNameHash;

    @Column(name = "account_number", length = 512)
    private String accountNumber;

    @Column(name = "account_number_hash", length = 64)
    private String accountNumberHash;
}
