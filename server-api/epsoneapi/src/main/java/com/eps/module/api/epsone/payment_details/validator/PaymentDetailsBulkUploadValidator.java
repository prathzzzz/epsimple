package com.eps.module.api.epsone.payment_details.validator;

import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsBulkUploadDto;
import com.eps.module.api.epsone.payment_method.repository.PaymentMethodRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentDetailsBulkUploadValidator implements BulkRowValidator<PaymentDetailsBulkUploadDto> {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<BulkUploadErrorDto> validate(PaymentDetailsBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Payment Method Name
        if (rowData.getPaymentMethodName() == null || rowData.getPaymentMethodName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Payment Method Name")
                    .errorMessage("Payment method name is required")
                    .rejectedValue(rowData.getPaymentMethodName())
                    .build());
        } else {
            // Check if payment method exists
            boolean methodExists = paymentMethodRepository.existsByMethodNameIgnoreCase(rowData.getPaymentMethodName());
            if (!methodExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Payment Method Name")
                        .errorMessage("Payment method '" + rowData.getPaymentMethodName() + "' does not exist")
                        .rejectedValue(rowData.getPaymentMethodName())
                        .build());
            }
        }

        // Validate field lengths
        if (rowData.getTransactionNumber() != null && rowData.getTransactionNumber().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Transaction Number")
                    .errorMessage("Transaction number cannot exceed 255 characters")
                    .rejectedValue(rowData.getTransactionNumber())
                    .build());
        }

        if (rowData.getVpa() != null && rowData.getVpa().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("VPA")
                    .errorMessage("VPA cannot exceed 255 characters")
                    .rejectedValue(rowData.getVpa())
                    .build());
        }

        if (rowData.getBeneficiaryName() != null && rowData.getBeneficiaryName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Beneficiary Name")
                    .errorMessage("Beneficiary name cannot exceed 255 characters")
                    .rejectedValue(rowData.getBeneficiaryName())
                    .build());
        }

        if (rowData.getBeneficiaryAccountNumber() != null && rowData.getBeneficiaryAccountNumber().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Beneficiary Account Number")
                    .errorMessage("Beneficiary account number cannot exceed 255 characters")
                    .rejectedValue(rowData.getBeneficiaryAccountNumber())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(PaymentDetailsBulkUploadDto rowData) {
        // Payment details typically don't have unique constraints, so no duplicate check
        return false;
    }
}
