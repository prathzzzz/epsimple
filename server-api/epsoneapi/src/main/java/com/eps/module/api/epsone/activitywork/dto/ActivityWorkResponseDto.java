package com.eps.module.api.epsone.activitywork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityWorkResponseDto {

    private Long id;
    private Long activitiesId;
    private String activitiesName;
    private Long vendorId;
    private String vendorName;
    private String vendorOrderNumber;
    private LocalDate workOrderDate;
    private LocalDate workStartDate;
    private LocalDate workCompletionDate;
    private Long statusTypeId;
    private String statusTypeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
