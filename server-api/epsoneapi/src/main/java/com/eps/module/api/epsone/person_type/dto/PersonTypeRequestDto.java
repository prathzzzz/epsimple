package com.eps.module.api.epsone.person_type.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonTypeRequestDto {
    
    @NotBlank(message = "Person type name is required")
    @Size(max = 100, message = "Person type name must not exceed 100 characters")
    private String typeName;
    
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
