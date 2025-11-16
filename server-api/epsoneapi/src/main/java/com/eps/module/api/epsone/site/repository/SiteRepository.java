package com.eps.module.api.epsone.site.repository;

import com.eps.module.site.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    @Query("SELECT s FROM Site s " +
           "LEFT JOIN FETCH s.project p " +
           "LEFT JOIN FETCH p.bank " +
           "LEFT JOIN FETCH s.siteCategory " +
           "LEFT JOIN FETCH s.location l " +
           "LEFT JOIN FETCH l.city c " +
           "LEFT JOIN FETCH c.state " +
           "LEFT JOIN FETCH s.siteType " +
           "LEFT JOIN FETCH s.siteStatus " +
           "LEFT JOIN FETCH s.channelManagerContact " +
           "LEFT JOIN FETCH s.regionalManagerContact " +
           "LEFT JOIN FETCH s.stateHeadContact " +
           "LEFT JOIN FETCH s.bankPersonContact " +
           "LEFT JOIN FETCH s.masterFranchiseeContact " +
           "WHERE s.id = :id")
    Optional<Site> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT s FROM Site s " +
           "LEFT JOIN FETCH s.project p " +
           "LEFT JOIN FETCH p.bank " +
           "LEFT JOIN FETCH s.siteCategory " +
           "LEFT JOIN FETCH s.location l " +
           "LEFT JOIN FETCH l.city c " +
           "LEFT JOIN FETCH c.state " +
           "LEFT JOIN FETCH s.siteType " +
           "LEFT JOIN FETCH s.siteStatus")
    Page<Site> findAllWithDetails(Pageable pageable);

    @Query("SELECT s FROM Site s " +
           "LEFT JOIN FETCH s.project p " +
           "LEFT JOIN FETCH p.bank " +
           "LEFT JOIN FETCH s.siteCategory sc " +
           "LEFT JOIN FETCH s.location l " +
           "LEFT JOIN FETCH l.city c " +
           "LEFT JOIN FETCH c.state st " +
           "LEFT JOIN FETCH s.siteType st2 " +
           "LEFT JOIN FETCH s.siteStatus ss " +
           "WHERE LOWER(s.siteCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(s.oldSiteCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(s.projectPhase) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.projectName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(sc.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(l.locationName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(st.stateName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(st2.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Site> searchSites(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT s FROM Site s " +
           "LEFT JOIN FETCH s.project p " +
           "LEFT JOIN FETCH p.bank " +
           "LEFT JOIN FETCH s.siteCategory " +
           "LEFT JOIN FETCH s.location l " +
           "LEFT JOIN FETCH l.city c " +
           "LEFT JOIN FETCH c.state " +
           "LEFT JOIN FETCH s.siteType " +
           "LEFT JOIN FETCH s.siteStatus " +
           "LEFT JOIN FETCH s.channelManagerContact " +
           "LEFT JOIN FETCH s.regionalManagerContact " +
           "LEFT JOIN FETCH s.stateHeadContact " +
           "LEFT JOIN FETCH s.bankPersonContact " +
           "LEFT JOIN FETCH s.masterFranchiseeContact " +
           "ORDER BY s.siteCode ASC")
    List<Site> findAllSitesList();

    Optional<Site> findBySiteCode(String siteCode);
    
    Optional<Site> findBySiteCodeIgnoreCase(String siteCode);

    boolean existsBySiteCode(String siteCode);
    
    boolean existsBySiteCodeIgnoreCase(String siteCode);

    boolean existsBySiteCodeAndIdNot(String siteCode, Long id);

    // Find max sequence from site codes for a specific project and state
    @Query("SELECT s.siteCode FROM Site s " +
           "WHERE s.project.id = :projectId " +
           "AND s.location.city.state.id = :stateId " +
           "ORDER BY s.siteCode DESC")
    List<String> findSiteCodesByProjectAndState(@Param("projectId") Long projectId, @Param("stateId") Long stateId);

    // Dependency checks
    Page<Site> findByProjectId(Long projectId, Pageable pageable);

    Page<Site> findBySiteCategoryId(Long siteCategoryId, Pageable pageable);

    Page<Site> findByLocationId(Long locationId, Pageable pageable);

    Page<Site> findBySiteTypeId(Long siteTypeId, Pageable pageable);

    Page<Site> findBySiteStatusId(Long siteStatusId, Pageable pageable);

    // Count dependencies from other tables
    @Query("SELECT COUNT(aos) FROM AssetsOnSite aos WHERE aos.site.id = :siteId")
    long countAssetsOnSiteBySiteId(@Param("siteId") Long siteId);

    @Query("SELECT COUNT(sawe) FROM SiteActivityWorkExpenditure sawe WHERE sawe.site.id = :siteId")
    long countSiteActivityWorkExpenditureBySiteId(@Param("siteId") Long siteId);
}
