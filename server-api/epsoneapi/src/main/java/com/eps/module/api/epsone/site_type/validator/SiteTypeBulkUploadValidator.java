package com.eps.module.api.epsone.site_type.validator;

import com.eps.module.api.epsone.site_type.dto.SiteTypeBulkUploadDto;
import com.eps.module.api.epsone.site_type.repository.SiteTypeRepository;
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
public class SiteTypeBulkUploadValidator implements BulkRowValidator<SiteTypeBulkUploadDto> {

    private final SiteTypeRepository siteTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(SiteTypeBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate typeName (required)
        if (dto.getTypeName() == null || dto.getTypeName().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage("Type name is required")
                    .rejectedValue(dto.getTypeName())
                    .build());
        } else if (dto.getTypeName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage("Type name cannot exceed 100 characters")
                    .rejectedValue(dto.getTypeName())
                    .build());
        }

        // Validate description (optional, max length)
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
    public boolean isDuplicate(SiteTypeBulkUploadDto dto) {
        if (dto.getTypeName() == null || dto.getTypeName().isBlank()) {
            return false;
        }
        return siteTypeRepository.existsByTypeNameIgnoreCase(dto.getTypeName().trim());
    }
}
