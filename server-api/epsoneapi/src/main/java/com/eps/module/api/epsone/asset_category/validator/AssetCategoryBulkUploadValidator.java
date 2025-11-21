package com.eps.module.api.epsone.asset_category.validator;

import com.eps.module.api.epsone.asset_category.dto.AssetCategoryBulkUploadDto;
import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AssetCategoryBulkUploadValidator implements BulkRowValidator<AssetCategoryBulkUploadDto> {

    private final AssetCategoryRepository assetCategoryRepository;

    @Override
    public List<BulkUploadErrorDto> validate(AssetCategoryBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate category name
        if (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("categoryName")
                    .errorMessage("Category name is required")
                    .rejectedValue(dto.getCategoryName())
                    .build());
        } else if (dto.getCategoryName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("categoryName")
                    .errorMessage("Category name must not exceed 100 characters")
                    .rejectedValue(dto.getCategoryName())
                    .build());
        }

        // Validate category code
        if (dto.getCategoryCode() == null || dto.getCategoryCode().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("categoryCode")
                    .errorMessage("Category code is required")
                    .rejectedValue(dto.getCategoryCode())
                    .build());
        } else {
            if (dto.getCategoryCode().length() > 20) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("categoryCode")
                        .errorMessage("Category code must not exceed 20 characters")
                        .rejectedValue(dto.getCategoryCode())
                        .build());
            }
            // Validate format: uppercase alphanumeric with optional hyphens/underscores
            if (!dto.getCategoryCode().matches("^[A-Z0-9_-]+$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("categoryCode")
                        .errorMessage("Category code must contain only uppercase letters, numbers, hyphens, and underscores")
                        .rejectedValue(dto.getCategoryCode())
                        .build());
            }
        }

        // Validate asset code alt
        if (dto.getAssetCodeAlt() == null || dto.getAssetCodeAlt().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("assetCodeAlt")
                    .errorMessage("Asset code alt is required")
                    .rejectedValue(dto.getAssetCodeAlt())
                    .build());
        } else if (dto.getAssetCodeAlt().length() > 10) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("assetCodeAlt")
                    .errorMessage("Asset code alt must not exceed 10 characters")
                    .rejectedValue(dto.getAssetCodeAlt())
                    .build());
        }

        // Validate description (optional, but check length if provided)
        if (dto.getDescription() != null && dto.getDescription().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("description")
                    .errorMessage("Description must not exceed 5000 characters")
                    .rejectedValue(dto.getDescription())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(AssetCategoryBulkUploadDto dto) {
        return assetCategoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName()) ||
               assetCategoryRepository.existsByCategoryCodeIgnoreCase(dto.getCategoryCode()) ||
               assetCategoryRepository.existsByAssetCodeAltIgnoreCase(dto.getAssetCodeAlt());
    }
}
