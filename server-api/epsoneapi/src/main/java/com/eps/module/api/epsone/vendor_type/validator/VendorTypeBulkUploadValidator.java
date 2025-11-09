package com.eps.module.api.epsone.vendor_type.validator;

import com.eps.module.api.epsone.vendor_type.dto.VendorTypeBulkUploadDto;
import com.eps.module.api.epsone.vendor_type.repository.VendorTypeRepository;
import com.eps.module.api.epsone.vendor_category.repository.VendorCategoryRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VendorTypeBulkUploadValidator implements BulkRowValidator<VendorTypeBulkUploadDto> {

    private final VendorTypeRepository vendorTypeRepository;
    private final VendorCategoryRepository vendorCategoryRepository;

    @Override
    public List<BulkUploadErrorDto> validate(VendorTypeBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate type name
        if (rowData.getTypeName() == null || rowData.getTypeName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage("Vendor type name is required")
                    .rejectedValue(rowData.getTypeName())
                    .build());
        } else if (rowData.getTypeName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage("Vendor type name cannot exceed 100 characters")
                    .rejectedValue(rowData.getTypeName())
                    .build());
        }

        // Validate vendor category name
        if (rowData.getVendorCategoryName() == null || rowData.getVendorCategoryName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Category Name")
                    .errorMessage("Vendor category name is required")
                    .rejectedValue(rowData.getVendorCategoryName())
                    .build());
        } else {
            // Check if vendor category exists
            boolean categoryExists = vendorCategoryRepository.existsByCategoryNameIgnoreCase(
                    rowData.getVendorCategoryName().trim()
            );
            
            if (!categoryExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Vendor Category Name")
                        .errorMessage("Vendor category '" + rowData.getVendorCategoryName() + "' not found")
                        .rejectedValue(rowData.getVendorCategoryName())
                        .build());
            }
        }

        // Validate description length if provided
        if (rowData.getDescription() != null && rowData.getDescription().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Description")
                    .errorMessage("Description cannot exceed 5000 characters")
                    .rejectedValue(rowData.getDescription())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(VendorTypeBulkUploadDto rowData) {
        return vendorTypeRepository.existsByTypeNameIgnoreCase(rowData.getTypeName());
    }
}
