package com.eps.module.api.epsone.person_details.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDetailsResponseDto {

    private Long id;
    private Long personTypeId;
    private String personTypeName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String contactNumber;
    private String email;
    private String permanentAddress;
    private String correspondenceAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
