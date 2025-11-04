package com.eps.module.api.epsone.site_activity_work_expenditure.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteActivityWorkExpenditureRequestDto {

    @NotNull(message = "Site ID is required")
    private Long siteId;

    @NotNull(message = "Activity Work ID is required")
    private Long activityWorkId;

    @NotNull(message = "Expenditures Invoice ID is required")
    private Long expendituresInvoiceId;
}
