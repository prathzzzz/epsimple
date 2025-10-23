package com.eps.module.seeder.repository.bank;

import com.eps.module.bank.ManagedProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedProjectSeederRepository extends JpaRepository<ManagedProject, Long> {
}
