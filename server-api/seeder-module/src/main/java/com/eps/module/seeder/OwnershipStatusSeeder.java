package com.eps.module.seeder;

import com.eps.module.seeder.repository.status.OwnershipStatusRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;
import com.eps.module.status.OwnershipStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(107)
@RequiredArgsConstructor
@Slf4j
public class OwnershipStatusSeeder extends AbstractSeeder {

    private final OwnershipStatusRepository ownershipStatusRepository;

    @Override
    public String getSeederName() {
        return "OwnershipStatus";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return ownershipStatusRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<OwnershipStatus> ownershipStatuses = Arrays.asList(
                OwnershipStatus.builder()
                        .typeName("Owned")
                        .typeDescription("Resource or item of value that is legally owned by an individual, organization, or entity")
                        .build(),

                OwnershipStatus.builder()
                        .typeName("Lease")
                        .typeDescription("A leased asset is used by a lessee through a rental agreement, while legal ownership remains with the lessor throughout")
                        .build()
        );

        ownershipStatusRepository.saveAll(ownershipStatuses);
        log.info("Seeded {} ownership statuses", ownershipStatuses.size());
    }
}
