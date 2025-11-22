package com.eps.module.api.epsone.site_category.validator;

import com.eps.module.api.epsone.site_category.dto.SiteCategoryBulkUploadDto;
import com.eps.module.api.epsone.site_category.repository.SiteCategoryRepository;
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
public class SiteCategoryBulkUploadValidator implements BulkRowValidator<SiteCategoryBulkUploadDto> {

    private final SiteCategoryRepository siteCategoryRepository;

    @Override
    public List<BulkUploadErrorDto> validate(SiteCategoryBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Category Name (required)
        if (dto.getCategoryName() == null || dto.getCategoryName().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Category Name")
                    .errorMessage("Category Name is required")
                    .rejectedValue(dto.getCategoryName())
                    .build());
        } else if (dto.getCategoryName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Category Name")
                    .errorMessage("Category Name cannot exceed 100 characters")
                    .rejectedValue(dto.getCategoryName())
                    .build());
        }

        // Validate Category Code (optional but with length limit and format)
        if (dto.getCategoryCode() != null && !dto.getCategoryCode().isBlank()) {
            if (dto.getCategoryCode().length() > 20) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Category Code")
                        .errorMessage("Category Code cannot exceed 20 characters")
                        .rejectedValue(dto.getCategoryCode())
                        .build());
            }
            // Validate format: uppercase alphanumeric with optional hyphens/underscores
            if (!dto.getCategoryCode().matches("^[A-Z0-9_-]+$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Category Code")
                        .errorMessage("Category Code must contain only uppercase letters, numbers, hyphens, and underscores")
                        .rejectedValue(dto.getCategoryCode())
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
    public boolean isDuplicate(SiteCategoryBulkUploadDto rowData) {
        // Check for duplicates based on category name
        if (rowData.getCategoryName() != null && !rowData.getCategoryName().isBlank()) {
            if (siteCategoryRepository.existsByCategoryNameIgnoreCase(rowData.getCategoryName().trim())) {
                return true;
            }
        }
        
        // Check for duplicates based on category code (if provided)
        if (rowData.getCategoryCode() != null && !rowData.getCategoryCode().isBlank()) {
            return siteCategoryRepository.existsByCategoryCode(rowData.getCategoryCode().trim());
        }
        
        return false;
    }
}
