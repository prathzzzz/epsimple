package com.eps.module.api.epsone.activity.processor;

import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activity.dto.ActivityBulkUploadDto;
import com.eps.module.api.epsone.activity.repository.ActivityRepository;
import com.eps.module.api.epsone.activity.validator.ActivityBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityBulkUploadProcessor extends BulkUploadProcessor<ActivityBulkUploadDto, Activity> {

    private final ActivityRepository activityRepository;
    private final ActivityBulkUploadValidator activityBulkUploadValidator;

    @Override
    public BulkRowValidator<ActivityBulkUploadDto> getValidator() {
        return activityBulkUploadValidator;
    }

    @Override
    @Transactional
    public Activity convertToEntity(ActivityBulkUploadDto dto) {
        return Activity.builder()
                .activityName(dto.getActivityName() != null ? dto.getActivityName().trim() : null)
                .activityDescription(dto.getActivityDescription() != null && !dto.getActivityDescription().trim().isEmpty() 
                        ? dto.getActivityDescription().trim() 
                        : null)
                .build();
    }

    @Override
    public void saveEntity(Activity entity) {
        activityRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(ActivityBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Activity Name", dto.getActivityName());
        rowData.put("Activity Description", dto.getActivityDescription());
        return rowData;
    }

    @Override
    public boolean isEmptyRow(ActivityBulkUploadDto dto) {
        return (dto.getActivityName() == null || dto.getActivityName().trim().isEmpty()) &&
               (dto.getActivityDescription() == null || dto.getActivityDescription().trim().isEmpty());
    }
}
