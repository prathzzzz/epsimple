package com.eps.module.seeder;

import com.eps.module.activity.Activity;
import com.eps.module.seeder.repository.activity.ActivityRepository;
import com.eps.module.common.seeder.base.AbstractSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(100)
@RequiredArgsConstructor
@Slf4j
public class ActivitySeeder extends AbstractSeeder {

    private final ActivityRepository activityRepository;

    @Override
    public String getSeederName() {
        return "Activity";
    }

    @Override
    protected boolean shouldSkipSeeding() {
        return activityRepository.count() > 0;
    }

    @Override
    protected void performSeeding() {
        List<Activity> activities = Arrays.asList(
                Activity.builder()
                        .activityName("Site Readiness")
                        .activityDescription("The complete set of activities like infrastructure, connectivity, security, branding and environmental conditions required to prepare a physical location (ATM Site) for operational use.")
                        .build(),

                Activity.builder()
                        .activityName("Site Maintenance")
                        .activityDescription("Includes routine inspections, repair work, cleaning, replenishment of consumables, equipment servicing, and addressing any environmental or infrastructure issues.")
                        .build(),

                Activity.builder()
                        .activityName("Site Repair")
                        .activityDescription("This includes repairing infrastructure, electrical systems, connectivity, security equipment, or any other components essential for safe and efficient ATM operation.")
                        .build()
        );

        activityRepository.saveAll(activities);
        log.info("Seeded {} activities", activities.size());
    }
}
