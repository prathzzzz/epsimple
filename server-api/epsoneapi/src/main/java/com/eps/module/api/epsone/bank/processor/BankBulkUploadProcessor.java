package com.eps.module.api.epsone.bank.processor;

import com.eps.module.api.epsone.bank.dto.BankBulkUploadDto;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.api.epsone.bank.validator.BankBulkUploadValidator;
import com.eps.module.bank.Bank;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankBulkUploadProcessor extends BulkUploadProcessor<BankBulkUploadDto, Bank> {

    private final BankRepository bankRepository;
    private final BankBulkUploadValidator validator;

    @Override
    protected BulkRowValidator<BankBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected Bank convertToEntity(BankBulkUploadDto dto) {
        return Bank.builder()
                .bankName(dto.getBankName())
                .rbiBankCode(dto.getRbiBankCode())
                .epsBankCode(dto.getEpsBankCode())
                .bankCodeAlt(dto.getBankCodeAlt())
                .bankLogo(dto.getBankLogo())
                .build();
    }

    @Override
    protected void saveEntity(Bank entity) {
        bankRepository.save(entity);
    }

    @Override
    protected boolean isEmptyRow(BankBulkUploadDto dto) {
        return (dto.getBankName() == null || dto.getBankName().trim().isEmpty());
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(BankBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("bankName", dto.getBankName());
        rowData.put("rbiBankCode", dto.getRbiBankCode());
        rowData.put("epsBankCode", dto.getEpsBankCode());
        rowData.put("bankCodeAlt", dto.getBankCodeAlt());
        rowData.put("bankLogo", dto.getBankLogo());
        return rowData;
    }
}
