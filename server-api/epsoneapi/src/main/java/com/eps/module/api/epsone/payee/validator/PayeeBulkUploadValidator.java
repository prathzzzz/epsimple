package com.eps.module.api.epsone.payee.validator;

import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.payee.constant.PayeeErrorMessages;
import com.eps.module.api.epsone.payee.dto.PayeeBulkUploadDto;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.payee_type.repository.PayeeTypeRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.payment.PayeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PayeeBulkUploadValidator implements BulkRowValidator<PayeeBulkUploadDto> {

    private final PayeeTypeRepository payeeTypeRepository;
    private final PayeeDetailsRepository payeeDetailsRepository;
    private final VendorRepository vendorRepository;
    private final LandlordRepository landlordRepository;
    private final PayeeRepository payeeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(PayeeBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Payee Type
        if (rowData.getPayeeType() == null || rowData.getPayeeType().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Payee Type")
                    .errorMessage(PayeeErrorMessages.PAYEE_TYPE_REQUIRED)
                    .rejectedValue(rowData.getPayeeType())
                    .build());
        } else {
            boolean payeeTypeExists = payeeTypeRepository.findByPayeeTypeIgnoreCase(rowData.getPayeeType()).isPresent();
            if (!payeeTypeExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Payee Type")
                        .errorMessage(String.format(PayeeErrorMessages.PAYEE_TYPE_NOT_FOUND_NAME, rowData.getPayeeType()))
                        .rejectedValue(rowData.getPayeeType())
                        .build());
            }
        }

        // Validate Payee Name
        if (rowData.getPayeeName() == null || rowData.getPayeeName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Payee Name")
                    .errorMessage(PayeeErrorMessages.PAYEE_NAME_REQUIRED)
                    .rejectedValue(rowData.getPayeeName())
                    .build());
        } else {
            // Check if payee details with this name exists
            Optional<PayeeDetails> payeeDetails = payeeDetailsRepository.findByPayeeNameIgnoreCase(rowData.getPayeeName());
            if (payeeDetails.isEmpty()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Payee Name")
                        .errorMessage(String.format(PayeeErrorMessages.PAYEE_DETAILS_NOT_FOUND_NAME, rowData.getPayeeName()))
                        .rejectedValue(rowData.getPayeeName())
                        .build());
            } else {
                // Check if this payee details is already assigned to another payee
                if (payeeRepository.countByPayeeDetailsId(payeeDetails.get().getId()) > 0) {
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .fieldName("Payee Name")
                            .errorMessage(String.format(PayeeErrorMessages.PAYEE_DETAILS_ALREADY_ASSIGNED_NAME, rowData.getPayeeName()))
                            .rejectedValue(rowData.getPayeeName())
                            .build());
                }
            }
        }

        // Validate Vendor if provided
        if (rowData.getVendorContactNumber() != null && !rowData.getVendorContactNumber().trim().isEmpty()) {
            boolean vendorExists = vendorRepository.findByVendorDetailsContactNumber(rowData.getVendorContactNumber()).isPresent();
            if (!vendorExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Vendor Contact Number")
                        .errorMessage(String.format(PayeeErrorMessages.VENDOR_NOT_FOUND_CONTACT, rowData.getVendorContactNumber()))
                        .rejectedValue(rowData.getVendorContactNumber())
                        .build());
            }
        }

        // Validate Landlord if provided
        if (rowData.getLandlordContactNumber() != null && !rowData.getLandlordContactNumber().trim().isEmpty()) {
            boolean landlordExists = landlordRepository.findByLandlordDetailsContactNumber(rowData.getLandlordContactNumber()).isPresent();
            if (!landlordExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Landlord Contact Number")
                        .errorMessage(String.format(PayeeErrorMessages.LANDLORD_NOT_FOUND_CONTACT, rowData.getLandlordContactNumber()))
                        .rejectedValue(rowData.getLandlordContactNumber())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(PayeeBulkUploadDto rowData) {
        // Check if a payee with the same payee details already exists
        if (rowData.getPayeeName() != null && !rowData.getPayeeName().trim().isEmpty()) {
            Optional<PayeeDetails> payeeDetails = payeeDetailsRepository.findByPayeeNameIgnoreCase(rowData.getPayeeName());
            if (payeeDetails.isPresent()) {
                return payeeRepository.countByPayeeDetailsId(payeeDetails.get().getId()) > 0;
            }
        }
        return false;
    }
}
