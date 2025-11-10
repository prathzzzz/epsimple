package com.eps.module.api.epsone.payee_type.repository;

import com.eps.module.payment.PayeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayeeTypeRepository extends JpaRepository<PayeeType, Long> {

    @Query("SELECT pt FROM PayeeType pt WHERE " +
            "LOWER(pt.payeeType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pt.payeeCategory) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pt.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PayeeType> searchPayeeTypes(@Param("searchTerm") String searchTerm, Pageable pageable);

    boolean existsByPayeeTypeIgnoreCase(String payeeType);

    boolean existsByPayeeTypeAndIdNot(String payeeType, Long id);

    @Query("SELECT pt FROM PayeeType pt WHERE LOWER(pt.payeeType) = LOWER(:payeeType)")
    java.util.Optional<PayeeType> findByPayeeTypeIgnoreCase(@Param("payeeType") String payeeType);

    // Bulk upload methods
    @Query("SELECT pt FROM PayeeType pt ORDER BY pt.payeeType ASC")
    List<PayeeType> findAllForExport();
}
