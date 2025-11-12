package com.eps.module.vendor;

import com.eps.module.common.entity.BaseEntity;
import com.eps.module.person.PersonDetails;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vendor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_type_id", nullable = false)
    private VendorType vendorType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_details_id", nullable = false, unique = true)
    private PersonDetails vendorDetails;

    @Column(name = "vendor_code_alt", nullable = false, unique = true, length = 10)
    private String vendorCodeAlt;
}
