package com.eps.module.api.epsone.bank.repository;

import com.eps.module.bank.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    Optional<Bank> findByBankName(String bankName);

    Optional<Bank> findByRbiBankCode(String rbiBankCode);

    Optional<Bank> findByEpsBankCode(String epsBankCode);

    Optional<Bank> findByBankCodeAlt(String bankCodeAlt);

    @Query("SELECT b FROM Bank b WHERE " +
           "LOWER(b.bankName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.rbiBankCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.epsBankCode) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Bank> searchBanks(@Param("search") String search, Pageable pageable);
}
