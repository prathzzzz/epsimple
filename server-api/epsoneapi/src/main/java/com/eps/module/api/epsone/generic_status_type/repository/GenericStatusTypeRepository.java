package com.eps.module.api.epsone.generic_status_type.repository;

import com.eps.module.status.GenericStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericStatusTypeRepository extends JpaRepository<GenericStatusType, Long> {

    @Query("SELECT g FROM GenericStatusType g WHERE " +
            "LOWER(g.statusName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.statusCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(g.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<GenericStatusType> searchGenericStatusTypes(@Param("searchTerm") String searchTerm, Pageable pageable);

    boolean existsByStatusNameIgnoreCase(String statusName);

    boolean existsByStatusNameAndIdNot(String statusName, Long id);

    boolean existsByStatusCode(String statusCode);

    boolean existsByStatusCodeAndIdNot(String statusCode, Long id);

    @Query("SELECT g FROM GenericStatusType g ORDER BY g.statusName ASC")
    List<GenericStatusType> findAllForExport();
}
