package com.eps.module.api.epsone.activity.validator;

import com.eps.module.api.epsone.activity.dto.ActivityBulkUploadDto;
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
public class ActivityBulkUploadValidator implements BulkRowValidator<ActivityBulkUploadDto> {

    private final ActivityRepository activityRepository;

    @Override
    public List<BulkUploadErrorDto> validate(ActivityBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Activity Name
        if (rowData.getActivityName() == null || rowData.getActivityName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Activity Name")
                    .errorMessage("Activity name is required")
                    .rejectedValue(rowData.getActivityName())
                    .build());
        } else if (rowData.getActivityName().trim().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Activity Name")
                    .errorMessage("Activity name cannot exceed 100 characters")
                    .rejectedValue(rowData.getActivityName())
                    .build());
        }

        // Validate Activity Description length (optional field)
        if (rowData.getActivityDescription() != null && !rowData.getActivityDescription().trim().isEmpty()) {
            if (rowData.getActivityDescription().trim().length() > 65535) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Activity Description")
                        .errorMessage("Activity description cannot exceed 65535 characters")
                        .rejectedValue(rowData.getActivityDescription())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(ActivityBulkUploadDto rowData) {
        if (rowData.getActivityName() == null || rowData.getActivityName().trim().isEmpty()) {
            return false;
        }
        
        String trimmedName = rowData.getActivityName().trim();
        boolean exists = activityRepository.existsByActivityNameIgnoreCase(trimmedName);
        
        if (exists) {
            log.debug("Duplicate Activity found: {}", trimmedName);
        }
        
        return exists;
    }
}
