package com.eps.module.api.epsone.landlord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandlordResponseDto {

    private Long id;
    private Long landlordDetailsId;
    private String landlordName;  // Person full name
    private String landlordEmail;
    private String landlordPhone;
    private BigDecimal rentSharePercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
