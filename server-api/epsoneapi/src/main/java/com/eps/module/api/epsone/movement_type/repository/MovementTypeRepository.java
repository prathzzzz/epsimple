package com.eps.module.api.epsone.movement_type.repository;

import com.eps.module.asset.AssetMovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    
    // Find movement type by exact name
    Optional<AssetMovementType> findByMovementType(String movementType);

    // Bulk Upload Methods
    @Query("SELECT mt FROM AssetMovementType mt ORDER BY mt.movementType ASC")
    List<AssetMovementType> findAllForExport();
}
