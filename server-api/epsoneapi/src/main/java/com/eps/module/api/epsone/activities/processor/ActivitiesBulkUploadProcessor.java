package com.eps.module.api.epsone.activities.processor;

import com.eps.module.activity.Activities;
import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activities.dto.ActivitiesBulkUploadDto;
import com.eps.module.api.epsone.activities.repository.ActivitiesRepository;
import com.eps.module.api.epsone.activities.validator.ActivitiesBulkUploadValidator;
import com.eps.module.api.epsone.activity.repository.ActivityRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivitiesBulkUploadProcessor extends BulkUploadProcessor<ActivitiesBulkUploadDto, Activities> {

    private final ActivitiesRepository activitiesRepository;
    private final ActivityRepository activityRepository;
    private final ActivitiesBulkUploadValidator validator;

    @Override
    protected ActivitiesBulkUploadValidator getValidator() {
        return validator;
    }

    @Override
    @Transactional
    protected Activities convertToEntity(ActivitiesBulkUploadDto dto) {
        // Find master activity by name - fetch eagerly to avoid lazy initialization exception
        Activity masterActivity = activityRepository
                .findByActivityNameIgnoreCase(dto.getMasterActivityName().trim())
                .orElseThrow(() -> new RuntimeException("Master activity not found: " + dto.getMasterActivityName()));

        // Build Activities entity with the fetched Activity
        Activities activities = Activities.builder()
                .activityName(dto.getActivityName() != null ? dto.getActivityName().trim() : null)
                .activityCategory(dto.getActivityCategory() != null && !dto.getActivityCategory().trim().isEmpty() 
                        ? dto.getActivityCategory().trim() : null)
                .activityDescription(dto.getActivityDescription() != null && !dto.getActivityDescription().trim().isEmpty() 
                        ? dto.getActivityDescription().trim() : null)
                .build();
        
        // Set the activity relationship
        activities.setActivity(masterActivity);
        
        return activities;
    }

    @Override
    @Transactional
    protected void saveEntity(Activities entity) {
        activitiesRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(ActivitiesBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Activity Name (Master)", dto.getMasterActivityName());
        rowData.put("Activity Name", dto.getActivityName());
        rowData.put("Activity Category", dto.getActivityCategory());
        rowData.put("Activity Description", dto.getActivityDescription());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(ActivitiesBulkUploadDto dto) {
        return (dto.getMasterActivityName() == null || dto.getMasterActivityName().trim().isEmpty()) &&
               (dto.getActivityName() == null || dto.getActivityName().trim().isEmpty()) &&
               (dto.getActivityCategory() == null || dto.getActivityCategory().trim().isEmpty()) &&
               (dto.getActivityDescription() == null || dto.getActivityDescription().trim().isEmpty());
    }
}
