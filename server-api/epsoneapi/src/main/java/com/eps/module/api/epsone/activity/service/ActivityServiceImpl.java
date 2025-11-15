package com.eps.module.api.epsone.activity.service;

import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activity.dto.ActivityBulkUploadDto;
import com.eps.module.api.epsone.activity.dto.ActivityErrorReportDto;
import com.eps.module.api.epsone.activity.dto.ActivityRequestDto;
import com.eps.module.api.epsone.activity.dto.ActivityResponseDto;
import com.eps.module.api.epsone.activity.mapper.ActivityMapper;
import com.eps.module.api.epsone.activity.processor.ActivityBulkUploadProcessor;
import com.eps.module.api.epsone.activity.repository.ActivityRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl extends BaseBulkUploadService<ActivityBulkUploadDto, Activity> implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final com.eps.module.api.epsone.activities.repository.ActivitiesRepository activitiesRepository;
    private final ActivityBulkUploadProcessor activityBulkUploadProcessor;

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<ActivityBulkUploadDto, Activity> getProcessor() {
        return activityBulkUploadProcessor;
    }

    @Override
    public Class<ActivityBulkUploadDto> getBulkUploadDtoClass() {
        return ActivityBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Activity";
    }

    @Override
    public List<Activity> getAllEntitiesForExport() {
        return activityRepository.findAllForExport();
    }

    @Override
    public Function<Activity, ActivityBulkUploadDto> getEntityToDtoMapper() {
        return activity -> ActivityBulkUploadDto.builder()
                .activityName(activity.getActivityName())
                .activityDescription(activity.getActivityDescription())
                .build();
    }

    @Override
    public ActivityErrorReportDto buildErrorReportDto(BulkUploadErrorDto errorDto) {
        return ActivityErrorReportDto.builder()
                .rowNumber(errorDto.getRowNumber())
                .activityName((String) errorDto.getRowData().get("Activity Name"))
                .activityDescription((String) errorDto.getRowData().get("Activity Description"))
                .error(errorDto.getErrorMessage())
                .build();
    }

    @Override
    public Class<ActivityErrorReportDto> getErrorReportDtoClass() {
        return ActivityErrorReportDto.class;
    }

    // ========== CRUD Methods ==========

    @Override
    @Transactional
    public ActivityResponseDto createActivity(ActivityRequestDto requestDto) {
        log.info("Creating new activity: {}", requestDto.getActivityName());

        if (activityRepository.existsByActivityNameIgnoreCase(requestDto.getActivityName())) {
            throw new IllegalArgumentException("Activity with name '" + requestDto.getActivityName() + "' already exists");
        }

        Activity activity = activityMapper.toEntity(requestDto);
        Activity savedActivity = activityRepository.save(activity);

        log.info("Activity created successfully with ID: {}", savedActivity.getId());
        return activityMapper.toResponseDto(savedActivity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponseDto> getAllActivities(Pageable pageable) {
        log.info("Fetching all activities with pagination");
        Page<Activity> activities = activityRepository.findAll(pageable);
        return activities.map(activityMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponseDto> searchActivities(String searchTerm, Pageable pageable) {
        log.info("Searching activities with term: {}", searchTerm);
        Page<Activity> activities = activityRepository.searchActivities(searchTerm, pageable);
        return activities.map(activityMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getActivityList() {
        log.info("Fetching all activities as list");
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
                .map(activityMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponseDto getActivityById(Long id) {
        log.info("Fetching activity by ID: {}", id);
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with ID: " + id));
        return activityMapper.toResponseDto(activity);
    }

    @Override
    @Transactional
    public ActivityResponseDto updateActivity(Long id, ActivityRequestDto activityRequestDto) {
        log.info("Updating activity with ID: {}", id);

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with ID: " + id));

        if (activityRepository.existsByActivityNameAndIdNot(activityRequestDto.getActivityName(), id)) {
            throw new IllegalArgumentException("Activity with name '" + activityRequestDto.getActivityName() + "' already exists");
        }

        activityMapper.updateEntityFromDto(activityRequestDto, activity);
        Activity updatedActivity = activityRepository.save(activity);

        log.info("Activity updated successfully with ID: {}", updatedActivity.getId());
        return activityMapper.toResponseDto(updatedActivity);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        log.info("Deleting activity with ID: {}", id);

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with ID: " + id));

        // Check if activity is being used by Activities records
        List<com.eps.module.activity.Activities> dependentActivities = activitiesRepository.findByActivityId((long) id.intValue());
        if (!dependentActivities.isEmpty()) {
            String activityNames = dependentActivities.stream()
                .limit(3) // Show max 3 names
                .map(com.eps.module.activity.Activities::getActivityName)
                .collect(Collectors.joining(", "));
            
            // If there are more than 3, add "and X more"
            if (dependentActivities.size() > 3) {
                activityNames += " and " + (dependentActivities.size() - 3) + " more";
            }
            
            throw new IllegalStateException(
                String.format("Activity '%s' cannot be deleted because it is being used by the following Activities: %s. Please remove these dependencies first.",
                    activity.getActivityName(), activityNames)
            );
        }

        activityRepository.deleteById(id);
        log.info("Activity deleted successfully with ID: {}", id);
    }
}
