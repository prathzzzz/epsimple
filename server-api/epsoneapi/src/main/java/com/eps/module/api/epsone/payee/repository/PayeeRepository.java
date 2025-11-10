package com.eps.module.api.epsone.payee.repository;

import com.eps.module.payment.Payee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayeeRepository extends JpaRepository<Payee, Long> {

    @Query("SELECT p FROM Payee p " +
            "LEFT JOIN FETCH p.payeeType pt " +
            "LEFT JOIN FETCH p.payeeDetails pd " +
            "LEFT JOIN FETCH pd.bank " +
            "LEFT JOIN FETCH p.vendor v " +
            "LEFT JOIN FETCH v.vendorDetails " +
            "LEFT JOIN FETCH p.landlord l " +
            "LEFT JOIN FETCH l.landlordDetails " +
            "WHERE p.id = :id")
    Optional<Payee> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT p FROM Payee p " +
            "LEFT JOIN FETCH p.payeeType pt " +
            "LEFT JOIN FETCH p.payeeDetails pd " +
            "LEFT JOIN FETCH pd.bank " +
            "LEFT JOIN FETCH p.vendor v " +
            "LEFT JOIN FETCH v.vendorDetails " +
            "LEFT JOIN FETCH p.landlord l " +
            "LEFT JOIN FETCH l.landlordDetails")
    Page<Payee> findAllWithDetails(Pageable pageable);

    @Query("SELECT p FROM Payee p " +
            "LEFT JOIN FETCH p.payeeType pt " +
            "LEFT JOIN FETCH p.payeeDetails pd " +
            "LEFT JOIN FETCH pd.bank b " +
            "LEFT JOIN FETCH p.vendor v " +
            "LEFT JOIN FETCH v.vendorDetails vd " +
            "LEFT JOIN FETCH p.landlord l " +
            "LEFT JOIN FETCH l.landlordDetails lpd " +
            "WHERE LOWER(pt.payeeType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(pd.payeeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(pd.accountNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.bankName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vd.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(vd.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(lpd.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(lpd.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Payee> searchPayees(@Param("searchTerm") String searchTerm, Pageable pageable);

    long countByPayeeTypeId(Long payeeTypeId);

    long countByPayeeDetailsId(Long payeeDetailsId);

    long countByVendorId(Long vendorId);

    long countByLandlordId(Long landlordId);

    @Query("SELECT p FROM Payee p " +
            "LEFT JOIN FETCH p.payeeType pt " +
            "LEFT JOIN FETCH p.payeeDetails pd " +
            "LEFT JOIN FETCH pd.bank " +
            "LEFT JOIN FETCH p.vendor v " +
            "LEFT JOIN FETCH v.vendorDetails " +
            "LEFT JOIN FETCH p.landlord l " +
            "LEFT JOIN FETCH l.landlordDetails " +
            "ORDER BY p.id")
    java.util.List<Payee> findAllForExport();
}
