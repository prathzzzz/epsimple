package com.eps.module.api.epsone.activityworkremarks.repository;

import com.eps.module.activity.ActivityWorkRemarks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityWorkRemarksRepository extends JpaRepository<ActivityWorkRemarks, Long> {

    @Query("SELECT r FROM ActivityWorkRemarks r " +
           "WHERE r.activityWork.id = :activityWorkId " +
           "ORDER BY r.commentedOn DESC")
    Page<ActivityWorkRemarks> findByActivityWorkId(@Param("activityWorkId") Long activityWorkId, Pageable pageable);

    @Query("SELECT r FROM ActivityWorkRemarks r " +
           "WHERE r.activityWork.id = :activityWorkId " +
           "ORDER BY r.commentedOn DESC")
    List<ActivityWorkRemarks> findAllByActivityWorkId(@Param("activityWorkId") Long activityWorkId);

    @Query("SELECT COUNT(r) FROM ActivityWorkRemarks r WHERE r.activityWork.id = :activityWorkId")
    long countByActivityWorkId(@Param("activityWorkId") Long activityWorkId);
}
