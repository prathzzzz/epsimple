package com.eps.module.warehouse;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.location.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 255, message = "Warehouse name cannot exceed 255 characters")
    @Column(name = "warehouse_name", nullable = false, length = 255)
    private String warehouseName;

    @Size(max = 50, message = "Warehouse code cannot exceed 50 characters")
    @Column(name = "warehouse_code", unique = true, length = 50)
    private String warehouseCode;

    @Size(max = 100, message = "Warehouse type cannot exceed 100 characters")
    @Column(name = "warehouse_type", length = 100)
    private String warehouseType;

    @NotNull(message = "Location is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
}
