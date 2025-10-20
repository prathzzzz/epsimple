package com.eps.module.api.epsone.bank.dto;

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
public class BankRequestDto {

    @NotBlank(message = "Bank name is required")
    @Size(max = 255, message = "Bank name cannot exceed 255 characters")
    private String bankName;

    @Size(max = 10, message = "RBI bank code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "RBI bank code can only contain letters and numbers")
    private String rbiBankCode;

    @Size(max = 10, message = "EPS bank code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "EPS bank code can only contain letters, numbers, hyphens and underscores")
    private String epsBankCode;

    @Size(max = 10, message = "Alternate bank code cannot exceed 10 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Alternate bank code can only contain letters, numbers, hyphens and underscores")
    private String bankCodeAlt;

    @Size(max = 500, message = "Bank logo URL cannot exceed 500 characters")
    private String bankLogo;
}
