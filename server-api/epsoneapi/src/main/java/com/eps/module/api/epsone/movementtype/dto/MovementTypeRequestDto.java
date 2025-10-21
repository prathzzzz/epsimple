package com.eps.module.api.epsone.movementtype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementTypeRequestDto {
    
    @NotBlank(message = "Movement type is required")
    @Size(max = 100, message = "Movement type must not exceed 100 characters")
    private String movementType;
    
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
