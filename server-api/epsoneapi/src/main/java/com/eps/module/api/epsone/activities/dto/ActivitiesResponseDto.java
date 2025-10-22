package com.eps.module.api.epsone.activities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesResponseDto {

    private Long id;
    private Long activityId;
    private String activityName;
    private String activityCategory;
    private String activityDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
