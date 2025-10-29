package com.eps.module.api.epsone.datacenter.repository;

import com.eps.module.warehouse.Datacenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatacenterRepository extends JpaRepository<Datacenter, Long> {

    @Query("SELECT d FROM Datacenter d " +
            "LEFT JOIN FETCH d.location l " +
            "LEFT JOIN FETCH l.city c " +
            "LEFT JOIN FETCH c.state s " +
            "WHERE d.id = :id")
    Optional<Datacenter> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT d FROM Datacenter d " +
            "LEFT JOIN FETCH d.location l " +
            "LEFT JOIN FETCH l.city c " +
            "LEFT JOIN FETCH c.state s")
    Page<Datacenter> findAllWithDetails(Pageable pageable);

    @Query("SELECT d FROM Datacenter d " +
            "LEFT JOIN FETCH d.location l " +
            "LEFT JOIN FETCH l.city c " +
            "LEFT JOIN FETCH c.state s " +
            "WHERE LOWER(d.datacenterName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(d.datacenterCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(d.datacenterType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(l.locationName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Datacenter> searchDatacenters(@Param("searchTerm") String searchTerm, Pageable pageable);

    Optional<Datacenter> findByDatacenterCode(String datacenterCode);

    boolean existsByDatacenterCode(String datacenterCode);

    boolean existsByDatacenterCodeAndIdNot(String datacenterCode, Long id);

    long countByLocationId(Long locationId);

    @Query("SELECT d FROM Datacenter d WHERE d.location.id = :locationId")
    Page<Datacenter> findByLocationId(@Param("locationId") Long locationId, Pageable pageable);
}
