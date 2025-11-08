package com.eps.module.api.epsone.city.processor;

import com.eps.module.api.epsone.city.dto.CityBulkUploadDto;
import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.api.epsone.city.validator.CityBulkUploadValidator;
import com.eps.module.api.epsone.state.repository.StateRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.location.City;
import com.eps.module.location.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityBulkUploadProcessor extends BulkUploadProcessor<CityBulkUploadDto, City> {
    
    private final CityBulkUploadValidator validator;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    
    @Override
    protected BulkRowValidator<CityBulkUploadDto> getValidator() {
        return validator;
    }
    
    @Override
    protected City convertToEntity(CityBulkUploadDto dto) {
        // Find state by name
        State state = stateRepository.findByStateName(dto.getStateName().trim())
                .orElseThrow(() -> new IllegalArgumentException("State not found with name: " + dto.getStateName()));
        
        return City.builder()
                .cityName(capitalizeFirstLetter(dto.getCityName()))
                .cityCode(dto.getCityCode() != null ? transformToUpperCase(dto.getCityCode()) : null)
                .state(state)
                .build();
    }
    
    @Override
    protected void saveEntity(City entity) {
        cityRepository.save(entity);
    }
    
    @Override
    protected Map<String, Object> getRowDataAsMap(CityBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("cityName", dto.getCityName());
        rowData.put("cityCode", dto.getCityCode());
        rowData.put("stateName", dto.getStateName());
        
        // Look up state to get state code and state code alt
        if (dto.getStateName() != null) {
            stateRepository.findByStateName(dto.getStateName().trim()).ifPresent(state -> {
                rowData.put("stateCode", state.getStateCode());
                rowData.put("stateCodeAlt", state.getStateCodeAlt());
            });
        }
        
        return rowData;
    }
    
    @Override
    protected boolean isEmptyRow(CityBulkUploadDto dto) {
        // Consider row empty if all required fields (cityName and stateName) are null or blank
        return (dto.getCityName() == null || dto.getCityName().trim().isEmpty()) &&
               (dto.getStateName() == null || dto.getStateName().trim().isEmpty());
    }
    
    /**
     * Capitalizes the first letter of each word in the input string.
     * Example: "mumbai" -> "Mumbai"
     * Example: "new delhi" -> "New Delhi"
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
     * Example: "mum  " -> "MUM"
     */
    private String transformToUpperCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.trim().toUpperCase();
    }
}

