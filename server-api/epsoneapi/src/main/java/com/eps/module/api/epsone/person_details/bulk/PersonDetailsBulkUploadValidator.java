package com.eps.module.api.epsone.person_details.bulk;

import com.eps.module.api.epsone.person_details.constant.PersonDetailsErrorMessages;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsBulkUploadDto;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.person_type.repository.PersonTypeRepository;
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
public class PersonDetailsBulkUploadValidator implements BulkRowValidator<PersonDetailsBulkUploadDto> {

    private final PersonDetailsRepository personDetailsRepository;
    private final PersonTypeRepository personTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(PersonDetailsBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();
        
        // Validate person type name
        if (dto.getPersonTypeName() == null || dto.getPersonTypeName().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Person Type Name")
                    .errorMessage(PersonDetailsErrorMessages.PERSON_TYPE_NAME_REQUIRED)
                    .rejectedValue(dto.getPersonTypeName())
                    .build());
        } else if (dto.getPersonTypeName().length() > 150) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Person Type Name")
                    .errorMessage(PersonDetailsErrorMessages.PERSON_TYPE_NAME_TOO_LONG)
                    .rejectedValue(dto.getPersonTypeName())
                    .build());
        } else {
            // Check if person type exists
            if (!personTypeRepository.existsByTypeNameIgnoreCase(dto.getPersonTypeName())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Person Type Name")
                        .errorMessage(PersonDetailsErrorMessages.PERSON_TYPE_NOT_FOUND_NAME + dto.getPersonTypeName())
                        .rejectedValue(dto.getPersonTypeName())
                        .build());
            }
        }

        // Validate first name (optional but if provided, should be valid)
        if (dto.getFirstName() != null && dto.getFirstName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("First Name")
                    .errorMessage(PersonDetailsErrorMessages.FIRST_NAME_TOO_LONG)
                    .rejectedValue(dto.getFirstName())
                    .build());
        }

        // Validate middle name (optional but if provided, should be valid)
        if (dto.getMiddleName() != null && dto.getMiddleName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Middle Name")
                    .errorMessage(PersonDetailsErrorMessages.MIDDLE_NAME_TOO_LONG)
                    .rejectedValue(dto.getMiddleName())
                    .build());
        }

        // Validate last name (optional but if provided, should be valid)
        if (dto.getLastName() != null && dto.getLastName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Last Name")
                    .errorMessage(PersonDetailsErrorMessages.LAST_NAME_TOO_LONG)
                    .rejectedValue(dto.getLastName())
                    .build());
        }

        // Validate contact number (optional but if provided, should be exactly 10 digits)
        if (dto.getContactNumber() != null && !dto.getContactNumber().isBlank()) {
            if (!dto.getContactNumber().matches("^[0-9]{10}$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Contact Number")
                        .errorMessage(PersonDetailsErrorMessages.CONTACT_NUMBER_INVALID)
                        .rejectedValue(dto.getContactNumber())
                        .build());
            }
        }

        // Validate permanent address (optional but if provided, should be valid)
        if (dto.getPermanentAddress() != null && dto.getPermanentAddress().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Permanent Address")
                    .errorMessage(PersonDetailsErrorMessages.PERMANENT_ADDRESS_TOO_LONG)
                    .rejectedValue(dto.getPermanentAddress())
                    .build());
        }

        // Validate correspondence address (optional but if provided, should be valid)
        if (dto.getCorrespondenceAddress() != null && dto.getCorrespondenceAddress().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Correspondence Address")
                    .errorMessage(PersonDetailsErrorMessages.CORRESPONDENCE_ADDRESS_TOO_LONG)
                    .rejectedValue(dto.getCorrespondenceAddress())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(PersonDetailsBulkUploadDto rowData) {
        // Check for duplicates based on contact number (if provided)
        if (rowData.getContactNumber() != null && !rowData.getContactNumber().isBlank()) {
            return personDetailsRepository.existsByContactNumber(rowData.getContactNumber().trim());
        }
        return false;
    }
}
