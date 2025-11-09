package com.eps.module.api.epsone.person_type.validator;

import com.eps.module.api.epsone.person_type.dto.PersonTypeBulkUploadDto;
import com.eps.module.api.epsone.person_type.repository.PersonTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PersonTypeBulkUploadValidator implements BulkRowValidator<PersonTypeBulkUploadDto> {

    private final PersonTypeRepository personTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(PersonTypeBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate type name
        if (rowData.getTypeName() == null || rowData.getTypeName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage("Person type name is required")
                    .rejectedValue(rowData.getTypeName())
                    .build());
        } else if (rowData.getTypeName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Type Name")
                    .errorMessage("Person type name cannot exceed 100 characters")
                    .rejectedValue(rowData.getTypeName())
                    .build());
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
    public boolean isDuplicate(PersonTypeBulkUploadDto rowData) {
        return personTypeRepository.existsByTypeNameIgnoreCase(rowData.getTypeName());
    }
}
