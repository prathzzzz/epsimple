package com.eps.module.api.epsone.activity.service;

import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activity.dto.ActivityBulkUploadDto;
import com.eps.module.api.epsone.activity.dto.ActivityRequestDto;
import com.eps.module.api.epsone.activity.dto.ActivityResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService extends BulkUploadService<ActivityBulkUploadDto, Activity> {

    ActivityResponseDto createActivity(ActivityRequestDto activityRequestDto);

    Page<ActivityResponseDto> getAllActivities(Pageable pageable);

    Page<ActivityResponseDto> searchActivities(String searchTerm, Pageable pageable);

    List<ActivityResponseDto> getActivityList();

    ActivityResponseDto getActivityById(Long id);

    ActivityResponseDto updateActivity(Long id, ActivityRequestDto activityRequestDto);

    void deleteActivity(Long id);
}
