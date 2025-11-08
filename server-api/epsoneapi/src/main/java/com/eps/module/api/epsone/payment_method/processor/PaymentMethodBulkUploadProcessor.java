package com.eps.module.api.epsone.payment_method.processor;

import com.eps.module.api.epsone.payment_method.dto.PaymentMethodBulkUploadDto;
import com.eps.module.api.epsone.payment_method.repository.PaymentMethodRepository;
import com.eps.module.api.epsone.payment_method.validator.PaymentMethodBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentMethodBulkUploadProcessor extends BulkUploadProcessor<PaymentMethodBulkUploadDto, PaymentMethod> {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodBulkUploadValidator validator;

    @Override
    protected BulkRowValidator<PaymentMethodBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected PaymentMethod convertToEntity(PaymentMethodBulkUploadDto dto) {
        return PaymentMethod.builder()
                .methodName(capitalizeFirstLetter(dto.getMethodName()))
                .description(dto.getDescription())
                .build();
    }

    @Override
    protected boolean isEmptyRow(PaymentMethodBulkUploadDto dto) {
        return (dto.getMethodName() == null || dto.getMethodName().trim().isEmpty()) &&
               (dto.getDescription() == null || dto.getDescription().trim().isEmpty());
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(PaymentMethodBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Method Name", dto.getMethodName());
        rowData.put("Description", dto.getDescription());
        return rowData;
    }

    @Override
    protected void saveEntity(PaymentMethod entity) {
        paymentMethodRepository.save(entity);
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
