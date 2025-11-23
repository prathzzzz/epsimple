package com.eps.module.api.epsone.activities.validator;

import com.eps.module.api.epsone.activities.constant.ActivitiesErrorMessages;
import com.eps.module.api.epsone.activities.dto.ActivitiesBulkUploadDto;
import com.eps.module.api.epsone.activities.repository.ActivitiesRepository;
import com.eps.module.api.epsone.activity.repository.ActivityRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivitiesBulkUploadValidator implements BulkRowValidator<ActivitiesBulkUploadDto> {

    private final ActivitiesRepository activitiesRepository;
    private final ActivityRepository activityRepository;

    @Override
    public List<BulkUploadErrorDto> validate(ActivitiesBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Master Activity Name (FK)
        if (rowData.getMasterActivityName() == null || rowData.getMasterActivityName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Activity Name (Master)")
                    .errorMessage(ActivitiesErrorMessages.MASTER_ACTIVITY_NAME_REQUIRED)
                    .rejectedValue(rowData.getMasterActivityName())
                    .build());
        } else {
            // Check if master activity exists
            boolean activityExists = activityRepository.existsByActivityNameIgnoreCase(
                    rowData.getMasterActivityName().trim()
            );
            
            if (!activityExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Activity Name (Master)")
                        .errorMessage(String.format(ActivitiesErrorMessages.MASTER_ACTIVITY_NOT_FOUND_NAME, rowData.getMasterActivityName()))
                        .rejectedValue(rowData.getMasterActivityName())
                        .build());
            }
        }

        // Validate Activity Name
        if (rowData.getActivityName() == null || rowData.getActivityName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Activity Name")
                    .errorMessage(ActivitiesErrorMessages.ACTIVITY_NAME_REQUIRED)
                    .rejectedValue(rowData.getActivityName())
                    .build());
        } else if (rowData.getActivityName().trim().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Activity Name")
                    .errorMessage(ActivitiesErrorMessages.ACTIVITY_NAME_MAX_LENGTH)
                    .rejectedValue(rowData.getActivityName())
                    .build());
        }

        // Validate Activity Category length (optional field)
        if (rowData.getActivityCategory() != null && !rowData.getActivityCategory().trim().isEmpty()) {
            if (rowData.getActivityCategory().trim().length() > 100) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Activity Category")
                        .errorMessage(ActivitiesErrorMessages.ACTIVITY_CATEGORY_MAX_LENGTH)
                        .rejectedValue(rowData.getActivityCategory())
                        .build());
            }
        }

        // Validate Activity Description length (optional field)
        if (rowData.getActivityDescription() != null && !rowData.getActivityDescription().trim().isEmpty()) {
            if (rowData.getActivityDescription().trim().length() > 5000) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Activity Description")
                        .errorMessage(ActivitiesErrorMessages.ACTIVITY_DESCRIPTION_MAX_LENGTH)
                        .rejectedValue(rowData.getActivityDescription())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(ActivitiesBulkUploadDto rowData) {
        if (rowData.getActivityName() == null || rowData.getActivityName().trim().isEmpty()) {
            return false;
        }
        
        String trimmedName = rowData.getActivityName().trim();
        boolean exists = activitiesRepository.existsByActivityNameIgnoreCase(trimmedName);
        
        if (exists) {
            log.debug("Duplicate Activities found: {}", trimmedName);
        }
        
        return exists;
    }
}
