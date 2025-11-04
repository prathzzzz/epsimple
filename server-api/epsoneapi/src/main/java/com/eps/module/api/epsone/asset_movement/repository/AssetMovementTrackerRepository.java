package com.eps.module.api.epsone.asset_movement.repository;

import com.eps.module.asset.AssetMovementTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetMovementTrackerRepository extends JpaRepository<AssetMovementTracker, Long> {

    // Find all movement records for a specific asset with pagination
    @Query(value = "SELECT amt FROM AssetMovementTracker amt " +
            "WHERE amt.asset.id = :assetId " +
            "ORDER BY amt.createdAt DESC",
           countQuery = "SELECT COUNT(amt) FROM AssetMovementTracker amt " +
            "WHERE amt.asset.id = :assetId")
    Page<AssetMovementTracker> findByAssetIdWithDetails(@Param("assetId") Long assetId, Pageable pageable);

    // Find movement records by IDs with all relationships loaded
    @Query("SELECT DISTINCT amt FROM AssetMovementTracker amt " +
            "LEFT JOIN FETCH amt.asset a " +
            "LEFT JOIN FETCH amt.assetMovementType mt " +
            "LEFT JOIN FETCH amt.fromSite fs " +
            "LEFT JOIN FETCH amt.toSite ts " +
            "LEFT JOIN FETCH amt.fromWarehouse fw " +
            "LEFT JOIN FETCH amt.toWarehouse tw " +
            "LEFT JOIN FETCH amt.fromDatacenter fd " +
            "LEFT JOIN FETCH amt.toDatacenter td " +
            "WHERE amt.id IN :ids")
    List<AssetMovementTracker> findByIdsWithDetails(@Param("ids") List<Long> ids);

    // Find all movement records for a specific asset (list version)
    @Query("SELECT amt FROM AssetMovementTracker amt " +
            "LEFT JOIN FETCH amt.asset a " +
            "LEFT JOIN FETCH amt.assetMovementType mt " +
            "LEFT JOIN FETCH amt.fromSite fs " +
            "LEFT JOIN FETCH amt.toSite ts " +
            "LEFT JOIN FETCH amt.fromWarehouse fw " +
            "LEFT JOIN FETCH amt.toWarehouse tw " +
            "LEFT JOIN FETCH amt.fromDatacenter fd " +
            "LEFT JOIN FETCH amt.toDatacenter td " +
            "WHERE amt.asset.id = :assetId " +
            "ORDER BY amt.createdAt DESC")
    List<AssetMovementTracker> findByAssetId(@Param("assetId") Long assetId);

    // Count movements for an asset
    long countByAssetId(Long assetId);
}
