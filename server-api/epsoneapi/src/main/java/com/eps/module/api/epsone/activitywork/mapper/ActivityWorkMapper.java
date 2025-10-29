package com.eps.module.api.epsone.activitywork.mapper;

import com.eps.module.activity.Activities;
import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activitywork.dto.ActivityWorkRequestDto;
import com.eps.module.api.epsone.activitywork.dto.ActivityWorkResponseDto;
import com.eps.module.status.GenericStatusType;
import com.eps.module.vendor.Vendor;
import org.springframework.stereotype.Component;

@Component
public class ActivityWorkMapper {

    public ActivityWork toEntity(ActivityWorkRequestDto dto, Activities activities, Vendor vendor, GenericStatusType statusType) {
        return ActivityWork.builder()
                .activities(activities)
                .vendor(vendor)
                .vendorOrderNumber(dto.getVendorOrderNumber())
                .workOrderDate(dto.getWorkOrderDate())
                .workStartDate(dto.getWorkStartDate())
                .workCompletionDate(dto.getWorkCompletionDate())
                .statusType(statusType)
                .build();
    }

    public void updateEntity(ActivityWork activityWork, ActivityWorkRequestDto dto, Activities activities, Vendor vendor, GenericStatusType statusType) {
        activityWork.setActivities(activities);
        activityWork.setVendor(vendor);
        activityWork.setVendorOrderNumber(dto.getVendorOrderNumber());
        activityWork.setWorkOrderDate(dto.getWorkOrderDate());
        activityWork.setWorkStartDate(dto.getWorkStartDate());
        activityWork.setWorkCompletionDate(dto.getWorkCompletionDate());
        activityWork.setStatusType(statusType);
    }

    public ActivityWorkResponseDto toDto(ActivityWork activityWork) {
        return ActivityWorkResponseDto.builder()
                .id(activityWork.getId())
                .activitiesId(activityWork.getActivities().getId())
                .activitiesName(activityWork.getActivities().getActivitiesName())
                .vendorId(activityWork.getVendor().getId())
                .vendorName(activityWork.getVendor().getVendorName())
                .vendorOrderNumber(activityWork.getVendorOrderNumber())
                .workOrderDate(activityWork.getWorkOrderDate())
                .workStartDate(activityWork.getWorkStartDate())
                .workCompletionDate(activityWork.getWorkCompletionDate())
                .statusTypeId(activityWork.getStatusType().getId())
                .statusTypeName(activityWork.getStatusType().getStatusTypeName())
                .createdAt(activityWork.getCreatedAt())
                .updatedAt(activityWork.getUpdatedAt())
                .build();
    }
}
