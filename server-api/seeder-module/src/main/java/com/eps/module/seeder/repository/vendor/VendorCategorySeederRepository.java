package com.eps.module.seeder.repository.vendor;

import com.eps.module.vendor.VendorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorCategorySeederRepository extends JpaRepository<VendorCategory, Long> {
}
