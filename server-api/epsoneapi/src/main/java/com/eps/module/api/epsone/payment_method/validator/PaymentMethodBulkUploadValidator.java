package com.eps.module.api.epsone.payment_method.validator;

import com.eps.module.api.epsone.payment_method.constant.PaymentMethodErrorMessages;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodBulkUploadDto;
import com.eps.module.api.epsone.payment_method.repository.PaymentMethodRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentMethodBulkUploadValidator implements BulkRowValidator<PaymentMethodBulkUploadDto> {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<BulkUploadErrorDto> validate(PaymentMethodBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Method Name
        if (rowData.getMethodName() == null || rowData.getMethodName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Method Name")
                    .errorMessage(PaymentMethodErrorMessages.METHOD_NAME_REQUIRED)
                    .rejectedValue(rowData.getMethodName())
                    .build());
        } else if (rowData.getMethodName().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Method Name")
                    .errorMessage(PaymentMethodErrorMessages.METHOD_NAME_TOO_LONG)
                    .rejectedValue(rowData.getMethodName())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(PaymentMethodBulkUploadDto rowData) {
        return paymentMethodRepository.existsByMethodNameIgnoreCase(rowData.getMethodName());
    }
}
