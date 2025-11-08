package com.eps.module.api.epsone.payee_type.processor;

import com.eps.module.api.epsone.payee_type.dto.PayeeTypeBulkUploadDto;
import com.eps.module.api.epsone.payee_type.repository.PayeeTypeRepository;
import com.eps.module.api.epsone.payee_type.validator.PayeeTypeBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.payment.PayeeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PayeeTypeBulkUploadProcessor extends BulkUploadProcessor<PayeeTypeBulkUploadDto, PayeeType> {

    private final PayeeTypeRepository payeeTypeRepository;
    private final PayeeTypeBulkUploadValidator validator;

    @Override
    protected BulkRowValidator<PayeeTypeBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected PayeeType convertToEntity(PayeeTypeBulkUploadDto dto) {
        return PayeeType.builder()
                .payeeType(capitalizeFirstLetter(dto.getPayeeType()))
                .payeeCategory(dto.getPayeeCategory())
                .description(dto.getDescription())
                .build();
    }

    @Override
    protected void saveEntity(PayeeType entity) {
        payeeTypeRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(PayeeTypeBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Payee Type", dto.getPayeeType());
        rowData.put("Payee Category", dto.getPayeeCategory());
        rowData.put("Description", dto.getDescription());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(PayeeTypeBulkUploadDto dto) {
        return (dto.getPayeeType() == null || dto.getPayeeType().trim().isEmpty()) &&
               (dto.getPayeeCategory() == null || dto.getPayeeCategory().trim().isEmpty()) &&
               (dto.getDescription() == null || dto.getDescription().trim().isEmpty());
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
