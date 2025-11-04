package com.eps.module.api.epsone.site_code.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteCodeGeneratorRequestDto {

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "State ID is required")
    private Long stateId;

    @Min(value = 1, message = "Max sequence digit must be at least 1")
    private Integer maxSeqDigit;

    @Min(value = 1, message = "Running sequence must be at least 1")
    private Integer runningSeq;
}
