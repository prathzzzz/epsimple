package com.eps.module.api.epsone.expenditures_voucher.repository;

import com.eps.module.cost.ExpendituresVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpendituresVoucherRepository extends JpaRepository<ExpendituresVoucher, Long> {

    @Query("SELECT DISTINCT ev FROM ExpendituresVoucher ev " +
            "LEFT JOIN FETCH ev.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ev.voucher v " +
            "LEFT JOIN FETCH v.payee p " +
            "LEFT JOIN FETCH ev.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "WHERE LOWER(ci.costItemFor) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ct.typeName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(cc.categoryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(v.voucherNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(mp.projectName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(ev.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ExpendituresVoucher> searchExpendituresVouchers(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT ev FROM ExpendituresVoucher ev " +
            "LEFT JOIN FETCH ev.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ev.voucher v " +
            "LEFT JOIN FETCH v.payee p " +
            "LEFT JOIN FETCH ev.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b")
    Page<ExpendituresVoucher> findAllWithDetails(Pageable pageable);

    @Query("SELECT ev FROM ExpendituresVoucher ev " +
            "LEFT JOIN FETCH ev.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ev.voucher v " +
            "LEFT JOIN FETCH v.payee p " +
            "LEFT JOIN FETCH ev.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "WHERE ev.managedProject.id = :projectId")
    Page<ExpendituresVoucher> findByManagedProjectId(@Param("projectId") Long projectId, Pageable pageable);

    @Query("SELECT ev FROM ExpendituresVoucher ev " +
            "LEFT JOIN FETCH ev.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ev.voucher v " +
            "LEFT JOIN FETCH v.payee p " +
            "LEFT JOIN FETCH ev.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "WHERE ev.voucher.id = :voucherId")
    List<ExpendituresVoucher> findByVoucherId(@Param("voucherId") Long voucherId);

    @Query("SELECT ev FROM ExpendituresVoucher ev " +
            "LEFT JOIN FETCH ev.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ev.voucher v " +
            "LEFT JOIN FETCH v.payee p " +
            "LEFT JOIN FETCH ev.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b " +
            "WHERE ev.costItem.id = :costItemId")
    Page<ExpendituresVoucher> findByCostItemId(@Param("costItemId") Long costItemId, Pageable pageable);

    @Query("SELECT ev FROM ExpendituresVoucher ev " +
            "LEFT JOIN FETCH ev.costItem ci " +
            "LEFT JOIN FETCH ci.costType ct " +
            "LEFT JOIN FETCH ct.costCategory cc " +
            "LEFT JOIN FETCH ev.voucher v " +
            "LEFT JOIN FETCH v.payee p " +
            "LEFT JOIN FETCH ev.managedProject mp " +
            "LEFT JOIN FETCH mp.bank b")
    List<ExpendituresVoucher> findAllWithDetailsList();
}
