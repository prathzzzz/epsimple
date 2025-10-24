package com.eps.module.site;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.activity.ActivityWork;
import com.eps.module.cost.ExpendituresInvoice;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "site_activity_work_expenditure",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"site_id", "activity_work_id", "expenditures_invoice_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteActivityWorkExpenditure extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "site_id", nullable = false)
        private Site site;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "activity_work_id", nullable = false)
        private ActivityWork activityWork;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "expenditures_invoice_id", nullable = false)
        private ExpendituresInvoice expendituresInvoice;
}
