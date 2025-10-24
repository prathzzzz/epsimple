package com.eps.module.asset;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.site.Site;
import com.eps.module.warehouse.Datacenter;
import com.eps.module.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asset_movement_tracker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetMovementTracker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "asset_id", nullable = false)
        private Asset asset;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "asset_movement_type_id", nullable = false)
        private AssetMovementType assetMovementType;

    @Column(name = "from_factory", length = 255)
    private String fromFactory;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_site_id")
        private Site fromSite;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_site_id")
        private Site toSite;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_warehouse_id")
        private Warehouse fromWarehouse;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_warehouse_id")
        private Warehouse toWarehouse;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_datacenter_id")
        private Datacenter fromDatacenter;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_datacenter_id")
        private Datacenter toDatacenter;

}
