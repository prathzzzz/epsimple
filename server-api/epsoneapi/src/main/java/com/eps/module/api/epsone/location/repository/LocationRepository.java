package com.eps.module.api.epsone.location.repository;

import com.eps.module.location.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Search locations by name, address, district, or pincode
     */
    @Query("SELECT l FROM Location l WHERE " +
           "LOWER(l.locationName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.district) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.pincode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Location> searchLocations(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Get all locations as a list (no pagination) - for dropdowns
     */
    @Query("SELECT l FROM Location l ORDER BY l.locationName ASC")
    List<Location> findAllList();

    /**
     * Find locations by city (for dependency checking when deleting cities)
     */
    @Query("SELECT l FROM Location l WHERE l.city.id = :cityId ORDER BY l.locationName ASC")
    Page<Location> findByCityId(@Param("cityId") Long cityId, Pageable pageable);

    /**
     * Count locations by city (for dependency protection)
     */
    long countByCityId(Long cityId);

    /**
     * Find location by name (case-insensitive) - for duplicate checking
     */
    @Query("SELECT l FROM Location l WHERE LOWER(l.locationName) = LOWER(:locationName)")
    java.util.Optional<Location> findByLocationName(@Param("locationName") String locationName);

    /**
     * Find location by name with city and state eagerly fetched - for bulk upload
     */
    @Query("SELECT l FROM Location l LEFT JOIN FETCH l.city c LEFT JOIN FETCH c.state WHERE LOWER(l.locationName) = LOWER(:locationName)")
    java.util.Optional<Location> findByLocationNameWithCityAndState(@Param("locationName") String locationName);

    /**
     * Get all locations with city and state eagerly fetched - for export
     */
    @Query("SELECT l FROM Location l LEFT JOIN FETCH l.city c LEFT JOIN FETCH c.state ORDER BY l.locationName ASC")
    List<Location> findAllWithCityAndState();
}
