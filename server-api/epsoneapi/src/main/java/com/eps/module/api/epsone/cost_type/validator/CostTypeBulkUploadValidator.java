package com.eps.module.api.epsone.cost_type.validator;

import com.eps.module.api.epsone.cost_category.repository.CostCategoryRepository;
import com.eps.module.api.epsone.cost_type.constant.CostTypeErrorMessages;
import com.eps.module.api.epsone.cost_type.dto.CostTypeBulkUploadDto;
import com.eps.module.api.epsone.cost_type.repository.CostTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CostTypeBulkUploadValidator implements BulkRowValidator<CostTypeBulkUploadDto> {

    private final CostTypeRepository costTypeRepository;
    private final CostCategoryRepository costCategoryRepository;

    @Override
    public List<BulkUploadErrorDto> validate(CostTypeBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Type Name
        if (rowData.getTypeName() == null || rowData.getTypeName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage(CostTypeErrorMessages.TYPE_NAME_REQUIRED)
                    .rejectedValue(rowData.getTypeName())
                    .build());
        } else if (rowData.getTypeName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage(CostTypeErrorMessages.TYPE_NAME_TOO_LONG)
                    .rejectedValue(rowData.getTypeName())
                    .build());
        }

        // Validate Type Description
        if (rowData.getTypeDescription() == null || rowData.getTypeDescription().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Description")
                    .errorMessage(CostTypeErrorMessages.TYPE_DESCRIPTION_REQUIRED)
                    .rejectedValue(rowData.getTypeDescription())
                    .build());
        }

        // Validate Cost Category Name
        if (rowData.getCostCategoryName() == null || rowData.getCostCategoryName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Cost Category Name")
                    .errorMessage(CostTypeErrorMessages.COST_CATEGORY_NAME_REQUIRED)
                    .rejectedValue(rowData.getCostCategoryName())
                    .build());
        } else {
            // Check if cost category exists
            boolean categoryExists = costCategoryRepository.existsByCategoryNameIgnoreCase(rowData.getCostCategoryName());
            if (!categoryExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Cost Category Name")
                        .errorMessage(String.format(CostTypeErrorMessages.COST_CATEGORY_NOT_FOUND_NAME, rowData.getCostCategoryName()))
                        .rejectedValue(rowData.getCostCategoryName())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(CostTypeBulkUploadDto rowData) {
        // For cost types, we can check if a type with the same name already exists
        // Note: In a real scenario, you might want to check for duplicates within the same category
        return costTypeRepository.existsByTypeNameIgnoreCase(rowData.getTypeName());
    }
}
