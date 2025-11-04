package com.eps.module.api.epsone.site_type.repository;

import com.eps.module.site.SiteType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteTypeRepository extends JpaRepository<SiteType, Long> {

    @Query("SELECT st FROM SiteType st WHERE " +
            "LOWER(st.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(st.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<SiteType> searchSiteTypes(@Param("searchTerm") String searchTerm, Pageable pageable);
}
