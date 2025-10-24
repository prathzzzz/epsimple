package com.eps.module.api.epsone.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponseDto {

    private Long id;
    private Long vendorTypeId;
    private String vendorTypeName;
    private String vendorCategoryName;
    private Long vendorDetailsId;
    private String vendorName;  // Person full name
    private String vendorEmail;
    private String vendorContact;
    private String vendorCodeAlt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
