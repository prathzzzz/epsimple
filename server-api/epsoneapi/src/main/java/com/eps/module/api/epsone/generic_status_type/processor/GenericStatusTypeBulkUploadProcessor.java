package com.eps.module.api.epsone.generic_status_type.processor;

import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeBulkUploadDto;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.generic_status_type.validator.GenericStatusTypeBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.status.GenericStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericStatusTypeBulkUploadProcessor extends BulkUploadProcessor<GenericStatusTypeBulkUploadDto, GenericStatusType> {

    private final GenericStatusTypeBulkUploadValidator validator;
    private final GenericStatusTypeRepository genericStatusTypeRepository;

    @Override
    public BulkRowValidator<GenericStatusTypeBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    @Transactional
    public GenericStatusType convertToEntity(GenericStatusTypeBulkUploadDto dto) {
        return GenericStatusType.builder()
                .statusName(dto.getStatusName() != null ? dto.getStatusName().trim() : null)
                .statusCode(dto.getStatusCode() != null && !dto.getStatusCode().isBlank() 
                        ? dto.getStatusCode().trim() 
                        : null)
                .description(dto.getDescription() != null && !dto.getDescription().isBlank() 
                        ? dto.getDescription().trim() 
                        : null)
                .build();
    }

    @Override
    public void saveEntity(GenericStatusType entity) {
        genericStatusTypeRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(GenericStatusTypeBulkUploadDto dto) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("statusName", dto.getStatusName());
        data.put("statusCode", dto.getStatusCode());
        data.put("description", dto.getDescription());
        return data;
    }

    @Override
    public boolean isEmptyRow(GenericStatusTypeBulkUploadDto dto) {
        return (dto.getStatusName() == null || dto.getStatusName().isBlank()) &&
               (dto.getStatusCode() == null || dto.getStatusCode().isBlank()) &&
               (dto.getDescription() == null || dto.getDescription().isBlank());
    }
}
