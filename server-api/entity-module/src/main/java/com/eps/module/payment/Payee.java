package com.eps.module.payment;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.vendor.Landlord;
import com.eps.module.vendor.Vendor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "payee_type_id", nullable = false)
        private PayeeType payeeType;

        @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "payee_details_id", nullable = false, unique = true)
        private PayeeDetails payeeDetails;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "vendor_id")
        private Vendor vendor;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "landlord_id")
        private Landlord landlord;
}
