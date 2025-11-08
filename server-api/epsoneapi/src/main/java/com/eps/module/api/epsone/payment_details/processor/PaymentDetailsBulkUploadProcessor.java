package com.eps.module.api.epsone.payment_details.processor;

import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsBulkUploadDto;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.api.epsone.payment_details.validator.PaymentDetailsBulkUploadValidator;
import com.eps.module.api.epsone.payment_method.repository.PaymentMethodRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.payment.PaymentDetails;
import com.eps.module.payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentDetailsBulkUploadProcessor extends BulkUploadProcessor<PaymentDetailsBulkUploadDto, PaymentDetails> {

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentDetailsBulkUploadValidator validator;

    @Override
    protected BulkRowValidator<PaymentDetailsBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected PaymentDetails convertToEntity(PaymentDetailsBulkUploadDto dto) {
        // Look up payment method by name
        PaymentMethod paymentMethod = paymentMethodRepository
                .findByMethodNameIgnoreCase(dto.getPaymentMethodName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment method not found: " + dto.getPaymentMethodName()));

        return PaymentDetails.builder()
                .paymentMethod(paymentMethod)
                .paymentDate(dto.getPaymentDate())
                .paymentAmount(dto.getPaymentAmount())
                .transactionNumber(dto.getTransactionNumber())
                .vpa(dto.getVpa())
                .beneficiaryName(dto.getBeneficiaryName())
                .beneficiaryAccountNumber(dto.getBeneficiaryAccountNumber())
                .paymentRemarks(dto.getPaymentRemarks())
                .build();
    }

    @Override
    protected boolean isEmptyRow(PaymentDetailsBulkUploadDto dto) {
        return (dto.getPaymentMethodName() == null || dto.getPaymentMethodName().trim().isEmpty()) &&
               dto.getPaymentDate() == null &&
               dto.getPaymentAmount() == null &&
               (dto.getTransactionNumber() == null || dto.getTransactionNumber().trim().isEmpty());
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(PaymentDetailsBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Payment Method Name", dto.getPaymentMethodName());
        rowData.put("Payment Date", dto.getPaymentDate() != null ? dto.getPaymentDate().toString() : null);
        rowData.put("Payment Amount", dto.getPaymentAmount() != null ? dto.getPaymentAmount().toString() : null);
        rowData.put("Transaction Number", dto.getTransactionNumber());
        rowData.put("VPA", dto.getVpa());
        rowData.put("Beneficiary Name", dto.getBeneficiaryName());
        rowData.put("Beneficiary Account Number", dto.getBeneficiaryAccountNumber());
        rowData.put("Payment Remarks", dto.getPaymentRemarks());
        return rowData;
    }

    @Override
    protected void saveEntity(PaymentDetails entity) {
        paymentDetailsRepository.save(entity);
    }
}
