package com.eps.module.api.epsone.genericstatustype.dto;

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
    @Pattern(regexp = "^[A-Z0-9_-]*$", message = "Status code must be uppercase alphanumeric with hyphens/underscores")
    private String statusCode;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
