package com.eps.module.api.epsone.paymentmethod.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequestDto {

    @NotBlank(message = "Method name is required")
    @Size(max = 50, message = "Method name must not exceed 50 characters")
    private String methodName;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;
}
