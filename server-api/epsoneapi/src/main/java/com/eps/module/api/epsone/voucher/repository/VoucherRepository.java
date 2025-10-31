package com.eps.module.api.epsone.voucher.repository;

import com.eps.module.payment.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query("SELECT v FROM Voucher v " +
           "LEFT JOIN FETCH v.payee p " +
           "LEFT JOIN FETCH p.payeeType pt " +
           "LEFT JOIN FETCH p.payeeDetails pd " +
           "LEFT JOIN FETCH v.paymentDetails pmd " +
           "WHERE LOWER(v.voucherNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(v.orderNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(pd.payeeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(v.paymentStatus) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(pmd.transactionNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Voucher> searchVouchers(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT v FROM Voucher v " +
           "LEFT JOIN FETCH v.payee p " +
           "LEFT JOIN FETCH p.payeeType pt " +
           "LEFT JOIN FETCH p.payeeDetails pd " +
           "LEFT JOIN FETCH v.paymentDetails pmd " +
           "WHERE p.id = :payeeId")
    Page<Voucher> findByPayeeId(@Param("payeeId") Long payeeId, Pageable pageable);

    boolean existsByVoucherNumberIgnoreCase(String voucherNumber);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Voucher v " +
           "WHERE LOWER(v.voucherNumber) = LOWER(:voucherNumber) AND v.id <> :id")
    boolean existsByVoucherNumberIgnoreCaseAndIdNot(@Param("voucherNumber") String voucherNumber, @Param("id") Long id);
}
