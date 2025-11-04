package com.eps.module.api.epsone.activity_work_remarks.service;

import com.eps.module.api.epsone.activity_work_remarks.dto.ActivityWorkRemarksRequestDto;
import com.eps.module.api.epsone.activity_work_remarks.dto.ActivityWorkRemarksResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityWorkRemarksService {
    
    ActivityWorkRemarksResponseDto createRemark(ActivityWorkRemarksRequestDto requestDto);
    
    Page<ActivityWorkRemarksResponseDto> getRemarksByActivityWorkId(Long activityWorkId, Pageable pageable);
    
    List<ActivityWorkRemarksResponseDto> getAllRemarksByActivityWorkId(Long activityWorkId);
    
    ActivityWorkRemarksResponseDto getRemarkById(Long id);
    
    ActivityWorkRemarksResponseDto updateRemark(Long id, ActivityWorkRemarksRequestDto requestDto);
    
    void deleteRemark(Long id);
    
    long countRemarksByActivityWorkId(Long activityWorkId);
}
