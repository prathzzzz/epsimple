package com.eps.module.api.epsone.activityworkremarks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityWorkRemarksRequestDto {

    @NotNull(message = "Activity work ID is required")
    private Long activityWorkId;

    @NotBlank(message = "Comment is required")
    private String comment;

    private Long commentedBy;
}
