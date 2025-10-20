package com.eps.module.vendor;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.person.PersonDetails;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "landlord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Landlord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "landlord_details_id", nullable = false, unique = true)
        private PersonDetails landlordDetails;

    @Column(name = "rent_share_percentage", precision = 5, scale = 2)
    private BigDecimal rentSharePercentage;
}
