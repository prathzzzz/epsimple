package com.eps.module.api.epsone.payment_details.validator;

import com.eps.module.api.epsone.payment_details.constant.PaymentDetailsErrorMessages;
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
                    .errorMessage(PaymentDetailsErrorMessages.PAYMENT_METHOD_NAME_REQUIRED)
                    .rejectedValue(rowData.getPaymentMethodName())
                    .build());
        } else {
            // Check if payment method exists
            boolean methodExists = paymentMethodRepository.existsByMethodNameIgnoreCase(rowData.getPaymentMethodName());
            if (!methodExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Payment Method Name")
                        .errorMessage(String.format(PaymentDetailsErrorMessages.PAYMENT_METHOD_NOT_FOUND_NAME, rowData.getPaymentMethodName()))
                        .rejectedValue(rowData.getPaymentMethodName())
                        .build());
            }
        }

        // Validate field lengths
        if (rowData.getTransactionNumber() != null && rowData.getTransactionNumber().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Transaction Number")
                    .errorMessage(PaymentDetailsErrorMessages.TRANSACTION_NUMBER_TOO_LONG)
                    .rejectedValue(rowData.getTransactionNumber())
                    .build());
        }

        if (rowData.getVpa() != null && rowData.getVpa().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("VPA")
                    .errorMessage(PaymentDetailsErrorMessages.VPA_TOO_LONG)
                    .rejectedValue(rowData.getVpa())
                    .build());
        }

        if (rowData.getBeneficiaryName() != null && rowData.getBeneficiaryName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Beneficiary Name")
                    .errorMessage(PaymentDetailsErrorMessages.BENEFICIARY_NAME_TOO_LONG)
                    .rejectedValue(rowData.getBeneficiaryName())
                    .build());
        }

        if (rowData.getBeneficiaryAccountNumber() != null && rowData.getBeneficiaryAccountNumber().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Beneficiary Account Number")
                    .errorMessage(PaymentDetailsErrorMessages.BENEFICIARY_ACCOUNT_NUMBER_TOO_LONG)
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
