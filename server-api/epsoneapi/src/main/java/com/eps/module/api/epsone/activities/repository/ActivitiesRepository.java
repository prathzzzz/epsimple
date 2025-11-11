package com.eps.module.api.epsone.activities.repository;

import com.eps.module.activity.Activities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivitiesRepository extends JpaRepository<Activities, Long> {

    
    List<Activities> findByActivityId(Long activity_id);

    @Query("SELECT a FROM Activities a WHERE " +
            "LOWER(a.activityName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.activityCategory) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.activityDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Activities> searchActivities(@Param("searchTerm") String searchTerm, Pageable pageable);

    boolean existsByActivityNameIgnoreCase(String activityName);

    Optional<Activities> findByActivityNameIgnoreCase(String activityName);

    boolean existsByActivityNameAndIdNot(String activityName, Long id);

    @Query("SELECT a FROM Activities a LEFT JOIN FETCH a.activity ORDER BY a.id")
    List<Activities> findAllForExport();
}
