package com.eps.module.api.epsone.state.processor;

import com.eps.module.api.epsone.state.dto.StateBulkUploadDto;
import com.eps.module.api.epsone.state.repository.StateRepository;
import com.eps.module.api.epsone.state.validator.StateBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.location.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StateBulkUploadProcessor extends BulkUploadProcessor<StateBulkUploadDto, State> {
    
    private final StateBulkUploadValidator validator;
    private final StateRepository stateRepository;
    
    @Override
    protected BulkRowValidator<StateBulkUploadDto> getValidator() {
        return validator;
    }
    
    @Override
    protected State convertToEntity(StateBulkUploadDto dto) {
        return State.builder()
                .stateName(dto.getStateName().trim())
                .stateCode(dto.getStateCode().trim().toUpperCase())
                .stateCodeAlt(dto.getStateCodeAlt() != null ? dto.getStateCodeAlt().trim().toUpperCase() : null)
                .build();
    }
    
    @Override
    protected void saveEntity(State entity) {
        stateRepository.save(entity);
    }
}
