package com.eps.module.asset;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.activity.ActivityWork;
import com.eps.module.status.GenericStatusType;
import com.eps.module.warehouse.Datacenter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "assets_on_datacenter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetsOnDatacenter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "asset_id", nullable = false)
        private Asset asset;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "datacenter_id", nullable = false)
        private Datacenter datacenter;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "asset_status_id", nullable = false)
        private GenericStatusType assetStatus;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "activity_work_id")
        private ActivityWork activityWork;

        @ManyToOne(fetch = FetchType.LAZY)
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
}
