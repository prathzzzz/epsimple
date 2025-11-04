package com.eps.module.api.epsone.activity_work_remarks.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityWorkRemarksResponseDto {

    private Long id;
    private Long activityWorkId;
    private String comment;
    private LocalDateTime commentedOn;
    private Long commentedBy;
    private String commentedByName; // For future user integration
}
