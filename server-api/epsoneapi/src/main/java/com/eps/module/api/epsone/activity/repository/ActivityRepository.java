package com.eps.module.api.epsone.activity.repository;

import com.eps.module.activity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("SELECT a FROM Activity a WHERE " +
            "LOWER(a.activityName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.activityDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Activity> searchActivities(@Param("searchTerm") String searchTerm, Pageable pageable);

    boolean existsByActivityNameIgnoreCase(String activityName);

    Optional<Activity> findByActivityNameIgnoreCase(String activityName);

    boolean existsByActivityNameAndIdNot(String activityName, Long id);

    @Query("SELECT a FROM Activity a ORDER BY a.id")
    List<Activity> findAllForExport();
}
