package com.eps.module.api.epsone.activity_work.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityWorkRequestDto {

    @NotNull(message = "Activity is required")
    private Long activitiesId;

    @NotNull(message = "Vendor is required")
    private Long vendorId;

    @Size(max = 100, message = "Vendor order number cannot exceed 100 characters")
    private String vendorOrderNumber;

    private LocalDate workOrderDate;

    private LocalDate workStartDate;

    private LocalDate workCompletionDate;

    @NotNull(message = "Status type is required")
    private Long statusTypeId;
}
