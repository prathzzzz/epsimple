package com.eps.module.api.epsone.invoice.repository;

import com.eps.module.payment.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i " +
            "LEFT JOIN FETCH i.payee p " +
            "LEFT JOIN FETCH p.payeeDetails pd " +
            "LEFT JOIN FETCH p.payeeType pt " +
            "LEFT JOIN FETCH i.paymentDetails pmd " +
            "WHERE LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(i.vendorName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(i.orderNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pd.payeeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(i.paymentStatus) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pmd.transactionNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Invoice> searchInvoices(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.payee p WHERE p.id = :payeeId")
    List<Invoice> findByPayeeId(@Param("payeeId") Long payeeId);

    boolean existsByInvoiceNumber(String invoiceNumber);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Invoice i WHERE i.invoiceNumber = :invoiceNumber AND i.id != :id")
    boolean existsByInvoiceNumberAndIdNot(@Param("invoiceNumber") String invoiceNumber, @Param("id") Long id);
}
