package com.eps.module.api.epsone.asset_placement.repository;

import com.eps.module.asset.AssetsOnWarehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetsOnWarehouseRepository extends JpaRepository<AssetsOnWarehouse, Long> {

    @Query("SELECT aow FROM AssetsOnWarehouse aow " +
            "LEFT JOIN FETCH aow.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aow.warehouse w " +
            "LEFT JOIN FETCH aow.assetStatus ast " +
            "LEFT JOIN FETCH aow.activityWork aw " +
            "LEFT JOIN FETCH aow.assetMovementTracker amt " +
            "WHERE aow.id = :id")
    Optional<AssetsOnWarehouse> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT aow FROM AssetsOnWarehouse aow " +
            "LEFT JOIN FETCH aow.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aow.warehouse w " +
            "LEFT JOIN FETCH aow.assetStatus ast " +
            "LEFT JOIN FETCH aow.activityWork aw " +
            "LEFT JOIN FETCH aow.assetMovementTracker amt " +
            "WHERE aow.vacatedOn IS NULL")
    Page<AssetsOnWarehouse> findAllWithDetails(Pageable pageable);

    @Query("SELECT aow FROM AssetsOnWarehouse aow " +
            "LEFT JOIN FETCH aow.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aow.warehouse w " +
            "LEFT JOIN FETCH aow.assetStatus ast " +
            "WHERE aow.warehouse.id = :warehouseId AND aow.vacatedOn IS NULL")
    Page<AssetsOnWarehouse> findByWarehouseIdWithDetails(@Param("warehouseId") Long warehouseId, Pageable pageable);

    @Query("SELECT aow FROM AssetsOnWarehouse aow " +
            "LEFT JOIN FETCH aow.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aow.warehouse w " +
            "LEFT JOIN FETCH aow.assetStatus ast " +
            "WHERE aow.asset.id = :assetId")
    Page<AssetsOnWarehouse> findByAssetIdWithDetails(@Param("assetId") Long assetId, Pageable pageable);

    @Query("SELECT aow FROM AssetsOnWarehouse aow " +
            "LEFT JOIN FETCH aow.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aow.warehouse w " +
            "LEFT JOIN FETCH aow.assetStatus ast " +
            "LEFT JOIN FETCH aow.activityWork aw " +
            "WHERE aow.vacatedOn IS NULL AND (" +
            "LOWER(a.assetTagId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.assetName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ast.statusName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<AssetsOnWarehouse> searchAssetsOnWarehouse(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Check if asset is already placed in a warehouse
    boolean existsByAssetIdAndWarehouseId(Long assetId, Long warehouseId);

    // Check if asset is placed in any warehouse
    boolean existsByAssetId(Long assetId);

    // Find asset placement by asset ID
    @Query("SELECT aow FROM AssetsOnWarehouse aow " +
            "LEFT JOIN FETCH aow.asset a " +
            "LEFT JOIN FETCH aow.warehouse w " +
            "WHERE aow.asset.id = :assetId")
    Optional<AssetsOnWarehouse> findByAssetId(@Param("assetId") Long assetId);

    // Find active placement (vacatedOn is null) by asset ID
    @Query("SELECT aow FROM AssetsOnWarehouse aow " +
            "LEFT JOIN FETCH aow.asset a " +
            "LEFT JOIN FETCH aow.warehouse w " +
            "LEFT JOIN FETCH aow.assetStatus ast " +
            "LEFT JOIN FETCH aow.activityWork aw " +
            "LEFT JOIN FETCH aow.assetMovementTracker amt " +
            "WHERE aow.asset.id = :assetId AND aow.vacatedOn IS NULL")
    Optional<AssetsOnWarehouse> findActiveByAssetId(@Param("assetId") Long assetId);

    // Find placement history (vacatedOn is not null) by asset ID
    @Query("SELECT aow FROM AssetsOnWarehouse aow " +
            "LEFT JOIN FETCH aow.asset a " +
            "LEFT JOIN FETCH aow.warehouse w " +
            "LEFT JOIN FETCH aow.assetStatus ast " +
            "WHERE aow.asset.id = :assetId AND aow.vacatedOn IS NOT NULL " +
            "ORDER BY aow.vacatedOn DESC")
    Page<AssetsOnWarehouse> findHistoryByAssetId(@Param("assetId") Long assetId, Pageable pageable);

    // Count assets in warehouse
    long countByWarehouseId(Long warehouseId);

    // Count assets
    long countByAssetId(Long assetId);
}
