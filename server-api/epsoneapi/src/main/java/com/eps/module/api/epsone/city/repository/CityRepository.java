package com.eps.module.api.epsone.city.repository;

import com.eps.module.location.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c LEFT JOIN FETCH c.state WHERE c.id = :id")
    Optional<City> findByIdWithState(@Param("id") Long id);

    @Query("SELECT c FROM City c LEFT JOIN FETCH c.state")
    Page<City> findAllWithState(Pageable pageable);

    @Query("SELECT c FROM City c LEFT JOIN FETCH c.state " +
           "WHERE LOWER(c.cityName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.cityCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<City> searchCities(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT c FROM City c LEFT JOIN FETCH c.state WHERE c.state.id = :stateId")
    Page<City> findByStateId(@Param("stateId") Long stateId, Pageable pageable);

    @Query("SELECT c FROM City c LEFT JOIN FETCH c.state ORDER BY c.cityName ASC")
    List<City> findAllCitiesList();

    Optional<City> findByCityCode(String cityCode);
}
