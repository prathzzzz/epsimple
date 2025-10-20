package com.eps.module.asset;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asset_movement_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetMovementType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movement_type", nullable = false, unique = true, length = 100)
    private String movementType;

    @Column(columnDefinition = "TEXT")
    private String description;
}
