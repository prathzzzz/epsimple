package com.eps.module.api.epsone.city.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityRequestDto {

    @NotBlank(message = "City name is required")
    @Size(max = 100, message = "City name cannot exceed 100 characters")
    private String cityName;

    @Size(max = 10, message = "City code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]*$", message = "City code can only contain letters, numbers, hyphens and underscores")
    private String cityCode;

    @NotNull(message = "State ID is required")
    private Long stateId;
}
