package com.eps.module.api.epsone.vendor.validator;

import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.vendor.constant.VendorErrorMessages;
import com.eps.module.api.epsone.vendor.dto.VendorBulkUploadDto;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.api.epsone.vendor_type.repository.VendorTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.person.PersonDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VendorBulkUploadValidator implements BulkRowValidator<VendorBulkUploadDto> {

    private final VendorRepository vendorRepository;
    private final VendorTypeRepository vendorTypeRepository;
    private final PersonDetailsRepository personDetailsRepository;

    @Override
    public List<BulkUploadErrorDto> validate(VendorBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Contact Number (required)
        if (dto.getContactNumber() == null || dto.getContactNumber().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Contact Number")
                    .errorMessage(VendorErrorMessages.CONTACT_NUMBER_REQUIRED)
                    .rejectedValue(dto.getContactNumber())
                    .build());
        } else if (!dto.getContactNumber().matches("^[0-9]{10}$")) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Contact Number")
                    .errorMessage(VendorErrorMessages.CONTACT_NUMBER_INVALID_FORMAT)
                    .rejectedValue(dto.getContactNumber())
                    .build());
        } else {
            // Check if PersonDetails exists with this contact number
            if (!personDetailsRepository.existsByContactNumber(dto.getContactNumber())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Contact Number")
                        .errorMessage(String.format(VendorErrorMessages.PERSON_DETAILS_NOT_FOUND_CONTACT, dto.getContactNumber()))
                        .rejectedValue(dto.getContactNumber())
                        .build());
            } else {
                // Check if this person is already a vendor
                try {
                    PersonDetails personDetails = personDetailsRepository.findByContactNumber(dto.getContactNumber()).get();
                    if (vendorRepository.findByVendorDetailsId(personDetails.getId()).isPresent()) {
                        errors.add(BulkUploadErrorDto.builder()
                                .rowNumber(rowNumber)
                                .fieldName("Contact Number")
                                .errorMessage(String.format(VendorErrorMessages.PERSON_ALREADY_VENDOR, dto.getContactNumber()))
                                .rejectedValue(dto.getContactNumber())
                                .build());
                    }
                } catch (Exception e) {
                    // Handle case where multiple person details exist with same contact number
                    log.error("Error checking vendor for contact number {}: {}", dto.getContactNumber(), e.getMessage());
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .fieldName("Contact Number")
                            .errorMessage(String.format(VendorErrorMessages.MULTIPLE_PERSON_RECORDS, dto.getContactNumber()))
                            .rejectedValue(dto.getContactNumber())
                            .build());
                }
            }
        }

        // Validate Vendor Type Name (required)
        if (dto.getVendorTypeName() == null || dto.getVendorTypeName().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Type Name")
                    .errorMessage(VendorErrorMessages.VENDOR_TYPE_NAME_REQUIRED)
                    .rejectedValue(dto.getVendorTypeName())
                    .build());
        } else if (dto.getVendorTypeName().length() > 150) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Type Name")
                    .errorMessage(VendorErrorMessages.VENDOR_TYPE_NAME_TOO_LONG)
                    .rejectedValue(dto.getVendorTypeName())
                    .build());
        } else {
            // Check if vendor type exists
            if (!vendorTypeRepository.existsByTypeNameIgnoreCase(dto.getVendorTypeName())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Vendor Type Name")
                        .errorMessage(String.format(VendorErrorMessages.VENDOR_TYPE_NOT_FOUND_NAME, dto.getVendorTypeName()))
                        .rejectedValue(dto.getVendorTypeName())
                        .build());
            }
        }

        // Validate Vendor Code (required)
        if (dto.getVendorCodeAlt() == null || dto.getVendorCodeAlt().isBlank()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Code")
                    .errorMessage(VendorErrorMessages.VENDOR_CODE_REQUIRED)
                    .rejectedValue(dto.getVendorCodeAlt())
                    .build());
        } else if (dto.getVendorCodeAlt().length() > 10) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Code")
                    .errorMessage(VendorErrorMessages.VENDOR_CODE_TOO_LONG)
                    .rejectedValue(dto.getVendorCodeAlt())
                    .build());
        } else if (!dto.getVendorCodeAlt().matches("^[A-Z0-9_-]+$")) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Code")
                    .errorMessage(VendorErrorMessages.VENDOR_CODE_INVALID_FORMAT)
                    .rejectedValue(dto.getVendorCodeAlt())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(VendorBulkUploadDto rowData) {
        // Check for duplicates based on vendor code (if provided)
        if (rowData.getVendorCodeAlt() != null && !rowData.getVendorCodeAlt().isBlank()) {
            return vendorRepository.existsByVendorCodeAlt(rowData.getVendorCodeAlt().trim());
        }
        return false;
    }
}
