package com.eps.module.api.epsone.managed_project.validator;

import com.eps.module.api.epsone.managed_project.dto.ManagedProjectBulkUploadDto;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ManagedProjectBulkUploadValidator implements BulkRowValidator<ManagedProjectBulkUploadDto> {

    private final ManagedProjectRepository managedProjectRepository;

    @Override
    public List<BulkUploadErrorDto> validate(ManagedProjectBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate project name
        if (dto.getProjectName() == null || dto.getProjectName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Project name is required")
                    .build());
        } else if (dto.getProjectName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Project name cannot exceed 255 characters")
                    .build());
        }

        // Validate project code
        if (dto.getProjectCode() != null && !dto.getProjectCode().trim().isEmpty()) {
            if (dto.getProjectCode().length() > 50) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Project code cannot exceed 50 characters")
                        .build());
            }
            if (!dto.getProjectCode().matches("^[A-Za-z0-9_-]+$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Project code can only contain letters, numbers, hyphens and underscores")
                        .build());
            }
            // Check for duplicate project code
            if (managedProjectRepository.existsByProjectCode(dto.getProjectCode())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Project code '" + dto.getProjectCode() + "' already exists")
                        .errorType("DUPLICATE")
                        .build());
            }
        }

        // Validate project type
        if (dto.getProjectType() != null && dto.getProjectType().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Project type cannot exceed 50 characters")
                    .build());
        }

        // Validate project description
        if (dto.getProjectDescription() != null && dto.getProjectDescription().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Project description cannot exceed 5000 characters")
                    .build());
        }

        // Validate bank name
        if (dto.getBankName() == null || dto.getBankName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Bank name is required")
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(ManagedProjectBulkUploadDto dto) {
        // Check if project name already exists
        if (dto.getProjectName() != null && 
            managedProjectRepository.existsByProjectName(dto.getProjectName())) {
            return true;
        }

        // Check if project code already exists (if provided)
        if (dto.getProjectCode() != null && !dto.getProjectCode().trim().isEmpty() &&
            managedProjectRepository.existsByProjectCode(dto.getProjectCode())) {
            return true;
        }

        return false;
    }
}
