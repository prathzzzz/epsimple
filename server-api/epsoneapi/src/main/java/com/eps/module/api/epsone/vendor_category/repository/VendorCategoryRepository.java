package com.eps.module.api.epsone.vendor_category.repository;

import com.eps.module.vendor.VendorCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorCategoryRepository extends JpaRepository<VendorCategory, Long> {
    
    @Query("SELECT vc FROM VendorCategory vc WHERE " +
           "LOWER(vc.categoryName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<VendorCategory> searchVendorCategories(@Param("search") String search, Pageable pageable);
    
    boolean existsByCategoryNameIgnoreCase(String categoryName);
    
    Optional<VendorCategory> findByCategoryNameIgnoreCase(String categoryName);
    
    @Query("SELECT CASE WHEN COUNT(vc) > 0 THEN true ELSE false END FROM VendorCategory vc WHERE " +
           "LOWER(vc.categoryName) = LOWER(:categoryName) AND vc.id <> :id")
    boolean existsByCategoryNameAndIdNot(@Param("categoryName") String categoryName, @Param("id") Long id);
}
