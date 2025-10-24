package com.eps.module.api.epsone.landlord.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandlordRequestDto {

    @NotNull(message = "Landlord details ID (Person Details) is required")
    private Long landlordDetailsId;

    @DecimalMin(value = "0.00", message = "Rent share percentage must be greater than or equal to 0")
    @DecimalMax(value = "100.00", message = "Rent share percentage must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "Rent share percentage must have at most 3 integer digits and 2 decimal digits")
    private BigDecimal rentSharePercentage;
}
