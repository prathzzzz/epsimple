package com.eps.module.asset;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.bank.Bank;
import com.eps.module.vendor.Vendor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "asset_tag_code_generator",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"asset_category_id", "vendor_id", "bank_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetTagCodeGenerator extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_category_id", nullable = false)
    private AssetCategory assetCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(name = "max_seq_digit")
    @Builder.Default
    private Integer maxSeqDigit = 5;

    @Column(name = "running_seq")
    @Builder.Default
    private Integer runningSeq = 1;
}
