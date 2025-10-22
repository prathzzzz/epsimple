package com.eps.module.api.epsone.activity.service;

import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activity.dto.ActivityRequestDto;
import com.eps.module.api.epsone.activity.dto.ActivityResponseDto;
import com.eps.module.api.epsone.activity.mapper.ActivityMapper;
import com.eps.module.api.epsone.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional
    public ActivityResponseDto createActivity(ActivityRequestDto activityRequestDto) {
        log.info("Creating new activity: {}", activityRequestDto.getActivityName());

        if (activityRepository.existsByActivityNameIgnoreCase(activityRequestDto.getActivityName())) {
            throw new IllegalArgumentException("Activity with name '" + activityRequestDto.getActivityName() + "' already exists");
        }

        Activity activity = activityMapper.toEntity(activityRequestDto);
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

        if (!activityRepository.existsById(id)) {
            throw new IllegalArgumentException("Activity not found with ID: " + id);
        }

        activityRepository.deleteById(id);
        log.info("Activity deleted successfully with ID: {}", id);
    }
}
