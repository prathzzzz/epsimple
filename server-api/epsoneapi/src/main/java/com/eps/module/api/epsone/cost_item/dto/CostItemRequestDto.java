package com.eps.module.api.epsone.cost_item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostItemRequestDto {

    @NotNull(message = "Cost type ID is required")
    private Long costTypeId;

    @NotBlank(message = "Cost item for is required")
    @Size(max = 255, message = "Cost item for must not exceed 255 characters")
    private String costItemFor;

    @Size(max = 1000, message = "Item description must not exceed 1000 characters")
    private String itemDescription;
}
