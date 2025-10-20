package com.eps.module.seeder.repository.status;

import com.eps.module.status.OwnershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnershipStatusRepository extends JpaRepository<OwnershipStatus, Long> {
}
