package com.eps.module.api.epsone.costtype.repository;

import com.eps.module.cost.CostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CostTypeRepository extends JpaRepository<CostType, Long> {

    @Query("SELECT ct FROM CostType ct WHERE " +
            "LOWER(ct.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(ct.typeDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(ct.costCategory.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<CostType> searchCostTypes(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<CostType> findByCostCategoryId(Long costCategoryId, Pageable pageable);
}
