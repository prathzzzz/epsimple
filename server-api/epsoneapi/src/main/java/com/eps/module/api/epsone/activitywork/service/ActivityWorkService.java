package com.eps.module.api.epsone.activitywork.service;

import com.eps.module.api.epsone.activitywork.dto.ActivityWorkRequestDto;
import com.eps.module.api.epsone.activitywork.dto.ActivityWorkResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityWorkService {

    ActivityWorkResponseDto createActivityWork(ActivityWorkRequestDto requestDto);

    Page<ActivityWorkResponseDto> getAllActivityWorks(int page, int size, String sortBy, String sortOrder);

    Page<ActivityWorkResponseDto> searchActivityWorks(String searchTerm, int page, int size, String sortBy, String sortOrder);

    List<ActivityWorkResponseDto> getAllActivityWorksList();

    ActivityWorkResponseDto getActivityWorkById(Long id);

    ActivityWorkResponseDto updateActivityWork(Long id, ActivityWorkRequestDto requestDto);

    void deleteActivityWork(Long id);
}
