package com.eps.module.api.epsone.cost_category.validator;

import com.eps.module.api.epsone.cost_category.constant.CostCategoryErrorMessages;
import com.eps.module.api.epsone.cost_category.dto.CostCategoryBulkUploadDto;
import com.eps.module.api.epsone.cost_category.repository.CostCategoryRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CostCategoryBulkUploadValidator implements BulkRowValidator<CostCategoryBulkUploadDto> {

    private final CostCategoryRepository costCategoryRepository;

    @Override
    public List<BulkUploadErrorDto> validate(CostCategoryBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        if (rowData.getCategoryName() == null || rowData.getCategoryName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Category Name")
                    .errorMessage(CostCategoryErrorMessages.COST_CATEGORY_NAME_REQUIRED)
                    .rejectedValue(rowData.getCategoryName())
                    .build());
        } else if (rowData.getCategoryName().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Category Name")
                    .errorMessage(CostCategoryErrorMessages.COST_CATEGORY_NAME_MAX_LENGTH)
                    .rejectedValue(rowData.getCategoryName())
                    .build());
        }

        if (rowData.getCategoryDescription() == null || rowData.getCategoryDescription().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Category Description")
                    .errorMessage(CostCategoryErrorMessages.COST_CATEGORY_DESCRIPTION_REQUIRED)
                    .rejectedValue(rowData.getCategoryDescription())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(CostCategoryBulkUploadDto rowData) {
        return costCategoryRepository.existsByCategoryNameIgnoreCase(rowData.getCategoryName());
    }
}
