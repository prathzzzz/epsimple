package com.eps.module.api.epsone.vendor.repository;

import com.eps.module.vendor.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    /**
     * Search vendors by code, person details
     */
    @Query("SELECT v FROM Vendor v " +
            "LEFT JOIN FETCH v.vendorType vt " +
            "LEFT JOIN FETCH v.vendorDetails vd " +
            "WHERE LOWER(v.vendorCodeAlt) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vd.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vd.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vd.contactNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vt.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Vendor> searchVendors(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Get all vendors with eager loading
     */
    @Query("SELECT v FROM Vendor v " +
            "LEFT JOIN FETCH v.vendorType vt " +
            "LEFT JOIN FETCH v.vendorDetails vd")
    List<Vendor> findAllList();

    /**
     * Find by vendor type
     */
    @Query("SELECT v FROM Vendor v " +
            "LEFT JOIN FETCH v.vendorType vt " +
            "LEFT JOIN FETCH v.vendorDetails vd " +
            "WHERE v.vendorType.id = :vendorTypeId")
    Page<Vendor> findByVendorTypeId(@Param("vendorTypeId") Long vendorTypeId, Pageable pageable);

    /**
     * Count vendors by vendor type
     */
    long countByVendorTypeId(Long vendorTypeId);

    /**
     * Find by vendor details (PersonDetails)
     */
    Optional<Vendor> findByVendorDetailsId(Long vendorDetailsId);

    /**
     * Count vendors by vendor details
     */
    long countByVendorDetailsId(Long vendorDetailsId);

    /**
     * Check if vendor code exists
     */
    boolean existsByVendorCodeAlt(String vendorCodeAlt);

    /**
     * Check if vendor code exists excluding current vendor
     */
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vendor v " +
            "WHERE v.vendorCodeAlt = :vendorCodeAlt AND v.id <> :id")
    boolean existsByVendorCodeAltAndIdNot(@Param("vendorCodeAlt") String vendorCodeAlt, @Param("id") Long id);

    /**
     * Get all vendors for export with eager loading to avoid LazyInitializationException
     */
    @Query("SELECT v FROM Vendor v " +
            "LEFT JOIN FETCH v.vendorType vt " +
            "LEFT JOIN FETCH v.vendorDetails vd " +
            "LEFT JOIN FETCH vd.personType pt")
    List<Vendor> findAllForExport();
}
