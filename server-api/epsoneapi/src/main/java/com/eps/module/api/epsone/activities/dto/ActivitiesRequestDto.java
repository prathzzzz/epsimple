package com.eps.module.api.epsone.activities.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiesRequestDto {

    @NotNull(message = "Activity ID is required")
    private Long activityId;

    @NotBlank(message = "Activity name is required")
    @Size(max = 100, message = "Activity name must not exceed 100 characters")
    private String activityName;

    @Size(max = 100, message = "Activity category must not exceed 100 characters")
    private String activityCategory;

    @Size(max = 5000, message = "Activity description must not exceed 5000 characters")
    private String activityDescription;
}
