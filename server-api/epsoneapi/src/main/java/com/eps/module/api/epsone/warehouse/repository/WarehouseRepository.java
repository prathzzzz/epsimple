package com.eps.module.api.epsone.warehouse.repository;

import com.eps.module.warehouse.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT w FROM Warehouse w " +
            "LEFT JOIN FETCH w.location l " +
            "LEFT JOIN FETCH l.city c " +
            "LEFT JOIN FETCH c.state s " +
            "WHERE w.id = :id")
    Optional<Warehouse> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT w FROM Warehouse w " +
            "LEFT JOIN FETCH w.location l " +
            "LEFT JOIN FETCH l.city c " +
            "LEFT JOIN FETCH c.state s")
    Page<Warehouse> findAllWithDetails(Pageable pageable);

    @Query("SELECT w FROM Warehouse w " +
            "LEFT JOIN FETCH w.location l " +
            "LEFT JOIN FETCH l.city c " +
            "LEFT JOIN FETCH c.state s " +
            "WHERE LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(w.warehouseType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(l.locationName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Warehouse> searchWarehouses(@Param("searchTerm") String searchTerm, Pageable pageable);

    Optional<Warehouse> findByWarehouseCode(String warehouseCode);

    boolean existsByWarehouseCode(String warehouseCode);

    boolean existsByWarehouseCodeAndIdNot(String warehouseCode, Long id);

    long countByLocationId(Long locationId);

    @Query("SELECT w FROM Warehouse w WHERE w.location.id = :locationId")
    Page<Warehouse> findByLocationId(@Param("locationId") Long locationId, Pageable pageable);

    /**
     * Find warehouse by name (case-insensitive) - for duplicate checking
     */
    @Query("SELECT w FROM Warehouse w WHERE LOWER(w.warehouseName) = LOWER(:warehouseName)")
    Optional<Warehouse> findByWarehouseName(@Param("warehouseName") String warehouseName);

    /**
     * Get all warehouses with location, city, and state eagerly fetched - for export
     */
    @Query("SELECT w FROM Warehouse w " +
           "LEFT JOIN FETCH w.location l " +
           "LEFT JOIN FETCH l.city c " +
           "LEFT JOIN FETCH c.state " +
           "ORDER BY w.warehouseName ASC")
    java.util.List<Warehouse> findAllWithLocationAndCityAndState();
}
