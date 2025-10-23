package com.eps.module.seeder;

import com.eps.module.seeder.repository.bank.BankSeederRepository;
import com.eps.module.seeder.repository.bank.ManagedProjectSeederRepository;
import com.eps.module.bank.Bank;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.seeder.base.AbstractSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(111)
@RequiredArgsConstructor
@Slf4j
public class ManagedProjectSeeder extends AbstractSeeder {

    private final ManagedProjectSeederRepository managedProjectSeederRepository;
    private final BankSeederRepository bankRepository;

    @Override
    public String getSeederName() {
        return "ManagedProject";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return managedProjectSeederRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        // Get banks
        Bank baroda = bankRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Bank of Baroda not found. Please run BankSeeder first."));
        Bank boi = bankRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Bank of India not found. Please run BankSeeder first."));
        Bank bancs = bankRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Bharat ATM Network not found. Please run BankSeeder first."));

        List<ManagedProject> projects = Arrays.asList(
                ManagedProject.builder()
                        .bank(baroda)
                        .projectType("BLA")
                        .projectName("BOB")
                        .projectCode("BOB")
                        .projectDescription("Bank of Baroda ATM deployment and management project for branch and offsite locations")
                        .build(),

                ManagedProject.builder()
                        .bank(boi)
                        .projectType("BLA")
                        .projectName("BOI")
                        .projectCode("7BOI")
                        .projectDescription("Bank of India ATM network expansion and maintenance project")
                        .build(),

                ManagedProject.builder()
                        .bank(bancs)
                        .projectType("WLA")
                        .projectName("epsBANCS")
                        .projectCode("EB")
                        .projectDescription("Bharat ATM Network Customer Services - White label ATM deployment project")
                        .build()
        );

        managedProjectSeederRepository.saveAll(projects);
        log.info("Seeded {} managed projects", projects.size());
    }
}
