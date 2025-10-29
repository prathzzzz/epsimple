package com.eps.module.api.epsone.sitecode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteCodeGeneratorResponseDto {

    private Long id;
    private Long projectId;
    private String projectName;
    private String projectCode;
    private Long stateId;
    private String stateName;
    private String stateCode;
    private Integer maxSeqDigit;
    private Integer runningSeq;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
