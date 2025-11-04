package com.eps.module.api.epsone.cost_item.repository;

import com.eps.module.cost.CostItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostItemRepository extends JpaRepository<CostItem, Long> {

    @Query("SELECT ci FROM CostItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "WHERE LOWER(ci.costItemFor) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ci.itemDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ct.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<CostItem> searchCostItems(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT ci FROM CostItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc")
    List<CostItem> findAllList();

    @Query("SELECT ci FROM CostItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "WHERE ci.id = :id")
    Optional<CostItem> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT ci FROM CostItem ci " +
            "WHERE ci.costType.id = :costTypeId")
    Optional<CostItem> findByCostTypeId(@Param("costTypeId") Long costTypeId);

    @Query("SELECT COUNT(ci) FROM CostItem ci WHERE ci.costType.id = :costTypeId")
    long countByCostTypeId(@Param("costTypeId") Long costTypeId);
}
