package com.eps.module.api.epsone.vendor.validator;

import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
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
                    .errorMessage("Contact Number is required")
                    .rejectedValue(dto.getContactNumber())
                    .build());
        } else if (!dto.getContactNumber().matches("^[0-9]{10}$")) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Contact Number")
                    .errorMessage("Contact Number must be exactly 10 digits")
                    .rejectedValue(dto.getContactNumber())
                    .build());
        } else {
            // Check if PersonDetails exists with this contact number
            if (!personDetailsRepository.existsByContactNumber(dto.getContactNumber())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Contact Number")
                        .errorMessage("Person Details not found with contact number: " + dto.getContactNumber())
                        .rejectedValue(dto.getContactNumber())
                        .build());
            } else {
                // Check if this person is already a vendor
                PersonDetails personDetails = personDetailsRepository.findByContactNumber(dto.getContactNumber()).get();
                if (vendorRepository.findByVendorDetailsId(personDetails.getId()).isPresent()) {
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .fieldName("Contact Number")
                            .errorMessage("Person with contact number " + dto.getContactNumber() + " is already a vendor")
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
                    .errorMessage("Vendor Type Name is required")
                    .rejectedValue(dto.getVendorTypeName())
                    .build());
        } else if (dto.getVendorTypeName().length() > 150) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Type Name")
                    .errorMessage("Vendor Type Name cannot exceed 150 characters")
                    .rejectedValue(dto.getVendorTypeName())
                    .build());
        } else {
            // Check if vendor type exists
            if (!vendorTypeRepository.existsByTypeNameIgnoreCase(dto.getVendorTypeName())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Vendor Type Name")
                        .errorMessage("Vendor Type not found: " + dto.getVendorTypeName())
                        .rejectedValue(dto.getVendorTypeName())
                        .build());
            }
        }

        // Validate Vendor Code (optional but with length limit and uniqueness check)
        if (dto.getVendorCodeAlt() != null && !dto.getVendorCodeAlt().isBlank()) {
            if (dto.getVendorCodeAlt().length() > 10) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Vendor Code")
                        .errorMessage("Vendor Code cannot exceed 10 characters")
                        .rejectedValue(dto.getVendorCodeAlt())
                        .build());
            }
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
