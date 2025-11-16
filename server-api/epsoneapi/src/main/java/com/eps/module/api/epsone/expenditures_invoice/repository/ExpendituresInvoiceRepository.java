package com.eps.module.api.epsone.expenditures_invoice.repository;

import com.eps.module.cost.ExpendituresInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpendituresInvoiceRepository extends JpaRepository<ExpendituresInvoice, Long> {

    // Bulk upload support methods
    @Query("SELECT CASE WHEN COUNT(ei) > 0 THEN true ELSE false END FROM ExpendituresInvoice ei " +
            "LEFT JOIN ei.invoice i " +
            "WHERE i.invoiceNumber = :invoiceNumber")
    boolean existsByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);
    
    @Query("SELECT ei FROM ExpendituresInvoice ei " +
            "LEFT JOIN FETCH ei.invoice i " +
            "WHERE i.invoiceNumber = :invoiceNumber")
    Optional<ExpendituresInvoice> findByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    @Query("SELECT DISTINCT ei FROM ExpendituresInvoice ei " +
            "LEFT JOIN FETCH ei.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ei.invoice i " +
            "LEFT JOIN FETCH i.payee p " +
            "LEFT JOIN FETCH ei.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "WHERE LOWER(ci.costItemFor) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ct.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(cc.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(mp.projectName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ei.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ExpendituresInvoice> searchExpendituresInvoices(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT ei FROM ExpendituresInvoice ei " +
            "LEFT JOIN FETCH ei.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ei.invoice i " +
            "LEFT JOIN FETCH i.payee p " +
            "LEFT JOIN FETCH ei.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b")
    Page<ExpendituresInvoice> findAllWithDetails(Pageable pageable);

    @Query("SELECT ei FROM ExpendituresInvoice ei " +
            "LEFT JOIN FETCH ei.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ei.invoice i " +
            "LEFT JOIN FETCH i.payee p " +
            "LEFT JOIN FETCH ei.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "WHERE ei.managedProject.id = :projectId")
    Page<ExpendituresInvoice> findByManagedProjectId(@Param("projectId") Long projectId, Pageable pageable);

    @Query("SELECT ei FROM ExpendituresInvoice ei " +
            "LEFT JOIN FETCH ei.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ei.invoice i " +
            "LEFT JOIN FETCH i.payee p " +
            "LEFT JOIN FETCH ei.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "WHERE ei.invoice.id = :invoiceId")
    List<ExpendituresInvoice> findByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Query("SELECT ei FROM ExpendituresInvoice ei " +
            "LEFT JOIN FETCH ei.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ei.invoice i " +
            "LEFT JOIN FETCH i.payee p " +
            "LEFT JOIN FETCH ei.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "WHERE ei.costItem.id = :costItemId")
    Page<ExpendituresInvoice> findByCostItemId(@Param("costItemId") Long costItemId, Pageable pageable);

    @Query("SELECT ei FROM ExpendituresInvoice ei " +
            "LEFT JOIN FETCH ei.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ei.invoice i " +
            "LEFT JOIN FETCH i.payee p " +
            "LEFT JOIN FETCH ei.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b")
    List<ExpendituresInvoice> findAllWithDetailsList();

    @Query("SELECT ei FROM ExpendituresInvoice ei " +
            "LEFT JOIN FETCH ei.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ei.invoice i " +
            "LEFT JOIN FETCH i.payee p " +
            "LEFT JOIN FETCH ei.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "ORDER BY ei.id")
    List<ExpendituresInvoice> findAllForExport();
}
