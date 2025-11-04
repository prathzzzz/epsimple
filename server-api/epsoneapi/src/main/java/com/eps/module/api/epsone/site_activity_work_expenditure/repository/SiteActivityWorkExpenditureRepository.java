package com.eps.module.api.epsone.site_activity_work_expenditure.repository;

import com.eps.module.site.SiteActivityWorkExpenditure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteActivityWorkExpenditureRepository extends JpaRepository<SiteActivityWorkExpenditure, Long> {

    /**
     * Find all expenditures for a specific site with pagination
     */
    @Query("SELECT sawe FROM SiteActivityWorkExpenditure sawe " +
           "LEFT JOIN FETCH sawe.site s " +
           "LEFT JOIN FETCH sawe.activityWork aw " +
           "LEFT JOIN FETCH aw.activities a " +
           "LEFT JOIN FETCH sawe.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "WHERE sawe.site.id = :siteId")
    Page<SiteActivityWorkExpenditure> findBySiteId(@Param("siteId") Long siteId, Pageable pageable);

    /**
     * Find all expenditures for a specific activity work with pagination
     */
    @Query("SELECT sawe FROM SiteActivityWorkExpenditure sawe " +
           "LEFT JOIN FETCH sawe.site s " +
           "LEFT JOIN FETCH sawe.activityWork aw " +
           "LEFT JOIN FETCH aw.activities a " +
           "LEFT JOIN FETCH sawe.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "WHERE sawe.activityWork.id = :activityWorkId")
    Page<SiteActivityWorkExpenditure> findByActivityWorkId(@Param("activityWorkId") Long activityWorkId, Pageable pageable);

    /**
     * Find all expenditures with full details and pagination
     */
    @Query("SELECT sawe FROM SiteActivityWorkExpenditure sawe " +
           "LEFT JOIN FETCH sawe.site s " +
           "LEFT JOIN FETCH sawe.activityWork aw " +
           "LEFT JOIN FETCH aw.activities a " +
           "LEFT JOIN FETCH sawe.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci")
    Page<SiteActivityWorkExpenditure> findAllWithDetails(Pageable pageable);

    /**
     * Find by ID with all details fetched
     */
    @Query("SELECT sawe FROM SiteActivityWorkExpenditure sawe " +
           "LEFT JOIN FETCH sawe.site s " +
           "LEFT JOIN FETCH sawe.activityWork aw " +
           "LEFT JOIN FETCH aw.activities a " +
           "LEFT JOIN FETCH sawe.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "WHERE sawe.id = :id")
    Optional<SiteActivityWorkExpenditure> findByIdWithDetails(@Param("id") Long id);

    /**
     * Check if a specific combination already exists
     */
    boolean existsBySiteIdAndActivityWorkIdAndExpendituresInvoiceId(
        Long siteId, 
        Long activityWorkId, 
        Long expendituresInvoiceId
    );

    /**
     * Count expenditures for a site
     */
    long countBySiteId(Long siteId);

    /**
     * Count expenditures for an activity work
     */
    long countByActivityWorkId(Long activityWorkId);

    /**
     * Search expenditures by site code or activity name
     */
    @Query("SELECT sawe FROM SiteActivityWorkExpenditure sawe " +
           "LEFT JOIN FETCH sawe.site s " +
           "LEFT JOIN FETCH sawe.activityWork aw " +
           "LEFT JOIN FETCH aw.activities a " +
           "LEFT JOIN FETCH sawe.expendituresInvoice ei " +
           "LEFT JOIN FETCH ei.invoice i " +
           "LEFT JOIN FETCH ei.costItem ci " +
           "WHERE LOWER(s.siteCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.activityName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<SiteActivityWorkExpenditure> searchExpenditures(@Param("searchTerm") String searchTerm, Pageable pageable);
}
