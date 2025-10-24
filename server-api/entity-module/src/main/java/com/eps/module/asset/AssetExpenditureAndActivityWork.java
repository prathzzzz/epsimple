package com.eps.module.asset;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.activity.ActivityWork;
import com.eps.module.cost.ExpendituresInvoice;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "asset_expenditure_and_activity_work",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"asset_id", "expenditures_invoice_id", "activity_work_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetExpenditureAndActivityWork extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenditures_invoice_id")
    private ExpendituresInvoice expendituresInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_work_id")
    private ActivityWork activityWork;
}
