package com.eps.module.api.epsone.activities.service;

import com.eps.module.activity.Activities;
import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activities.dto.ActivitiesBulkUploadDto;
import com.eps.module.api.epsone.activities.dto.ActivitiesErrorReportDto;
import com.eps.module.api.epsone.activities.dto.ActivitiesRequestDto;
import com.eps.module.api.epsone.activities.dto.ActivitiesResponseDto;
import com.eps.module.api.epsone.activities.mapper.ActivitiesMapper;
import com.eps.module.api.epsone.activities.processor.ActivitiesBulkUploadProcessor;
import com.eps.module.api.epsone.activities.repository.ActivitiesRepository;
import com.eps.module.api.epsone.activity.repository.ActivityRepository;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
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
public class ActivitiesServiceImpl extends BaseBulkUploadService<ActivitiesBulkUploadDto, Activities> implements ActivitiesService {

    private final ActivitiesRepository activitiesRepository;
    private final ActivityRepository activityRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final ActivitiesMapper activitiesMapper;
    private final ActivitiesBulkUploadProcessor activitiesBulkUploadProcessor;

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<ActivitiesBulkUploadDto, Activities> getProcessor() {
        return activitiesBulkUploadProcessor;
    }

    @Override
    public Class<ActivitiesBulkUploadDto> getBulkUploadDtoClass() {
        return ActivitiesBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Activities";
    }

    @Override
    public List<Activities> getAllEntitiesForExport() {
        return activitiesRepository.findAllForExport();
    }

    @Override
    public Function<Activities, ActivitiesBulkUploadDto> getEntityToDtoMapper() {
        return activities -> ActivitiesBulkUploadDto.builder()
                .masterActivityName(activities.getActivity() != null ? activities.getActivity().getActivityName() : "")
                .activityName(activities.getActivityName())
                .activityCategory(activities.getActivityCategory())
                .activityDescription(activities.getActivityDescription())
                .build();
    }

    @Override
    public ActivitiesErrorReportDto buildErrorReportDto(BulkUploadErrorDto errorDto) {
        return ActivitiesErrorReportDto.builder()
                .rowNumber(errorDto.getRowNumber())
                .masterActivityName((String) errorDto.getRowData().get("Activity Name (Master)"))
                .activityName((String) errorDto.getRowData().get("Activity Name"))
                .activityCategory((String) errorDto.getRowData().get("Activity Category"))
                .activityDescription((String) errorDto.getRowData().get("Activity Description"))
                .error(errorDto.getErrorMessage())
                .build();
    }

    @Override
    public Class<ActivitiesErrorReportDto> getErrorReportDtoClass() {
        return ActivitiesErrorReportDto.class;
    }

    // ========== CRUD Methods ==========

    @Override
    @Transactional
    public ActivitiesResponseDto createActivities(ActivitiesRequestDto activitiesRequestDto) {
        log.info("Creating new activities: {}", activitiesRequestDto.getActivityName());

        if (activitiesRepository.existsByActivityNameIgnoreCase(activitiesRequestDto.getActivityName())) {
            throw new IllegalArgumentException("Activities with name '" + activitiesRequestDto.getActivityName() + "' already exists");
        }

        Activity activity = activityRepository.findById(activitiesRequestDto.getActivityId())
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with ID: " + activitiesRequestDto.getActivityId()));

        Activities activities = activitiesMapper.toEntityWithActivity(activitiesRequestDto, activity);
        Activities savedActivities = activitiesRepository.save(activities);

        log.info("Activities created successfully with ID: {}", savedActivities.getId());
        return activitiesMapper.toResponseDto(savedActivities);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivitiesResponseDto> getAllActivities(Pageable pageable) {
        log.info("Fetching all activities with pagination");
        Page<Activities> activities = activitiesRepository.findAll(pageable);
        return activities.map(activitiesMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivitiesResponseDto> searchActivities(String searchTerm, Pageable pageable) {
        log.info("Searching activities with term: {}", searchTerm);
        Page<Activities> activities = activitiesRepository.searchActivities(searchTerm, pageable);
        return activities.map(activitiesMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivitiesResponseDto> getActivitiesList() {
        log.info("Fetching all activities as list");
        List<Activities> activities = activitiesRepository.findAll();
        return activities.stream()
                .map(activitiesMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ActivitiesResponseDto getActivitiesById(Long id) {
        log.info("Fetching activities by ID: {}", id);
        Activities activities = activitiesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activities not found with ID: " + id));
        return activitiesMapper.toResponseDto(activities);
    }

    @Override
    @Transactional
    public ActivitiesResponseDto updateActivities(Long id, ActivitiesRequestDto activitiesRequestDto) {
        log.info("Updating activities with ID: {}", id);

        Activities activities = activitiesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Activities not found with ID: " + id));

        if (activitiesRepository.existsByActivityNameAndIdNot(activitiesRequestDto.getActivityName(), id)) {
            throw new IllegalArgumentException("Activities with name '" + activitiesRequestDto.getActivityName() + "' already exists");
        }

        Activity activity = activityRepository.findById(activitiesRequestDto.getActivityId())
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with ID: " + activitiesRequestDto.getActivityId()));

        activitiesMapper.updateEntityFromDto(activitiesRequestDto, activities);
        activities.setActivity(activity);
        Activities updatedActivities = activitiesRepository.save(activities);

        log.info("Activities updated successfully with ID: {}", updatedActivities.getId());
        return activitiesMapper.toResponseDto(updatedActivities);
    }

    @Override
    @Transactional
    public void deleteActivities(Long id) {
        log.info("Deleting activities with ID: {}", id);

        if (!activitiesRepository.existsById(id)) {
            throw new IllegalArgumentException("Activities not found with ID: " + id);
        }

        // Check for dependencies - activity work orders
        long activityWorkCount = activityWorkRepository.countByActivitiesId(id);
        if (activityWorkCount > 0) {
            throw new IllegalStateException("Cannot delete activities as it has " + activityWorkCount + " associated activity work orders");
        }

        activitiesRepository.deleteById(id);
        log.info("Activities deleted successfully with ID: {}", id);
    }
}
