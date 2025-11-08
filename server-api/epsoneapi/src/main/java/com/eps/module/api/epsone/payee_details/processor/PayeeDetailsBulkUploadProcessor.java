package com.eps.module.api.epsone.payee_details.processor;

import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsBulkUploadDto;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.payee_details.validator.PayeeDetailsBulkUploadValidator;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.bank.Bank;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.payment.PayeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PayeeDetailsBulkUploadProcessor extends BulkUploadProcessor<PayeeDetailsBulkUploadDto, PayeeDetails> {

    private final PayeeDetailsRepository payeeDetailsRepository;
    private final BankRepository bankRepository;
    private final PayeeDetailsBulkUploadValidator validator;

    @Override
    protected BulkRowValidator<PayeeDetailsBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected PayeeDetails convertToEntity(PayeeDetailsBulkUploadDto dto) {
        PayeeDetails.PayeeDetailsBuilder builder = PayeeDetails.builder()
                .payeeName(dto.getPayeeName())
                .panNumber(dto.getPanNumber())
                .aadhaarNumber(dto.getAadhaarNumber())
                .ifscCode(dto.getIfscCode())
                .beneficiaryName(dto.getBeneficiaryName())
                .accountNumber(dto.getAccountNumber());

        // Set Bank if provided
        if (dto.getBankName() != null && !dto.getBankName().trim().isEmpty()) {
            Bank bank = bankRepository.findByBankName(dto.getBankName()).orElse(null);
            builder.bank(bank);
        }

        return builder.build();
    }

    @Override
    protected void saveEntity(PayeeDetails entity) {
        payeeDetailsRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(PayeeDetailsBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Payee Name", dto.getPayeeName());
        rowData.put("PAN Number", dto.getPanNumber());
        rowData.put("Aadhaar Number", dto.getAadhaarNumber());
        rowData.put("Bank Name", dto.getBankName());
        rowData.put("IFSC Code", dto.getIfscCode());
        rowData.put("Beneficiary Name", dto.getBeneficiaryName());
        rowData.put("Account Number", dto.getAccountNumber());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(PayeeDetailsBulkUploadDto dto) {
        return (dto.getPayeeName() == null || dto.getPayeeName().trim().isEmpty()) &&
               (dto.getPanNumber() == null || dto.getPanNumber().trim().isEmpty()) &&
               (dto.getAadhaarNumber() == null || dto.getAadhaarNumber().trim().isEmpty()) &&
               (dto.getBankName() == null || dto.getBankName().trim().isEmpty()) &&
               (dto.getIfscCode() == null || dto.getIfscCode().trim().isEmpty()) &&
               (dto.getBeneficiaryName() == null || dto.getBeneficiaryName().trim().isEmpty()) &&
               (dto.getAccountNumber() == null || dto.getAccountNumber().trim().isEmpty());
    }
}
