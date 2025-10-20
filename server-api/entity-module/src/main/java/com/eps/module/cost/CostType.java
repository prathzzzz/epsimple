package com.eps.module.cost;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cost_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cost_category_id", nullable = false)
    private CostCategory costCategory;

    @Column(name = "type_name", nullable = false, length = 100)
    private String typeName;

    @Column(name = "type_description", columnDefinition = "TEXT")
    private String typeDescription;
}
