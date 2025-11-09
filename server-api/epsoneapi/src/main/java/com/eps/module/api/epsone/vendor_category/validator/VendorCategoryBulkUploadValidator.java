package com.eps.module.api.epsone.vendor_category.validator;

import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryBulkUploadDto;
import com.eps.module.api.epsone.vendor_category.repository.VendorCategoryRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VendorCategoryBulkUploadValidator implements BulkRowValidator<VendorCategoryBulkUploadDto> {

    private final VendorCategoryRepository vendorCategoryRepository;

    @Override
    public List<BulkUploadErrorDto> validate(VendorCategoryBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        if (rowData.getCategoryName() == null || rowData.getCategoryName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Category Name")
                    .errorMessage("Category name is required")
                    .rejectedValue(rowData.getCategoryName())
                    .build());
        } else if (rowData.getCategoryName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Category Name")
                    .errorMessage("Category name cannot exceed 100 characters")
                    .rejectedValue(rowData.getCategoryName())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(VendorCategoryBulkUploadDto rowData) {
        return vendorCategoryRepository.existsByCategoryNameIgnoreCase(rowData.getCategoryName());
    }
}
