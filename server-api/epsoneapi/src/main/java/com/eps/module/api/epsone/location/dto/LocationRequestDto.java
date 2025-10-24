package com.eps.module.api.epsone.location.dto;

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
public class LocationRequestDto {

    @NotBlank(message = "Location name is required")
    @Size(max = 255, message = "Location name cannot exceed 255 characters")
    private String locationName;

    @Size(max = 5000, message = "Address cannot exceed 5000 characters")
    private String address;

    @Size(max = 100, message = "District name cannot exceed 100 characters")
    private String district;

    @NotNull(message = "City is required")
    private Long cityId;

    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    private String pincode;

    @Size(max = 50, message = "Region cannot exceed 50 characters")
    private String region;

    @Size(max = 50, message = "Zone cannot exceed 50 characters")
    private String zone;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Digits(integer = 3, fraction = 8, message = "Longitude must have max 3 integer digits and 8 decimal digits")
    private BigDecimal longitude;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Digits(integer = 2, fraction = 8, message = "Latitude must have max 2 integer digits and 8 decimal digits")
    private BigDecimal latitude;
}
