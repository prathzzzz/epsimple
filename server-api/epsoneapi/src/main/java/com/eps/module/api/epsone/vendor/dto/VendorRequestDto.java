package com.eps.module.api.epsone.vendor.dto;

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
public class VendorRequestDto {

    @NotNull(message = "Vendor type ID is required")
    private Long vendorTypeId;

    @NotNull(message = "Vendor details ID (Person Details) is required")
    private Long vendorDetailsId;

    @Size(max = 10, message = "Vendor code alternate must not exceed 10 characters")
    @Pattern(regexp = "^[A-Z0-9_-]*$", message = "Vendor code alternate must contain only uppercase letters, numbers, hyphens, and underscores")
    private String vendorCodeAlt;
}
