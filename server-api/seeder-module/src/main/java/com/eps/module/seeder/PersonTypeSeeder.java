package com.eps.module.seeder;

import com.eps.module.seeder.repository.person.PersonTypeSeederRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;
import com.eps.module.person.PersonType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(101)
@RequiredArgsConstructor
@Slf4j
public class PersonTypeSeeder extends AbstractSeeder {

    private final PersonTypeSeederRepository personTypeRepository;

    @Override
    public String getSeederName() {
        return "PersonType";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return personTypeRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<PersonType> personTypes = Arrays.asList(
                PersonType.builder()
                        .typeName("Vendor")
                        .description("Individual or organization that provides goods, services, or supplies to the business for ATM site operations and maintenance")
                        .build(),

                PersonType.builder()
                        .typeName("Landlord")
                        .description("Property owner who leases or rents premises for ATM site installation and operations")
                        .build(),

                PersonType.builder()
                        .typeName("Bank")
                        .description("Financial institution representative who oversees and manages ATM operations and banking services")
                        .build(),

                PersonType.builder()
                        .typeName("Channel Manager")
                        .description("Personnel responsible for managing and coordinating ATM channel operations, site activities, and service delivery")
                        .build()
        );

        personTypeRepository.saveAll(personTypes);
        log.info("Seeded {} person types", personTypes.size());
    }
}
