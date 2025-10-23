package com.eps.module.api.epsone.genericstatustype.repository;

import com.eps.module.status.GenericStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericStatusTypeRepository extends JpaRepository<GenericStatusType, Long> {

    @Query("SELECT g FROM GenericStatusType g WHERE " +
            "LOWER(g.statusName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.statusCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<GenericStatusType> searchGenericStatusTypes(@Param("searchTerm") String searchTerm, Pageable pageable);

    boolean existsByStatusNameIgnoreCase(String statusName);

    boolean existsByStatusNameAndIdNot(String statusName, Long id);

    boolean existsByStatusCodeIgnoreCase(String statusCode);

    boolean existsByStatusCodeAndIdNot(String statusCode, Long id);
}
