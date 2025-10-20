package com.eps.module.api.epsone.state.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateRequestDto {

    @NotBlank(message = "State name is required")
    @Size(max = 100, message = "State name cannot exceed 100 characters")
    private String stateName;

    @NotBlank(message = "State code is required")
    @Size(min = 2, max = 10, message = "State code must be between 2 and 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "State code can only contain letters, numbers, hyphens and underscores")
    private String stateCode;

    @Size(max = 10, message = "Alternate state code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Alternate state code can only contain letters, numbers, hyphens and underscores")
    private String stateCodeAlt;
}
