package com.eps.module.api.epsone.state.repository;

import com.eps.module.location.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    Optional<State> findByStateName(String stateName);

    Optional<State> findByStateCode(String stateCode);

    boolean existsByStateName(String stateName);

    boolean existsByStateCode(String stateCode);
    
    boolean existsByStateNameIgnoreCase(String stateName);
    
    boolean existsByStateCodeIgnoreCase(String stateCode);

    @Query("SELECT s FROM State s WHERE " +
           "LOWER(s.stateName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.stateCode) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<State> searchStates(@Param("search") String search, Pageable pageable);
}
