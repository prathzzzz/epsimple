package com.eps.module.api.epsone.managedproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagedProjectResponseDto {

    private Long id;
    private Long bankId;
    private String bankName;
    private String projectType;
    private String projectName;
    private String projectCode;
    private String projectDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
