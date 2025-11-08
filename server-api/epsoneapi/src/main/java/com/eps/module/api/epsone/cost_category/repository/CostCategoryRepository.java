package com.eps.module.api.epsone.cost_category.repository;

import com.eps.module.cost.CostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostCategoryRepository extends JpaRepository<CostCategory, Long> {
    
    @Query("SELECT cc FROM CostCategory cc WHERE " +
           "LOWER(cc.categoryName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(cc.categoryDescription) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<CostCategory> searchCostCategories(@Param("search") String search, Pageable pageable);
    
    boolean existsByCategoryNameIgnoreCase(String categoryName);
    
    @Query("SELECT CASE WHEN COUNT(cc) > 0 THEN true ELSE false END FROM CostCategory cc WHERE " +
           "LOWER(cc.categoryName) = LOWER(:categoryName) AND cc.id <> :id")
    boolean existsByCategoryNameAndIdNot(@Param("categoryName") String categoryName, @Param("id") Long id);

    @Query("SELECT cc FROM CostCategory cc ORDER BY cc.categoryName ASC")
    List<CostCategory> findAllForExport();

    Optional<CostCategory> findByCategoryNameIgnoreCase(String categoryName);
}
