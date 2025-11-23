package com.eps.module.api.epsone.landlord.validator;

import com.eps.module.api.epsone.landlord.constant.LandlordErrorMessages;
import com.eps.module.api.epsone.landlord.dto.LandlordBulkUploadDto;
import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LandlordBulkUploadValidator implements BulkRowValidator<LandlordBulkUploadDto> {

    private final LandlordRepository landlordRepository;
    private final PersonDetailsRepository personDetailsRepository;

    @Override
    public List<BulkUploadErrorDto> validate(LandlordBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Contact Number (required)
        if (dto.getContactNumber() == null || dto.getContactNumber().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Contact Number")
                    .errorMessage(LandlordErrorMessages.CONTACT_NUMBER_REQUIRED)
                    .rejectedValue(dto.getContactNumber())
                    .build());
        } else {
            String contactNumber = dto.getContactNumber().trim();
            
            // Validate format
            if (!contactNumber.matches("\\d{10}")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Contact Number")
                        .errorMessage(LandlordErrorMessages.CONTACT_NUMBER_INVALID_FORMAT)
                        .rejectedValue(dto.getContactNumber())
                        .build());
            } else {
                // Check if PersonDetails exists with this contact number
                if (!personDetailsRepository.existsByContactNumber(contactNumber)) {
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .fieldName("Contact Number")
                            .errorMessage(String.format(LandlordErrorMessages.PERSON_DETAILS_NOT_FOUND_CONTACT, contactNumber))
                            .rejectedValue(dto.getContactNumber())
                            .build());
                } else {
                    // Check if this PersonDetails is already used by another landlord
                    var personDetails = personDetailsRepository.findByContactNumber(contactNumber);
                    if (personDetails.isPresent() && landlordRepository.findByLandlordDetailsId(personDetails.get().getId()).isPresent()) {
                        errors.add(BulkUploadErrorDto.builder()
                                .rowNumber(rowNumber)
                                .fieldName("Contact Number")
                                .errorMessage(String.format(LandlordErrorMessages.PERSON_ALREADY_LANDLORD, contactNumber))
                                .rejectedValue(dto.getContactNumber())
                                .build());
                    }
                }
            }
        }

        // Validate Rent Share Percentage (optional)
        if (dto.getRentSharePercentage() != null) {
            BigDecimal percentage = dto.getRentSharePercentage();
            if (percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(new BigDecimal("100")) > 0) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Rent Share Percentage")
                        .errorMessage(LandlordErrorMessages.RENT_SHARE_INVALID_RANGE)
                        .rejectedValue(dto.getRentSharePercentage() != null ? dto.getRentSharePercentage().toString() : null)
                        .build());
            }
            if (percentage.scale() > 2) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Rent Share Percentage")
                        .errorMessage(LandlordErrorMessages.RENT_SHARE_INVALID_SCALE)
                        .rejectedValue(dto.getRentSharePercentage() != null ? dto.getRentSharePercentage().toString() : null)
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(LandlordBulkUploadDto dto) {
        return false;
    }
}
