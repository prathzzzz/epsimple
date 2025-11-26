package com.eps.module.api.epsone.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDto {

    private Long id;
    private String locationName;
    private String address;
    private String district;
    private Long cityId;
    private String cityName;
    private String stateName;
    private String pincode;
    private String region;
    private String zone;
    private String longitude;
    private String latitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
