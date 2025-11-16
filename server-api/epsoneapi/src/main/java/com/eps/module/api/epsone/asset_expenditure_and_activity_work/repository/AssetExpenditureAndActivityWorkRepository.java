package com.eps.module.api.epsone.asset_expenditure_and_activity_work.repository;

import com.eps.module.asset.AssetExpenditureAndActivityWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetExpenditureAndActivityWorkRepository extends JpaRepository<AssetExpenditureAndActivityWork, Long> {

    /**
     * Find all expenditures for a specific asset with pagination
     */
    @Query("SELECT aeaw FROM AssetExpenditureAndActivityWork aeaw " +
           "LEFT JOIN FETCH aeaw.asset a " +
           "LEFT JOIN FETCH aeaw.activityWork aw " +
           "LEFT JOIN FETCH aw.activities act " +
           "LEFT JOIN FETCH aeaw.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "WHERE aeaw.asset.id = :assetId")
    Page<AssetExpenditureAndActivityWork> findByAssetId(@Param("assetId") Long assetId, Pageable pageable);

    /**
     * Find all expenditures for a specific activity work with pagination
     */
    @Query("SELECT aeaw FROM AssetExpenditureAndActivityWork aeaw " +
           "LEFT JOIN FETCH aeaw.asset a " +
           "LEFT JOIN FETCH aeaw.activityWork aw " +
           "LEFT JOIN FETCH aw.activities act " +
           "LEFT JOIN FETCH aeaw.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "WHERE aeaw.activityWork.id = :activityWorkId")
    Page<AssetExpenditureAndActivityWork> findByActivityWorkId(@Param("activityWorkId") Long activityWorkId, Pageable pageable);

    /**
     * Find all expenditures with full details and pagination
     */
    @Query("SELECT aeaw FROM AssetExpenditureAndActivityWork aeaw " +
           "LEFT JOIN FETCH aeaw.asset a " +
           "LEFT JOIN FETCH aeaw.activityWork aw " +
           "LEFT JOIN FETCH aw.activities act " +
           "LEFT JOIN FETCH aeaw.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci")
    Page<AssetExpenditureAndActivityWork> findAllWithDetails(Pageable pageable);

    /**
     * Find by ID with all details fetched
     */
    @Query("SELECT aeaw FROM AssetExpenditureAndActivityWork aeaw " +
           "LEFT JOIN FETCH aeaw.asset a " +
           "LEFT JOIN FETCH aeaw.activityWork aw " +
           "LEFT JOIN FETCH aw.activities act " +
           "LEFT JOIN FETCH aeaw.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "WHERE aeaw.id = :id")
    Optional<AssetExpenditureAndActivityWork> findByIdWithDetails(@Param("id") Long id);

    /**
     * Check if a specific combination already exists
     */
    boolean existsByAssetIdAndExpendituresInvoiceIdAndActivityWorkId(
        Long assetId, 
        Long expendituresInvoiceId, 
        Long activityWorkId
    );

    /**
     * Count expenditures for an asset
     */
    long countByAssetId(Long assetId);

    /**
     * Count expenditures for an activity work
     */
    long countByActivityWorkId(Long activityWorkId);

    /**
     * Search expenditures by asset tag, activity name, or invoice number
     */
    @Query("SELECT aeaw FROM AssetExpenditureAndActivityWork aeaw " +
           "LEFT JOIN FETCH aeaw.asset a " +
           "LEFT JOIN FETCH aeaw.activityWork aw " +
           "LEFT JOIN FETCH aw.activities act " +
           "LEFT JOIN FETCH aeaw.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "WHERE LOWER(a.assetTagId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.assetName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(act.activityName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<AssetExpenditureAndActivityWork> searchExpenditures(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find all with details for bulk export
     */
    @Query("SELECT aeaw FROM AssetExpenditureAndActivityWork aeaw " +
           "LEFT JOIN FETCH aeaw.asset a " +
           "LEFT JOIN FETCH aeaw.activityWork aw " +
           "LEFT JOIN FETCH aw.activities act " +
           "LEFT JOIN FETCH aeaw.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "ORDER BY aeaw.id")
    List<AssetExpenditureAndActivityWork> findAllForExport();
}
