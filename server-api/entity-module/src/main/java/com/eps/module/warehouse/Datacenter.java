package com.eps.module.warehouse;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.location.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Datacenter name is required")
    @Size(max = 255, message = "Datacenter name cannot exceed 255 characters")
    @Column(name = "datacenter_name", nullable = false, length = 255)
    private String datacenterName;

    @Size(max = 50, message = "Datacenter code cannot exceed 50 characters")
    @Column(name = "datacenter_code", unique = true, length = 50)
    private String datacenterCode;

    @Size(max = 100, message = "Datacenter type cannot exceed 100 characters")
    @Column(name = "datacenter_type", length = 100)
    private String datacenterType;

    @NotNull(message = "Location is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
}
