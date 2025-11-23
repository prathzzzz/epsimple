package com.eps.module.api.epsone.payee_type.validator;

import com.eps.module.api.epsone.payee_type.constant.PayeeTypeErrorMessages;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeBulkUploadDto;
import com.eps.module.api.epsone.payee_type.repository.PayeeTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PayeeTypeBulkUploadValidator implements BulkRowValidator<PayeeTypeBulkUploadDto> {

    private final PayeeTypeRepository payeeTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(PayeeTypeBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Payee Type
        if (rowData.getPayeeType() == null || rowData.getPayeeType().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Payee Type")
                    .errorMessage(PayeeTypeErrorMessages.PAYEE_TYPE_REQUIRED)
                    .rejectedValue(rowData.getPayeeType())
                    .build());
        } else if (rowData.getPayeeType().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Payee Type")
                    .errorMessage(PayeeTypeErrorMessages.PAYEE_TYPE_MAX_LENGTH)
                    .rejectedValue(rowData.getPayeeType())
                    .build());
        }

        // Validate Payee Category length
        if (rowData.getPayeeCategory() != null && rowData.getPayeeCategory().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Payee Category")
                    .errorMessage(PayeeTypeErrorMessages.PAYEE_CATEGORY_MAX_LENGTH)
                    .rejectedValue(rowData.getPayeeCategory())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(PayeeTypeBulkUploadDto rowData) {
        return payeeTypeRepository.existsByPayeeTypeIgnoreCase(rowData.getPayeeType());
    }
}
