package com.eps.module.api.epsone.activity_work_remarks.service;

import com.eps.module.activity.ActivityWork;
import com.eps.module.activity.ActivityWorkRemarks;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.activity_work_remarks.dto.ActivityWorkRemarksRequestDto;
import com.eps.module.api.epsone.activity_work_remarks.dto.ActivityWorkRemarksResponseDto;
import com.eps.module.api.epsone.activity_work_remarks.mapper.ActivityWorkRemarksMapper;
import com.eps.module.api.epsone.activity_work_remarks.repository.ActivityWorkRemarksRepository;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
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
public class ActivityWorkRemarksServiceImpl implements ActivityWorkRemarksService {

    private final ActivityWorkRemarksRepository remarksRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final ActivityWorkRemarksMapper mapper;

    @Override
    @Transactional
    public ActivityWorkRemarksResponseDto createRemark(ActivityWorkRemarksRequestDto requestDto) {
        log.info("Creating remark for activity work ID: {}", requestDto.getActivityWorkId());

        // Validate activity work exists
        ActivityWork activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Activity work not found with id: " + requestDto.getActivityWorkId()));

        ActivityWorkRemarks remark = mapper.toEntity(requestDto, activityWork);
        ActivityWorkRemarks savedRemark = remarksRepository.save(remark);

        log.info("Successfully created remark with ID: {} for activity work ID: {}", 
                savedRemark.getId(), requestDto.getActivityWorkId());

        return mapper.toDto(savedRemark);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityWorkRemarksResponseDto> getRemarksByActivityWorkId(Long activityWorkId, Pageable pageable) {
        log.info("Fetching remarks for activity work ID: {} with pagination", activityWorkId);

        // Validate activity work exists
        if (!activityWorkRepository.existsById(activityWorkId)) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.ACTIVITY_WORK_NOT_FOUND, activityWorkId));
        }

        return remarksRepository.findByActivityWorkId(activityWorkId, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityWorkRemarksResponseDto> getAllRemarksByActivityWorkId(Long activityWorkId) {
        log.info("Fetching all remarks for activity work ID: {}", activityWorkId);

        // Validate activity work exists
        if (!activityWorkRepository.existsById(activityWorkId)) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.ACTIVITY_WORK_NOT_FOUND, activityWorkId));
        }

        return remarksRepository.findAllByActivityWorkId(activityWorkId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityWorkRemarksResponseDto getRemarkById(Long id) {
        log.info("Fetching remark with ID: {}", id);

        ActivityWorkRemarks remark = remarksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Remark not found with id: " + id));

        return mapper.toDto(remark);
    }

    @Override
    @Transactional
    public ActivityWorkRemarksResponseDto updateRemark(Long id, ActivityWorkRemarksRequestDto requestDto) {
        log.info("Updating remark with ID: {}", id);

        ActivityWorkRemarks existingRemark = remarksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Remark not found with id: " + id));

        // Update only the comment and commentedBy fields
        mapper.updateEntityFromDto(requestDto, existingRemark);
        ActivityWorkRemarks updatedRemark = remarksRepository.save(existingRemark);

        log.info("Successfully updated remark with ID: {}", id);

        return mapper.toDto(updatedRemark);
    }

    @Override
    @Transactional
    public void deleteRemark(Long id) {
        log.info("Deleting remark with ID: {}", id);

        ActivityWorkRemarks remark = remarksRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Remark not found with id: " + id));

        remarksRepository.deleteById(id);

        log.info("Successfully deleted remark with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countRemarksByActivityWorkId(Long activityWorkId) {
        log.info("Counting remarks for activity work ID: {}", activityWorkId);
        return remarksRepository.countByActivityWorkId(activityWorkId);
    }
}
