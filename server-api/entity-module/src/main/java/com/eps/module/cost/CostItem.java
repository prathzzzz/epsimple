package com.eps.module.cost;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cost_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cost_type_id", nullable = false)
    private CostType costType;

    @Column(name = "cost_item_for", nullable = false, length = 255)
    private String costItemFor;

    @Column(name = "item_description", columnDefinition = "TEXT")
    private String itemDescription;
}
