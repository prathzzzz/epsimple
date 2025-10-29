package com.eps.module.api.epsone.sitecode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratedSiteCodeDto {

    private String siteCode;
    private Long projectId;
    private String projectName;
    private String projectCode;
    private Long stateId;
    private String stateName;
    private String stateCode;
    private Integer currentSequence;
    private Integer nextSequence;
}
