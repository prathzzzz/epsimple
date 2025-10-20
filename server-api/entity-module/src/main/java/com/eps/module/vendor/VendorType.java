package com.eps.module.vendor;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vendor_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", nullable = false, unique = true, length = 100)
    private String typeName;

    @Column(name = "vendor_category", length = 100)
    private String vendorCategory;

    @Column(columnDefinition = "TEXT")
    private String description;
}
