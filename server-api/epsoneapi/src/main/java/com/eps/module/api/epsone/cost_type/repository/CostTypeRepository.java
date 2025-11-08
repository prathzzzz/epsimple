package com.eps.module.api.epsone.cost_type.repository;

import com.eps.module.cost.CostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostTypeRepository extends JpaRepository<CostType, Long> {

    @Query("SELECT ct FROM CostType ct WHERE " +
            "LOWER(ct.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(ct.typeDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(ct.costCategory.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<CostType> searchCostTypes(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<CostType> findByCostCategoryId(Long costCategoryId, Pageable pageable);

    // Bulk upload support
    boolean existsByTypeNameIgnoreCase(String typeName);

    Optional<CostType> findByTypeNameIgnoreCase(String typeName);

    @Query("SELECT ct FROM CostType ct LEFT JOIN FETCH ct.costCategory ORDER BY ct.typeName ASC")
    List<CostType> findAllForExport();
}
