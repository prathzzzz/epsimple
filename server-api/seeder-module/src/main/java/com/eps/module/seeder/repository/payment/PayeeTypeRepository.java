package com.eps.module.seeder.repository.payment;

import com.eps.module.payment.PayeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayeeTypeRepository extends JpaRepository<PayeeType, Long> {
}
