package com.eps.module.api.epsone.movementtype.repository;

import com.eps.module.asset.AssetMovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementTypeRepository extends JpaRepository<AssetMovementType, Long> {
    
    @Query("SELECT mt FROM AssetMovementType mt WHERE " +
           "LOWER(mt.movementType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(mt.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<AssetMovementType> searchMovementTypes(@Param("search") String search, Pageable pageable);
    
    boolean existsByMovementTypeIgnoreCase(String movementType);
    
    @Query("SELECT CASE WHEN COUNT(mt) > 0 THEN true ELSE false END FROM AssetMovementType mt WHERE " +
           "LOWER(mt.movementType) = LOWER(:movementType) AND mt.id <> :id")
    boolean existsByMovementTypeAndIdNot(@Param("movementType") String movementType, @Param("id") Long id);
}
