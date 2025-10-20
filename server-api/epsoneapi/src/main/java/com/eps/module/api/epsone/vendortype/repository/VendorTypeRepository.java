package com.eps.module.api.epsone.vendortype.repository;

import com.eps.module.vendor.VendorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorTypeRepository extends JpaRepository<VendorType, Long> {
    
    @Query("SELECT vt FROM VendorType vt WHERE " +
           "LOWER(vt.typeName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(vt.vendorCategory.categoryName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<VendorType> searchVendorTypes(@Param("search") String search, Pageable pageable);
    
    boolean existsByTypeNameIgnoreCase(String typeName);
    
    Optional<VendorType> findByTypeNameIgnoreCase(String typeName);
    
    @Query("SELECT CASE WHEN COUNT(vt) > 0 THEN true ELSE false END FROM VendorType vt WHERE " +
           "LOWER(vt.typeName) = LOWER(:typeName) AND vt.id <> :id")
    boolean existsByTypeNameAndIdNot(@Param("typeName") String typeName, @Param("id") Long id);
    
    @Query("SELECT vt FROM VendorType vt WHERE vt.vendorCategory.id = :categoryId")
    java.util.List<VendorType> findByVendorCategoryId(@Param("categoryId") Long categoryId);
}
