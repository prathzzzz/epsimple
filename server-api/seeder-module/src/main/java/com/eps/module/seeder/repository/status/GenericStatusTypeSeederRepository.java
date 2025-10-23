package com.eps.module.seeder.repository.status;

import com.eps.module.status.GenericStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericStatusTypeSeederRepository extends JpaRepository<GenericStatusType, Long> {
}
