package com.eps.module.warehouse;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.location.Location;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warehouse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_name", nullable = false, length = 255)
    private String warehouseName;

    @Column(name = "warehouse_code", unique = true, length = 50)
    private String warehouseCode;

    @Column(name = "warehouse_type", length = 100)
    private String warehouseType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
}
