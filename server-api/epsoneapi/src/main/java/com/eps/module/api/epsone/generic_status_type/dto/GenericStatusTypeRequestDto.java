package com.eps.module.api.epsone.generic_status_type.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericStatusTypeRequestDto {

    @NotBlank(message = "Status name is required")
    @Size(max = 100, message = "Status name must not exceed 100 characters")
    private String statusName;

    @Size(max = 20, message = "Status code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z ]*$", message = "Status code must contain only uppercase letters and spaces")
    private String statusCode;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
    
    // Automatically uppercase status code when set
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode != null ? statusCode.toUpperCase().trim() : null;
    }
}
