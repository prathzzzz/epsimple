package com.eps.module.api.epsone.assetplacement.repository;

import com.eps.module.asset.AssetsOnSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetsOnSiteRepository extends JpaRepository<AssetsOnSite, Long> {

    @Query("SELECT aos FROM AssetsOnSite aos " +
            "LEFT JOIN FETCH aos.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aos.site s " +
            "LEFT JOIN FETCH s.location l " +
            "LEFT JOIN FETCH aos.assetStatus ast " +
            "LEFT JOIN FETCH aos.activityWork aw " +
            "LEFT JOIN FETCH aos.assetMovementTracker amt " +
            "WHERE aos.id = :id")
    Optional<AssetsOnSite> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT aos FROM AssetsOnSite aos " +
            "LEFT JOIN FETCH aos.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aos.site s " +
            "LEFT JOIN FETCH s.location l " +
            "LEFT JOIN FETCH aos.assetStatus ast " +
            "LEFT JOIN FETCH aos.activityWork aw " +
            "LEFT JOIN FETCH aos.assetMovementTracker amt")
    Page<AssetsOnSite> findAllWithDetails(Pageable pageable);

    @Query("SELECT aos FROM AssetsOnSite aos " +
            "LEFT JOIN FETCH aos.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aos.site s " +
            "LEFT JOIN FETCH s.location l " +
            "LEFT JOIN FETCH aos.assetStatus ast " +
            "WHERE aos.site.id = :siteId")
    Page<AssetsOnSite> findBySiteIdWithDetails(@Param("siteId") Long siteId, Pageable pageable);

    @Query("SELECT aos FROM AssetsOnSite aos " +
            "LEFT JOIN FETCH aos.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aos.site s " +
            "LEFT JOIN FETCH s.location l " +
            "LEFT JOIN FETCH aos.assetStatus ast " +
            "WHERE aos.asset.id = :assetId")
    Page<AssetsOnSite> findByAssetIdWithDetails(@Param("assetId") Long assetId, Pageable pageable);

    @Query("SELECT aos FROM AssetsOnSite aos " +
            "LEFT JOIN FETCH aos.asset a " +
            "LEFT JOIN FETCH a.assetType at " +
            "LEFT JOIN FETCH a.assetCategory ac " +
            "LEFT JOIN FETCH aos.site s " +
            "LEFT JOIN FETCH s.location l " +
            "LEFT JOIN FETCH aos.assetStatus ast " +
            "LEFT JOIN FETCH aos.activityWork aw " +
            "WHERE LOWER(a.assetTagId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(a.assetName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(s.siteCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ast.statusName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<AssetsOnSite> searchAssetsOnSite(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Check if asset is already placed on a site
    boolean existsByAssetIdAndSiteId(Long assetId, Long siteId);

    // Check if asset is placed on any site
    boolean existsByAssetId(Long assetId);

    // Find asset placement by asset ID
    @Query("SELECT aos FROM AssetsOnSite aos " +
            "LEFT JOIN FETCH aos.asset a " +
            "LEFT JOIN FETCH aos.site s " +
            "WHERE aos.asset.id = :assetId")
    Optional<AssetsOnSite> findByAssetId(@Param("assetId") Long assetId);

    // Count assets on site
    long countBySiteId(Long siteId);

    // Count assets
    long countByAssetId(Long assetId);
}
