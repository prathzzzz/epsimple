package com.eps.module.api.epsone.payment_method.repository;

import com.eps.module.payment.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query("SELECT pm FROM PaymentMethod pm WHERE " +
            "LOWER(pm.methodName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pm.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<PaymentMethod> searchPaymentMethods(@Param("searchTerm") String searchTerm, Pageable pageable);

    boolean existsByMethodNameIgnoreCase(String methodName);

    Optional<PaymentMethod> findByMethodNameIgnoreCase(String methodName);

    boolean existsByMethodNameAndIdNot(String methodName, Long id);

    @Query("SELECT pm FROM PaymentMethod pm ORDER BY pm.id")
    List<PaymentMethod> findAllForExport();
}
