package com.eps.module.seeder.repository.vendor;

import com.eps.module.vendor.VendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorTypeSeederRepository extends JpaRepository<VendorType, Long> {
}
