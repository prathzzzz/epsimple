package com.eps.module.api.epsone.asset_type.repository;

import com.eps.module.asset.AssetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {
    
    @Query("SELECT at FROM AssetType at WHERE " +
           "LOWER(at.typeName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(at.typeCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(at.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<AssetType> searchAssetTypes(@Param("search") String search, Pageable pageable);
    
    boolean existsByTypeNameIgnoreCase(String typeName);
    
    boolean existsByTypeCodeIgnoreCase(String typeCode);
    
    @Query("SELECT CASE WHEN COUNT(at) > 0 THEN true ELSE false END FROM AssetType at WHERE " +
           "LOWER(at.typeName) = LOWER(:typeName) AND at.id <> :id")
    boolean existsByTypeNameAndIdNot(@Param("typeName") String typeName, @Param("id") Long id);
    
    @Query("SELECT CASE WHEN COUNT(at) > 0 THEN true ELSE false END FROM AssetType at WHERE " +
           "LOWER(at.typeCode) = LOWER(:typeCode) AND at.id <> :id")
    boolean existsByTypeCodeAndIdNot(@Param("typeCode") String typeCode, @Param("id") Long id);

    // Bulk Upload Methods
    @Query("SELECT at FROM AssetType at ORDER BY at.typeName ASC")
    List<AssetType> findAllForExport();
}
