package com.eps.module.api.epsone.activities.service;

import com.eps.module.api.epsone.activities.dto.ActivitiesRequestDto;
import com.eps.module.api.epsone.activities.dto.ActivitiesResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivitiesService {

    ActivitiesResponseDto createActivities(ActivitiesRequestDto activitiesRequestDto);

    Page<ActivitiesResponseDto> getAllActivities(Pageable pageable);

    Page<ActivitiesResponseDto> searchActivities(String searchTerm, Pageable pageable);

    List<ActivitiesResponseDto> getActivitiesList();

    ActivitiesResponseDto getActivitiesById(Long id);

    ActivitiesResponseDto updateActivities(Long id, ActivitiesRequestDto activitiesRequestDto);

    void deleteActivities(Long id);
}
