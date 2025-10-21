package com.eps.module.api.epsone.paymentmethod.repository;

import com.eps.module.payment.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query("SELECT pm FROM PaymentMethod pm WHERE " +
            "LOWER(pm.methodName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pm.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PaymentMethod> searchPaymentMethods(@Param("searchTerm") String searchTerm, Pageable pageable);

    boolean existsByMethodNameIgnoreCase(String methodName);

    boolean existsByMethodNameAndIdNot(String methodName, Long id);
}
