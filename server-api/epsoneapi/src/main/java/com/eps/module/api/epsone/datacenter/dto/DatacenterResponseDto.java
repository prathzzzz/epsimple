package com.eps.module.api.epsone.datacenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatacenterResponseDto {

    private Long id;
    private String datacenterName;
    private String datacenterCode;
    private String datacenterType;
    private Long locationId;
    private String locationName;
    private String cityName;
    private String stateName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
