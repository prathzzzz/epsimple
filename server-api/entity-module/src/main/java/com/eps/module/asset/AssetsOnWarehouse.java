package com.eps.module.asset;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.activity.ActivityWork;
import com.eps.module.status.GenericStatusType;
import com.eps.module.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "assets_on_warehouse",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"asset_id", "warehouse_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetsOnWarehouse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "asset_id", nullable = false)
        private Asset asset;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "warehouse_id", nullable = false)
        private Warehouse warehouse;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "asset_status_id", nullable = false)
        private GenericStatusType assetStatus;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "activity_work_id")
        private ActivityWork activityWork;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "asset_movement_tracker_id")
        private AssetMovementTracker assetMovementTracker;

    @Column(name = "assigned_on")
    private LocalDate assignedOn;

    @Column(name = "delivered_on")
    private LocalDate deliveredOn;

    @Column(name = "commissioned_on")
    private LocalDate commissionedOn;

    @Column(name = "vacated_on")
    private LocalDate vacatedOn;

    @Column(name = "disposed_on")
    private LocalDate disposedOn;

    @Column(name = "scrapped_on")
    private LocalDate scrappedOn;
}
