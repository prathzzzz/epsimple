package com.eps.module.seeder.repository.bank;

import com.eps.module.bank.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankSeederRepository extends JpaRepository<Bank, Long> {
}
