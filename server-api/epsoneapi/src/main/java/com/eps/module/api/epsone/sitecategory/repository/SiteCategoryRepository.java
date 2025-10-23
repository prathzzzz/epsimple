package com.eps.module.api.epsone.sitecategory.repository;

import com.eps.module.site.SiteCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteCategoryRepository extends JpaRepository<SiteCategory, Long> {

    @Query("SELECT sc FROM SiteCategory sc WHERE " +
            "LOWER(sc.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(sc.categoryCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(sc.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<SiteCategory> searchSiteCategories(@Param("searchTerm") String searchTerm, Pageable pageable);
}
