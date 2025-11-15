package com.eps.module.api.epsone.activity_work.service;

import com.eps.module.activity.Activities;
import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activities.repository.ActivitiesRepository;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkBulkUploadDto;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkErrorReportDto;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkRequestDto;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkResponseDto;
import com.eps.module.api.epsone.activity_work.mapper.ActivityWorkMapper;
import com.eps.module.api.epsone.activity_work.processor.ActivityWorkBulkUploadProcessor;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.activity_work_remarks.repository.ActivityWorkRemarksRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.status.GenericStatusType;
import com.eps.module.vendor.Vendor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityWorkServiceImpl extends BaseBulkUploadService<ActivityWorkBulkUploadDto, ActivityWork> implements ActivityWorkService {

    private final ActivityWorkRepository activityWorkRepository;
    private final ActivitiesRepository activitiesRepository;
    private final VendorRepository vendorRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final ActivityWorkRemarksRepository activityWorkRemarksRepository;
    private final ActivityWorkMapper activityWorkMapper;
    private final ActivityWorkBulkUploadProcessor activityWorkBulkUploadProcessor;

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<ActivityWorkBulkUploadDto, ActivityWork> getProcessor() {
        return activityWorkBulkUploadProcessor;
    }

    @Override
    public Class<ActivityWorkBulkUploadDto> getBulkUploadDtoClass() {
        return ActivityWorkBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Activity Work";
    }

    @Override
    public List<ActivityWork> getAllEntitiesForExport() {
        return activityWorkRepository.findAllForExport();
    }

    @Override
    public Function<ActivityWork, ActivityWorkBulkUploadDto> getEntityToDtoMapper() {
        return activityWork -> {
            String activitiesName = activityWork.getActivities() != null 
                    ? activityWork.getActivities().getActivityName() : "";
            
            String vendorName = "";
            if (activityWork.getVendor() != null && activityWork.getVendor().getVendorDetails() != null) {
                String firstName = activityWork.getVendor().getVendorDetails().getFirstName();
                String lastName = activityWork.getVendor().getVendorDetails().getLastName();
                vendorName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
                vendorName = vendorName.trim();
            }
            
            String statusCode = activityWork.getStatusType() != null 
                    ? activityWork.getStatusType().getStatusCode() : "";

            return ActivityWorkBulkUploadDto.builder()
                    .activitiesName(activitiesName)
                    .vendorName(vendorName)
                    .vendorOrderNumber(activityWork.getVendorOrderNumber())
                    .workOrderDate(activityWork.getWorkOrderDate() != null ? activityWork.getWorkOrderDate().toString() : "")
                    .workStartDate(activityWork.getWorkStartDate() != null ? activityWork.getWorkStartDate().toString() : "")
                    .workCompletionDate(activityWork.getWorkCompletionDate() != null ? activityWork.getWorkCompletionDate().toString() : "")
                    .statusTypeCode(statusCode)
                    .build();
        };
    }

    @Override
    public ActivityWorkErrorReportDto buildErrorReportDto(BulkUploadErrorDto errorDto) {
        return ActivityWorkErrorReportDto.builder()
                .rowNumber(errorDto.getRowNumber())
                .activitiesName((String) errorDto.getRowData().get("Activities Name"))
                .vendorName((String) errorDto.getRowData().get("Vendor Name"))
                .vendorOrderNumber((String) errorDto.getRowData().get("Vendor Order Number"))
                .workOrderDate((String) errorDto.getRowData().get("Work Order Date"))
                .workStartDate((String) errorDto.getRowData().get("Work Start Date"))
                .workCompletionDate((String) errorDto.getRowData().get("Work Completion Date"))
                .statusTypeCode((String) errorDto.getRowData().get("Status Type Code"))
                .error(errorDto.getErrorMessage())
                .build();
    }

    @Override
    public Class<ActivityWorkErrorReportDto> getErrorReportDtoClass() {
        return ActivityWorkErrorReportDto.class;
    }

    // ========== CRUD Methods ==========

    @Override
    @Transactional
    public ActivityWorkResponseDto createActivityWork(ActivityWorkRequestDto requestDto) {
        log.info("Creating new activity work for activity ID: {}", requestDto.getActivitiesId());
        
        // Validate activity exists
        Activities activities = activitiesRepository.findById(requestDto.getActivitiesId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Activity not found with id: " + requestDto.getActivitiesId()));

        // Validate vendor exists
        Vendor vendor = vendorRepository.findById(requestDto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor not found with id: " + requestDto.getVendorId()));

        // Validate status type exists
        GenericStatusType statusType = genericStatusTypeRepository.findById(requestDto.getStatusTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status type not found with id: " + requestDto.getStatusTypeId()));

        ActivityWork activityWork = activityWorkMapper.toEntity(requestDto, activities, vendor, statusType);
        ActivityWork savedActivityWork = activityWorkRepository.save(activityWork);

        // Fetch with details for response
        ActivityWork activityWorkWithDetails = activityWorkRepository.findByIdWithDetails(savedActivityWork.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity work not found after save"));

        log.info("Activity work created successfully with ID: {}", savedActivityWork.getId());
        return activityWorkMapper.toDto(activityWorkWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityWorkResponseDto> getAllActivityWorks(int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching all activity works with pagination: page={}, size={}", page, size);
        Sort sort = sortOrder.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ActivityWork> activityWorkPage = activityWorkRepository.findAllWithDetails(pageable);
        return activityWorkPage.map(activityWorkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityWorkResponseDto> searchActivityWorks(String searchTerm, int page, int size, String sortBy, String sortOrder) {
        log.info("Searching activity works with keyword: {}", searchTerm);
        Sort sort = sortOrder.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ActivityWork> activityWorkPage = activityWorkRepository.searchActivityWorks(searchTerm, pageable);
        return activityWorkPage.map(activityWorkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityWorkResponseDto> getAllActivityWorksList() {
        log.info("Fetching all activity works as list");
        List<ActivityWork> activityWorks = activityWorkRepository.findAll(Sort.by("id").descending());
        return activityWorks.stream()
                .map(activityWorkMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityWorkResponseDto getActivityWorkById(Long id) {
        log.info("Fetching activity work with ID: {}", id);
        ActivityWork activityWork = activityWorkRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity work not found with id: " + id));
        return activityWorkMapper.toDto(activityWork);
    }

    @Override
    @Transactional
    public ActivityWorkResponseDto updateActivityWork(Long id, ActivityWorkRequestDto requestDto) {
        log.info("Updating activity work with ID: {}", id);
        
        ActivityWork activityWork = activityWorkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity work not found with id: " + id));

        // Validate activity exists
        Activities activities = activitiesRepository.findById(requestDto.getActivitiesId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Activity not found with id: " + requestDto.getActivitiesId()));

        // Validate vendor exists
        Vendor vendor = vendorRepository.findById(requestDto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vendor not found with id: " + requestDto.getVendorId()));

        // Validate status type exists
        GenericStatusType statusType = genericStatusTypeRepository.findById(requestDto.getStatusTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status type not found with id: " + requestDto.getStatusTypeId()));

        activityWorkMapper.updateEntity(activityWork, requestDto, activities, vendor, statusType);
        ActivityWork updatedActivityWork = activityWorkRepository.save(activityWork);

        // Fetch with details for response
        ActivityWork activityWorkWithDetails = activityWorkRepository.findByIdWithDetails(updatedActivityWork.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity work not found after update"));

        log.info("Activity work updated successfully with ID: {}", id);
        return activityWorkMapper.toDto(activityWorkWithDetails);
    }

    @Override
    @Transactional
    public void deleteActivityWork(Long id) {
        log.info("Deleting activity work with ID: {}", id);
        
        ActivityWork activityWork = activityWorkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity work not found with id: " + id));

        // Check if activity work has remarks
        long remarksCount = activityWorkRemarksRepository.countByActivityWorkId(id);
        if (remarksCount > 0) {
            throw new IllegalStateException("Cannot delete activity work. It has " + remarksCount + " remark(s) associated with it.");
        }

        activityWorkRepository.delete(activityWork);
        log.info("Activity work deleted successfully with ID: {}", id);
    }
}
