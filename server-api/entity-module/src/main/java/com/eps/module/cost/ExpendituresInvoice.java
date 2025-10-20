package com.eps.module.cost;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.bank.ManagedProject;
import com.eps.module.payment.Invoice;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "expenditures_invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpendituresInvoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "cost_item_id", nullable = false)
        private CostItem costItem;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "invoice_id", nullable = false)
        private Invoice invoice;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "managed_project_id", nullable = false)
        private ManagedProject managedProject;

    @Column(name = "incurred_date")
    private LocalDate incurredDate;

    @Column(columnDefinition = "TEXT")
    private String description;
}
