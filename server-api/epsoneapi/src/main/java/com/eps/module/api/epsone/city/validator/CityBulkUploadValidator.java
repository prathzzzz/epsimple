package com.eps.module.api.epsone.city.validator;

import com.eps.module.api.epsone.city.dto.CityBulkUploadDto;
import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.api.epsone.state.repository.StateRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityBulkUploadValidator implements BulkRowValidator<CityBulkUploadDto> {
    
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    
    private static final Pattern CODE_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+$");
    
    @Override
    public List<BulkUploadErrorDto> validate(CityBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();
        
        // Validate City Name
        if (rowData.getCityName() == null || rowData.getCityName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("City Name")
                    .errorMessage("City name is required")
                    .rejectedValue(rowData.getCityName())
                    .build());
        } else if (rowData.getCityName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("City Name")
                    .errorMessage("City name cannot exceed 100 characters")
                    .rejectedValue(rowData.getCityName())
                    .build());
        }
        
        // Validate City Code (optional)
        if (rowData.getCityCode() != null && !rowData.getCityCode().trim().isEmpty()) {
            if (rowData.getCityCode().length() > 10) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("City Code")
                        .errorMessage("City code cannot exceed 10 characters")
                        .rejectedValue(rowData.getCityCode())
                        .build());
            } else if (!CODE_PATTERN.matcher(rowData.getCityCode()).matches()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("City Code")
                        .errorMessage("City code can only contain letters, numbers, hyphens and underscores")
                        .rejectedValue(rowData.getCityCode())
                        .build());
            }
        }
        
        // Validate State Name (required)
        if (rowData.getStateName() == null || rowData.getStateName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("State Name")
                    .errorMessage("State name is required")
                    .rejectedValue(rowData.getStateName())
                    .build());
            
            // Also check if state code or state code alt are empty when state name is missing
            if (rowData.getStateCode() == null || rowData.getStateCode().trim().isEmpty()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("State Code")
                        .errorMessage("State code is required (or provide State Name to look it up)")
                        .rejectedValue(rowData.getStateCode())
                        .build());
            }
            
            if (rowData.getStateCodeAlt() == null || rowData.getStateCodeAlt().trim().isEmpty()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("State Code Alt")
                        .errorMessage("Alternate state code is required (or provide State Name to look it up)")
                        .rejectedValue(rowData.getStateCodeAlt())
                        .build());
            }
        } else {
            // Check if state exists by name
            var stateOpt = stateRepository.findByStateName(rowData.getStateName().trim());
            if (stateOpt.isEmpty()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("State Name")
                        .errorMessage("State not found with name: " + rowData.getStateName())
                        .rejectedValue(rowData.getStateName())
                        .build());
            } else {
                // If state code or state code alt are provided, validate they match
                var state = stateOpt.get();
                
                if (rowData.getStateCode() != null && !rowData.getStateCode().trim().isEmpty()) {
                    if (!rowData.getStateCode().trim().equalsIgnoreCase(state.getStateCode())) {
                        errors.add(BulkUploadErrorDto.builder()
                                .rowNumber(rowNumber)
                                .fieldName("State Code")
                                .errorMessage("State code '" + rowData.getStateCode() + "' does not match state '" + rowData.getStateName() + "' (expected: " + state.getStateCode() + ")")
                                .rejectedValue(rowData.getStateCode())
                                .build());
                    }
                }
                
                if (rowData.getStateCodeAlt() != null && !rowData.getStateCodeAlt().trim().isEmpty()) {
                    String expectedAlt = state.getStateCodeAlt();
                    if (expectedAlt != null && !rowData.getStateCodeAlt().trim().equalsIgnoreCase(expectedAlt)) {
                        errors.add(BulkUploadErrorDto.builder()
                                .rowNumber(rowNumber)
                                .fieldName("State Code Alt")
                                .errorMessage("Alternate state code '" + rowData.getStateCodeAlt() + "' does not match state '" + rowData.getStateName() + "' (expected: " + expectedAlt + ")")
                                .rejectedValue(rowData.getStateCodeAlt())
                                .build());
                    } else if (expectedAlt == null) {
                        errors.add(BulkUploadErrorDto.builder()
                                .rowNumber(rowNumber)
                                .fieldName("State Code Alt")
                                .errorMessage("State '" + rowData.getStateName() + "' does not have an alternate code")
                                .rejectedValue(rowData.getStateCodeAlt())
                                .build());
                    }
                }
            }
        }
        
        return errors;
    }
    
    @Override
    public boolean isDuplicate(CityBulkUploadDto rowData) {
        // Check if city code already exists (if provided)
        if (rowData.getCityCode() != null && !rowData.getCityCode().trim().isEmpty()) {
            return cityRepository.findByCityCode(rowData.getCityCode().trim()).isPresent();
        }
        return false;
    }
}
