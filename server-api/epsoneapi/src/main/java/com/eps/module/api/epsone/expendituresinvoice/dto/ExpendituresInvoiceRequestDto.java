package com.eps.module.api.epsone.expendituresinvoice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpendituresInvoiceRequestDto {

    @NotNull(message = "Cost item ID is required")
    private Long costItemId;

    @NotNull(message = "Invoice ID is required")
    private Long invoiceId;

    @NotNull(message = "Managed project ID is required")
    private Long managedProjectId;

    private LocalDate incurredDate;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
