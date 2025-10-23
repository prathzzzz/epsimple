package com.eps.module.api.epsone.city.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityResponseDto {

    private Long id;
    private String cityName;
    private String cityCode;
    private Long stateId;
    private String stateName;
    private String stateCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
