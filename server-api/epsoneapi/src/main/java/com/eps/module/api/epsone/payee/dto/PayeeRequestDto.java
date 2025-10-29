package com.eps.module.api.epsone.payee.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayeeRequestDto {

    @NotNull(message = "Payee type is required")
    private Long payeeTypeId;

    @NotNull(message = "Payee details are required")
    private Long payeeDetailsId;

    private Long vendorId;

    private Long landlordId;
}
