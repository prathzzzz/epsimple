package com.eps.module.asset;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.activity.ActivityWork;
import com.eps.module.site.Site;
import com.eps.module.status.GenericStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "assets_on_site",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"asset_id", "site_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetsOnSite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "asset_id", nullable = false)
        private Asset asset;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "site_id", nullable = false)
        private Site site;

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

    @Column(name = "deployed_on")
    private LocalDate deployedOn;

    @Column(name = "activated_on")
    private LocalDate activatedOn;

    @Column(name = "decommissioned_on")
    private LocalDate decommissionedOn;

    @Column(name = "vacated_on")
    private LocalDate vacatedOn;
}
