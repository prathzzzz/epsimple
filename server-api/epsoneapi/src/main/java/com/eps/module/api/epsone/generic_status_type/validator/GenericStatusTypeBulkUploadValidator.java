package com.eps.module.api.epsone.generic_status_type.validator;

import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeBulkUploadDto;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
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
public class GenericStatusTypeBulkUploadValidator implements BulkRowValidator<GenericStatusTypeBulkUploadDto> {

    private final GenericStatusTypeRepository genericStatusTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(GenericStatusTypeBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Status Name (required)
        if (dto.getStatusName() == null || dto.getStatusName().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Status Name")
                    .errorMessage("Status Name is required")
                    .rejectedValue(dto.getStatusName())
                    .build());
        } else if (dto.getStatusName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Status Name")
                    .errorMessage("Status Name cannot exceed 100 characters")
                    .rejectedValue(dto.getStatusName())
                    .build());
        }

        // Validate Status Code (optional but with format validation)
        if (dto.getStatusCode() != null && !dto.getStatusCode().isBlank()) {
            if (dto.getStatusCode().length() > 20) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Status Code")
                        .errorMessage("Status Code cannot exceed 20 characters")
                        .rejectedValue(dto.getStatusCode())
                        .build());
            } else if (!dto.getStatusCode().matches("^[A-Z ]*$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Status Code")
                        .errorMessage("Status Code must contain only uppercase letters and spaces")
                        .rejectedValue(dto.getStatusCode())
                        .build());
            }
        }

        // Validate Description (optional but with length limit)
        if (dto.getDescription() != null && dto.getDescription().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Description")
                    .errorMessage("Description cannot exceed 5000 characters")
                    .rejectedValue(dto.getDescription())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(GenericStatusTypeBulkUploadDto rowData) {
        // Check for duplicates based on status name
        if (rowData.getStatusName() != null && !rowData.getStatusName().isBlank()) {
            if (genericStatusTypeRepository.existsByStatusNameIgnoreCase(rowData.getStatusName().trim())) {
                return true;
            }
        }
        
        // Check for duplicates based on status code (if provided)
        if (rowData.getStatusCode() != null && !rowData.getStatusCode().isBlank()) {
            return genericStatusTypeRepository.existsByStatusCode(rowData.getStatusCode().trim());
        }
        
        return false;
    }
}
