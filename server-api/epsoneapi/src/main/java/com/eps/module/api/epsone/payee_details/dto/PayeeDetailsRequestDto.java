package com.eps.module.api.epsone.payee_details.dto;

import jakarta.validation.constraints.NotBlank;
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
public class PayeeDetailsRequestDto {

    @NotBlank(message = "Payee name is required")
    @Size(max = 255, message = "Payee name cannot exceed 255 characters")
    private String payeeName;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "PAN number must be in format: AAAAA9999A (e.g., ABCDE1234F)")
    @Size(max = 255, message = "PAN number cannot exceed 255 characters")
    private String panNumber;

    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar number must be exactly 12 digits")
    @Size(max = 255, message = "Aadhaar number cannot exceed 255 characters")
    private String aadhaarNumber;

    private Long bankId;

    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "IFSC code must be in format: AAAA0999999 (e.g., SBIN0001234)")
    @Size(max = 20, message = "IFSC code cannot exceed 20 characters")
    private String ifscCode;

    @Size(max = 255, message = "Beneficiary name cannot exceed 255 characters")
    private String beneficiaryName;

    @Pattern(regexp = "^[0-9]{9,18}$", message = "Account number must be between 9 to 18 digits")
    @Size(max = 255, message = "Account number cannot exceed 255 characters")
    private String accountNumber;
}
