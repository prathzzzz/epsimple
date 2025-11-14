package com.eps.module.api.epsone.asset.repository;

import com.eps.module.asset.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByAssetTagId(String assetTagId);

    Optional<Asset> findBySerialNumber(String serialNumber);

    @Query("SELECT a FROM Asset a " +
           "JOIN FETCH a.assetCategory ac " +
           "JOIN FETCH a.assetType at " +
           "JOIN FETCH a.vendor v " +
           "JOIN FETCH v.vendorDetails vd " +
           "JOIN FETCH a.lenderBank b " +
           "LEFT JOIN FETCH a.statusType st " +
           "WHERE LOWER(a.assetTagId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.serialNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.modelNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.assetName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(ac.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(at.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(vd.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(vd.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(vd.contactNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(b.bankName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Asset> search(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT a FROM Asset a " +
           "JOIN FETCH a.assetCategory " +
           "JOIN FETCH a.assetType " +
           "JOIN FETCH a.vendor v " +
           "JOIN FETCH v.vendorDetails " +
           "JOIN FETCH a.lenderBank " +
           "LEFT JOIN FETCH a.statusType")
    List<Asset> findAllWithDetails();

    @Query("SELECT a FROM Asset a " +
           "WHERE a.assetTagId LIKE CONCAT(:tagPrefix, '%') " +
           "ORDER BY a.assetTagId DESC")
    List<Asset> findByAssetTagIdStartingWith(@Param("tagPrefix") String tagPrefix);

    // Find asset tag IDs for a specific category, vendor, and bank combination
    @Query("SELECT a.assetTagId FROM Asset a " +
           "WHERE a.assetCategory.id = :categoryId " +
           "AND a.vendor.id = :vendorId " +
           "AND a.lenderBank.id = :bankId " +
           "ORDER BY a.assetTagId DESC")
    List<String> findAssetTagIdsByCategoryVendorBank(
        @Param("categoryId") Long categoryId,
        @Param("vendorId") Long vendorId,
        @Param("bankId") Long bankId
    );

    /**
     * Find all assets with all relationships eagerly fetched - for bulk upload export
     * IMPORTANT: Use LEFT JOIN FETCH for ALL relationships accessed in DTO mapping
     */
    @Query("SELECT a FROM Asset a " +
           "LEFT JOIN FETCH a.assetType at " +
           "LEFT JOIN FETCH a.assetCategory ac " +
           "LEFT JOIN FETCH a.vendor v " +
           "LEFT JOIN FETCH v.vendorDetails vd " +
           "LEFT JOIN FETCH a.lenderBank lb " +
           "LEFT JOIN FETCH a.statusType st " +
           "ORDER BY a.assetTagId ASC")
    List<Asset> findAllForExport();

    /**
     * Check if asset exists by asset tag ID (case-insensitive) - for duplicate checking
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Asset a WHERE LOWER(a.assetTagId) = LOWER(:assetTagId)")
    boolean existsByAssetTagIdIgnoreCase(@Param("assetTagId") String assetTagId);
    
    /**
     * Count assets by asset category ID
     */
    @Query("SELECT COUNT(a) FROM Asset a WHERE a.assetCategory.id = :categoryId")
    long countByAssetCategoryId(@Param("categoryId") Long categoryId);
}
