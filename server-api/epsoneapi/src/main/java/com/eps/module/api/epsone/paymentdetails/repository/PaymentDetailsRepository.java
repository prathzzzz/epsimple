package com.eps.module.api.epsone.paymentdetails.repository;

import com.eps.module.payment.PaymentDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    @Query("SELECT pd FROM PaymentDetails pd LEFT JOIN FETCH pd.paymentMethod pm WHERE " +
            "LOWER(pm.methodName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pd.transactionNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pd.beneficiaryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pd.beneficiaryAccountNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pd.vpa) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PaymentDetails> searchPaymentDetails(@Param("searchTerm") String searchTerm, Pageable pageable);
}
