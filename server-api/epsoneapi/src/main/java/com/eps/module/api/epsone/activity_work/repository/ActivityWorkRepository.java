package com.eps.module.api.epsone.activity_work.repository;

import com.eps.module.activity.ActivityWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityWorkRepository extends JpaRepository<ActivityWork, Long> {

    @Query("SELECT aw FROM ActivityWork aw " +
            "LEFT JOIN FETCH aw.activities a " +
            "LEFT JOIN FETCH aw.vendor v " +
            "LEFT JOIN FETCH v.vendorDetails " +
            "LEFT JOIN FETCH aw.statusType st " +
            "WHERE aw.id = :id")
    Optional<ActivityWork> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT aw FROM ActivityWork aw " +
            "LEFT JOIN FETCH aw.activities a " +
            "LEFT JOIN FETCH aw.vendor v " +
            "LEFT JOIN FETCH v.vendorDetails " +
            "LEFT JOIN FETCH aw.statusType st")
    Page<ActivityWork> findAllWithDetails(Pageable pageable);

    @Query("SELECT aw FROM ActivityWork aw " +
            "LEFT JOIN FETCH aw.activities a " +
            "LEFT JOIN FETCH aw.vendor v " +
            "LEFT JOIN FETCH v.vendorDetails vd " +
            "LEFT JOIN FETCH aw.statusType st " +
            "WHERE LOWER(a.activityName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vd.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vd.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(aw.vendorOrderNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(st.statusName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ActivityWork> searchActivityWorks(@Param("searchTerm") String searchTerm, Pageable pageable);

    long countByActivitiesId(Long activitiesId);

    long countByVendorId(Long vendorId);

    long countByStatusTypeId(Long statusTypeId);

    @Query("SELECT aw FROM ActivityWork aw WHERE aw.activities.id = :activitiesId")
    Page<ActivityWork> findByActivitiesId(@Param("activitiesId") Long activitiesId, Pageable pageable);

    @Query("SELECT aw FROM ActivityWork aw WHERE aw.vendor.id = :vendorId")
    Page<ActivityWork> findByVendorId(@Param("vendorId") Long vendorId, Pageable pageable);

    @Query("SELECT aw FROM ActivityWork aw WHERE aw.statusType.id = :statusTypeId")
    Page<ActivityWork> findByStatusTypeId(@Param("statusTypeId") Long statusTypeId, Pageable pageable);
}
