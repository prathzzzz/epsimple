package com.eps.module.cost;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.bank.ManagedProject;
import com.eps.module.payment.Voucher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "expenditures_voucher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpendituresVoucher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "cost_item_id", nullable = false)
        private CostItem costItem;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "voucher_id", nullable = false)
        private Voucher voucher;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "managed_project_id", nullable = false)
        private ManagedProject managedProject;

    @Column(name = "incurred_date")
    private LocalDate incurredDate;

    @Column(columnDefinition = "TEXT")
    private String description;
}
