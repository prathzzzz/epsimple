package com.eps.module.seeder.repository.person;

import com.eps.module.person.PersonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTypeSeederRepository extends JpaRepository<PersonType, Long> {
}
