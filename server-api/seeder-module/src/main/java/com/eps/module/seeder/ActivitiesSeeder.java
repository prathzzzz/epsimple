package com.eps.module.seeder;

import com.eps.module.activity.Activities;
import com.eps.module.activity.Activity;
import com.eps.module.seeder.repository.activity.ActivitiesRepository;
import com.eps.module.seeder.repository.activity.ActivitySeederRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(110)
@RequiredArgsConstructor
@Slf4j
public class ActivitiesSeeder extends AbstractSeeder {

    private final ActivitiesRepository activitiesRepository;
    private final ActivitySeederRepository activitySeederRepository;

    @Override
    public String getSeederName() {
        return "Activities";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return activitiesRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        // Get Site Readiness activity (ID=1)
        Activity siteReadiness = activitySeederRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Site Readiness activity not found. Please run ActivitySeeder first."));

        List<Activities> activitiesList = Arrays.asList(
                Activities.builder()
                        .activity(siteReadiness)
                        .activityName("TIS")
                        .activityCategory("TIS")
                        .activityDescription("Turnkey Infrastructure Setup – Full site readiness with integrated systems and utilities")
                        .build(),

                Activities.builder()
                        .activity(siteReadiness)
                        .activityName("UPS")
                        .activityCategory("Power Backup")
                        .activityDescription("Installation and configuration of UPS backup systems")
                        .build(),

                Activities.builder()
                        .activity(siteReadiness)
                        .activityName("VSAT")
                        .activityCategory("Connectivity")
                        .activityDescription("Installation and testing of VSAT connectivity solutions")
                        .build(),

                Activities.builder()
                        .activity(siteReadiness)
                        .activityName("AC")
                        .activityCategory("Environmental Control")
                        .activityDescription("Setup and validation of air conditioning systems")
                        .build(),

                Activities.builder()
                        .activity(siteReadiness)
                        .activityName("E-Surveillance")
                        .activityCategory("Surveillance")
                        .activityDescription("Installation of electronic surveillance systems (CCTV, DVR, etc.)")
                        .build(),

                Activities.builder()
                        .activity(siteReadiness)
                        .activityName("Signage")
                        .activityCategory("Visual Setup")
                        .activityDescription("Installation of sign boards for visual identity elements")
                        .build(),

                Activities.builder()
                        .activity(siteReadiness)
                        .activityName("Branding")
                        .activityCategory("Visual Setup")
                        .activityDescription("Installation of branding materials for visual identity elements")
                        .build()
        );

        activitiesRepository.saveAll(activitiesList);
        log.info("Seeded {} activities", activitiesList.size());
    }
}
