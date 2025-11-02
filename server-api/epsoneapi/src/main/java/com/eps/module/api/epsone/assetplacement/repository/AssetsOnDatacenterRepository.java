package com.eps.module.api.epsone.assetplacement.repository;

import com.eps.module.asset.AssetsOnDatacenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetsOnDatacenterRepository extends JpaRepository<AssetsOnDatacenter, Long> {

    @Query("SELECT aod FROM AssetsOnDatacenter aod " +
            "LEFT JOIN FETCH aod.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aod.datacenter d " +
            "LEFT JOIN FETCH aod.assetStatus ast " +
            "LEFT JOIN FETCH aod.activityWork aw " +
            "LEFT JOIN FETCH aod.assetMovementTracker amt " +
            "WHERE aod.id = :id")
    Optional<AssetsOnDatacenter> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT aod FROM AssetsOnDatacenter aod " +
            "LEFT JOIN FETCH aod.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aod.datacenter d " +
            "LEFT JOIN FETCH aod.assetStatus ast " +
            "LEFT JOIN FETCH aod.activityWork aw " +
            "LEFT JOIN FETCH aod.assetMovementTracker amt")
    Page<AssetsOnDatacenter> findAllWithDetails(Pageable pageable);

    @Query("SELECT aod FROM AssetsOnDatacenter aod " +
            "LEFT JOIN FETCH aod.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aod.datacenter d " +
            "LEFT JOIN FETCH aod.assetStatus ast " +
            "WHERE aod.datacenter.id = :datacenterId")
    Page<AssetsOnDatacenter> findByDatacenterIdWithDetails(@Param("datacenterId") Long datacenterId, Pageable pageable);

    @Query("SELECT aod FROM AssetsOnDatacenter aod " +
            "LEFT JOIN FETCH aod.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aod.datacenter d " +
            "LEFT JOIN FETCH aod.assetStatus ast " +
            "WHERE aod.asset.id = :assetId")
    Page<AssetsOnDatacenter> findByAssetIdWithDetails(@Param("assetId") Long assetId, Pageable pageable);

    @Query("SELECT aod FROM AssetsOnDatacenter aod " +
            "LEFT JOIN FETCH aod.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aod.datacenter d " +
            "LEFT JOIN FETCH aod.assetStatus ast " +
            "LEFT JOIN FETCH aod.activityWork aw " +
            "WHERE LOWER(a.assetTagId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.assetName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(d.datacenterName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(d.datacenterCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ast.statusName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<AssetsOnDatacenter> searchAssetsOnDatacenter(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Check if asset is already placed in a datacenter
    boolean existsByAssetIdAndDatacenterId(Long assetId, Long datacenterId);

    // Check if asset is placed in any datacenter
    boolean existsByAssetId(Long assetId);

    // Find asset placement by asset ID
    @Query("SELECT aod FROM AssetsOnDatacenter aod " +
            "LEFT JOIN FETCH aod.asset a " +
            "LEFT JOIN FETCH aod.datacenter d " +
            "WHERE aod.asset.id = :assetId")
    Optional<AssetsOnDatacenter> findByAssetId(@Param("assetId") Long assetId);

    // Count assets in datacenter
    long countByDatacenterId(Long datacenterId);

    // Count assets
    long countByAssetId(Long assetId);
}
