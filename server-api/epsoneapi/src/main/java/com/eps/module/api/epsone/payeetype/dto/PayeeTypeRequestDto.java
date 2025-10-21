package com.eps.module.api.epsone.payeetype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayeeTypeRequestDto {

    @NotBlank(message = "Payee type is required")
    @Size(max = 50, message = "Payee type must not exceed 50 characters")
    private String payeeType;

    @Size(max = 100, message = "Payee category must not exceed 100 characters")
    private String payeeCategory;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
