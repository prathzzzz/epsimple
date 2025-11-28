package com.eps.module.api.epsone.location.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String pincode;

    @Size(max = 50, message = "Region cannot exceed 50 characters")
    private String region;

    @Size(max = 50, message = "Zone cannot exceed 50 characters")
    private String zone;

    @Size(max = 50, message = "Longitude cannot exceed 50 characters")
    private String longitude;

    @Size(max = 50, message = "Latitude cannot exceed 50 characters")
    private String latitude;
}
