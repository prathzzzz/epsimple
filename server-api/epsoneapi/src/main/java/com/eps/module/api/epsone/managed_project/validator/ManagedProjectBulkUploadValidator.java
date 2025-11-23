package com.eps.module.api.epsone.managed_project.validator;

import com.eps.module.api.epsone.managed_project.constant.ManagedProjectErrorMessages;
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
                    .errorMessage(ManagedProjectErrorMessages.PROJECT_NAME_REQUIRED)
                    .build());
        } else if (dto.getProjectName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(ManagedProjectErrorMessages.PROJECT_NAME_TOO_LONG)
                    .build());
        }

        // Validate project code
        if (dto.getProjectCode() != null && !dto.getProjectCode().trim().isEmpty()) {
            if (dto.getProjectCode().length() > 50) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(ManagedProjectErrorMessages.PROJECT_CODE_TOO_LONG)
                        .build());
            }
            if (!dto.getProjectCode().matches("^[A-Za-z0-9_-]+$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(ManagedProjectErrorMessages.PROJECT_CODE_INVALID_FORMAT)
                        .build());
            }
            // Check for duplicate project code
            if (managedProjectRepository.existsByProjectCode(dto.getProjectCode())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(String.format(ManagedProjectErrorMessages.DUPLICATE_PROJECT_CODE, dto.getProjectCode()))
                        .errorType("DUPLICATE")
                        .build());
            }
        }

        // Validate project type
        if (dto.getProjectType() != null && dto.getProjectType().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(ManagedProjectErrorMessages.PROJECT_TYPE_TOO_LONG)
                    .build());
        }

        // Validate project description
        if (dto.getProjectDescription() != null && dto.getProjectDescription().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(ManagedProjectErrorMessages.PROJECT_DESCRIPTION_TOO_LONG)
                    .build());
        }

        // Validate bank name
        if (dto.getBankName() == null || dto.getBankName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(ManagedProjectErrorMessages.BANK_NAME_REQUIRED)
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
