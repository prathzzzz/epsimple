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

import java.util.HashMap;
import java.util.Map;

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
                .stateName(capitalizeFirstLetter(dto.getStateName()))
                .stateCode(transformToUpperCase(dto.getStateCode()))
                .stateCodeAlt(transformToUpperCase(dto.getStateCodeAlt()))
                .build();
    }
    
    @Override
    protected void saveEntity(State entity) {
        stateRepository.save(entity);
    }
    
    @Override
    protected Map<String, Object> getRowDataAsMap(StateBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("stateName", dto.getStateName());
        rowData.put("stateCode", dto.getStateCode());
        rowData.put("stateCodeAlt", dto.getStateCodeAlt());
        return rowData;
    }
    
    @Override
    protected boolean isEmptyRow(StateBulkUploadDto dto) {
        // Consider row empty if all required fields (stateName and stateCode) are null or blank
        return (dto.getStateName() == null || dto.getStateName().trim().isEmpty()) &&
               (dto.getStateCode() == null || dto.getStateCode().trim().isEmpty());
    }
    
    /**
     * Capitalizes the first letter of each word in the input string.
     * Example: "maharashtra" -> "Maharashtra"
     * Example: "new delhi" -> "New Delhi"
     * Example: "uttar pradesh" -> "Uttar Pradesh"
     */
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        
        // Split by spaces and capitalize each word
        String[] words = trimmed.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            String word = words[i];
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase())
                      .append(word.substring(1).toLowerCase());
            }
        }
        
        return result.toString();
    }
    
    /**
     * Transforms string to uppercase and trims whitespace.
     * Example: "mh  " -> "MH"
     */
    private String transformToUpperCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.trim().toUpperCase();
    }
}
