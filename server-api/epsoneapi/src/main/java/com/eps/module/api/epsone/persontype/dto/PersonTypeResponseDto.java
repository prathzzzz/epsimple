package com.eps.module.api.epsone.persontype.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonTypeResponseDto {
    private Long id;
    private String typeName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
