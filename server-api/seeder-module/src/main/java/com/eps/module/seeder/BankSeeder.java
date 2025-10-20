package com.eps.module.seeder;

import com.eps.module.seeder.repository.bank.BankSeederRepository;
import com.eps.module.bank.Bank;
import com.eps.module.common.seeder.base.AbstractSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(104)
@RequiredArgsConstructor
@Slf4j
public class BankSeeder extends AbstractSeeder {

    private final BankSeederRepository bankRepository;

    @Override
    public String getSeederName() {
        return "Bank";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return bankRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<Bank> banks = Arrays.asList(
                Bank.builder()
                        .bankName("Bank of Baroda")
                        .rbiBankCode("BARB")
                        .epsBankCode("BOB")
                        .bankLogo(null)
                        .build(),

                Bank.builder()
                        .bankName("Bank of India")
                        .rbiBankCode("BKID")
                        .epsBankCode("BOI")
                        .bankLogo(null)
                        .build(),

                Bank.builder()
                        .bankName("Bharat ATM Network Customer Services")
                        .rbiBankCode(null)
                        .epsBankCode("BANCS")
                        .bankLogo(null)
                        .build()
        );

        bankRepository.saveAll(banks);
        log.info("Seeded {} banks", banks.size());
    }
}
