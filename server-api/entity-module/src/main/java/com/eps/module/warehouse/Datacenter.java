package com.eps.module.warehouse;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.location.Location;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "datacenter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Datacenter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datacenter_name", nullable = false, length = 255)
    private String datacenterName;

    @Column(name = "datacenter_code", unique = true, length = 50)
    private String datacenterCode;

    @Column(name = "datacenter_type", length = 100)
    private String datacenterType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
}
