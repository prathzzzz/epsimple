package com.eps.module.api.epsone.managedproject.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ManagedProjectRequestDto {

    @NotNull(message = "Bank ID is required")
    private Long bankId;

    @Size(max = 50, message = "Project type cannot exceed 50 characters")
    private String projectType;

    @NotBlank(message = "Project name is required")
    @Size(max = 255, message = "Project name cannot exceed 255 characters")
    private String projectName;

    @Size(max = 50, message = "Project code cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]*$", message = "Project code can only contain letters, numbers, hyphens and underscores")
    private String projectCode;

    @Size(max = 5000, message = "Project description cannot exceed 5000 characters")
    private String projectDescription;
}
