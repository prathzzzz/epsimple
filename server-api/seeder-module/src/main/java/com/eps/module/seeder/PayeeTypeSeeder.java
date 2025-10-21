package com.eps.module.seeder;

import com.eps.module.seeder.repository.payment.PayeeTypeSeederRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;
import com.eps.module.payment.PayeeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(102)
@RequiredArgsConstructor
@Slf4j
public class PayeeTypeSeeder extends AbstractSeeder {

    private final PayeeTypeSeederRepository payeeTypeSeederRepository;

    @Override
    public String getSeederName() {
        return "PayeeType";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return payeeTypeSeederRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<PayeeType> payeeTypes = Arrays.asList(
                PayeeType.builder()
                        .payeeType("Vendor")
                        .payeeCategory("Site and Asset Vendors")
                        .description("Vendors managing Site and providing Assets")
                        .build(),

                PayeeType.builder()
                        .payeeType("Landlord")
                        .payeeCategory("Site Landlord")
                        .description("Site's Landlord whom the Rent for the site will be paid")
                        .build(),

                PayeeType.builder()
                        .payeeType("Channel Manager")
                        .payeeCategory("Site Channel Manager")
                        .description("Person who manages the Site")
                        .build()
        );

        payeeTypeSeederRepository.saveAll(payeeTypes);
        log.info("Seeded {} payee types", payeeTypes.size());
    }
}
