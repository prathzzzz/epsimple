package com.eps.module.asset;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.bank.Bank;
import com.eps.module.status.GenericStatusType;
import com.eps.module.status.OwnershipStatus;
import com.eps.module.vendor.Vendor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "asset")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "asset_tag_id", nullable = false, unique = true, length = 50)
    private String assetTagId;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "asset_type_id", nullable = false)
        private AssetType assetType;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "asset_category_id", nullable = false)
        private AssetCategory assetCategory;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "vendor_id", nullable = false)
        private Vendor vendor;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "lender_bank_id", nullable = false)
        private Bank lenderBank;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "status_type_id")
        private GenericStatusType statusType;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "ownership_status_id")
        private OwnershipStatus ownershipStatus;

    @Column(name = "asset_Name")
    private String assetName;

    @Column(name = "serial_number", unique = true, length = 100)
    private String serialNumber;

    @Column(name = "model_number", length = 100)
    private String modelNumber;

    @Column(name = "purchase_order_number", length = 100)
    private String purchaseOrderNumber;

    @Column(name = "purchase_order_date")
    private LocalDate purchaseOrderDate;

    @Column(name = "purchase_order_cost", precision = 12, scale = 2)
    private BigDecimal purchaseOrderCost;

    @Column(name = "dispatch_order_number", length = 100)
    private String dispatchOrderNumber;

    @Column(name = "dispatch_order_date")
    private LocalDate dispatchOrderDate;

    @Column(name = "warranty_period")
    private Integer warrantyPeriod;

    @Column(name = "warranty_expiry_date")
    private LocalDate warrantyExpiryDate;

    @Column(name = "end_of_life_date")
    private LocalDate endOfLifeDate;

    @Column(name = "end_of_support_date")
    private LocalDate endOfSupportDate;
}
