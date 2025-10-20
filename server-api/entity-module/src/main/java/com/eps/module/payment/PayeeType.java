package com.eps.module.payment;

import com.eps.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payee_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayeeType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payee_type", nullable = false, unique = true, length = 50)
    private String payeeType;

    @Column(name = "payee_category", length = 100)
    private String payeeCategory;

    @Column(columnDefinition = "TEXT")
    private String description;
}
