package com.eps.module.api.epsone.asset_type.validator;

import com.eps.module.api.epsone.asset_type.constant.AssetTypeErrorMessages;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeBulkUploadDto;
import com.eps.module.api.epsone.asset_type.repository.AssetTypeRepository;
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
public class AssetTypeBulkUploadValidator implements BulkRowValidator<AssetTypeBulkUploadDto> {

    private final AssetTypeRepository assetTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(AssetTypeBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Type Name (required)
        if (dto.getTypeName() == null || dto.getTypeName().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage(AssetTypeErrorMessages.TYPE_NAME_REQUIRED)
                    .rejectedValue(dto.getTypeName())
                    .build());
        } else if (dto.getTypeName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage(AssetTypeErrorMessages.TYPE_NAME_MAX_LENGTH)
                    .rejectedValue(dto.getTypeName())
                    .build());
        }

        // Validate Type Code (required)
        if (dto.getTypeCode() == null || dto.getTypeCode().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Code")
                    .errorMessage(AssetTypeErrorMessages.TYPE_CODE_REQUIRED)
                    .rejectedValue(dto.getTypeCode())
                    .build());
        } else if (dto.getTypeCode().length() > 20) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Code")
                    .errorMessage(AssetTypeErrorMessages.TYPE_CODE_MAX_LENGTH)
                    .rejectedValue(dto.getTypeCode())
                    .build());
        } else if (!dto.getTypeCode().matches("^[A-Z0-9_-]+$")) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Code")
                    .errorMessage(AssetTypeErrorMessages.TYPE_CODE_INVALID_FORMAT)
                    .rejectedValue(dto.getTypeCode())
                    .build());
        }

        // Validate Description (optional)
        if (dto.getDescription() != null && dto.getDescription().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Description")
                    .errorMessage(AssetTypeErrorMessages.DESCRIPTION_MAX_LENGTH)
                    .rejectedValue(dto.getDescription())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(AssetTypeBulkUploadDto dto) {
        if (dto.getTypeName() != null && !dto.getTypeName().isBlank()) {
            if (assetTypeRepository.existsByTypeNameIgnoreCase(dto.getTypeName().trim())) {
                return true;
            }
        }
        
        if (dto.getTypeCode() != null && !dto.getTypeCode().isBlank()) {
            return assetTypeRepository.existsByTypeCodeIgnoreCase(dto.getTypeCode().trim());
        }
        
        return false;
    }
}
