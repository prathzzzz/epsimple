package com.eps.module.api.epsone.asset_category.repository;

import com.eps.module.asset.AssetCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long> {

    @Query("SELECT ac FROM AssetCategory ac LEFT JOIN FETCH ac.assetType " +
           "WHERE LOWER(ac.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(ac.categoryCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(ac.assetCodeAlt) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(ac.assetType.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<AssetCategory> searchAssetCategories(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT ac FROM AssetCategory ac LEFT JOIN FETCH ac.assetType")
    List<AssetCategory> findAllForExport();

    Page<AssetCategory> findByAssetTypeId(Long assetTypeId, Pageable pageable);

    boolean existsByCategoryNameIgnoreCase(String categoryName);

    boolean existsByCategoryNameAndIdNot(String categoryName, Long id);

    boolean existsByCategoryCodeIgnoreCase(String categoryCode);

    boolean existsByCategoryCodeAndIdNot(String categoryCode, Long id);

    boolean existsByAssetCodeAltIgnoreCase(String assetCodeAlt);

    boolean existsByAssetCodeAltAndIdNot(String assetCodeAlt, Long id);

    /**
     * Find asset category by name (case-insensitive) - for bulk upload
     */
    @Query("SELECT ac FROM AssetCategory ac LEFT JOIN FETCH ac.assetType WHERE LOWER(ac.categoryName) = LOWER(:categoryName)")
    java.util.Optional<AssetCategory> findByCategoryNameIgnoreCase(@Param("categoryName") String categoryName);
}
