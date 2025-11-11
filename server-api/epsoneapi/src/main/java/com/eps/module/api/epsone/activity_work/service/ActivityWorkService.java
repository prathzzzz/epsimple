package com.eps.module.api.epsone.activity_work.service;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkBulkUploadDto;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkRequestDto;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityWorkService extends BulkUploadService<ActivityWorkBulkUploadDto, ActivityWork> {

    ActivityWorkResponseDto createActivityWork(ActivityWorkRequestDto requestDto);

    Page<ActivityWorkResponseDto> getAllActivityWorks(int page, int size, String sortBy, String sortOrder);

    Page<ActivityWorkResponseDto> searchActivityWorks(String searchTerm, int page, int size, String sortBy, String sortOrder);

    List<ActivityWorkResponseDto> getAllActivityWorksList();

    ActivityWorkResponseDto getActivityWorkById(Long id);

    ActivityWorkResponseDto updateActivityWork(Long id, ActivityWorkRequestDto requestDto);

    void deleteActivityWork(Long id);
}
