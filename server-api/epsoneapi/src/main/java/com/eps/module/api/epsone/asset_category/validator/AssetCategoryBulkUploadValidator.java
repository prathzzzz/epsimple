package com.eps.module.api.epsone.asset_category.validator;

import com.eps.module.api.epsone.asset_category.constant.AssetCategoryErrorMessages;
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
                    .errorMessage(AssetCategoryErrorMessages.ASSET_CATEGORY_NAME_REQUIRED)
                    .rejectedValue(dto.getCategoryName())
                    .build());
        } else if (dto.getCategoryName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("categoryName")
                    .errorMessage(AssetCategoryErrorMessages.ASSET_CATEGORY_NAME_MAX_LENGTH)
                    .rejectedValue(dto.getCategoryName())
                    .build());
        }

        // Validate category code
        if (dto.getCategoryCode() == null || dto.getCategoryCode().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("categoryCode")
                    .errorMessage(AssetCategoryErrorMessages.ASSET_CATEGORY_CODE_REQUIRED)
                    .rejectedValue(dto.getCategoryCode())
                    .build());
        } else {
            if (dto.getCategoryCode().length() > 20) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("categoryCode")
                        .errorMessage(AssetCategoryErrorMessages.ASSET_CATEGORY_CODE_MAX_LENGTH)
                        .rejectedValue(dto.getCategoryCode())
                        .build());
            }
            // Validate format: uppercase alphanumeric with optional hyphens/underscores/ampersand
            if (!dto.getCategoryCode().matches("^[A-Z0-9_&-]+$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("categoryCode")
                        .errorMessage(AssetCategoryErrorMessages.ASSET_CATEGORY_CODE_FORMAT)
                        .rejectedValue(dto.getCategoryCode())
                        .build());
            }
        }

        // Validate asset code alt
        if (dto.getAssetCodeAlt() == null || dto.getAssetCodeAlt().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("assetCodeAlt")
                    .errorMessage(AssetCategoryErrorMessages.ASSET_CODE_ALT_REQUIRED)
                    .rejectedValue(dto.getAssetCodeAlt())
                    .build());
        } else if (dto.getAssetCodeAlt().length() > 10) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("assetCodeAlt")
                    .errorMessage(AssetCategoryErrorMessages.ASSET_CODE_ALT_MAX_LENGTH)
                    .rejectedValue(dto.getAssetCodeAlt())
                    .build());
        }

        // Validate description (optional, but check length if provided)
        if (dto.getDescription() != null && dto.getDescription().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("description")
                    .errorMessage(AssetCategoryErrorMessages.DESCRIPTION_MAX_LENGTH)
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
