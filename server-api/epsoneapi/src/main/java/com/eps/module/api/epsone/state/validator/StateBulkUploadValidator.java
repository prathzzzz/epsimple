package com.eps.module.api.epsone.state.validator;

import com.eps.module.api.epsone.state.constant.StateErrorMessages;
import com.eps.module.api.epsone.state.dto.StateBulkUploadDto;
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
public class StateBulkUploadValidator implements BulkRowValidator<StateBulkUploadDto> {
    
    private final StateRepository stateRepository;
    
    private static final Pattern CODE_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+$");
    
    @Override
    public List<BulkUploadErrorDto> validate(StateBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();
        
        // Validate State Name
        if (rowData.getStateName() == null || rowData.getStateName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("State Name")
                    .errorMessage(StateErrorMessages.STATE_NAME_REQUIRED)
                    .rejectedValue(rowData.getStateName())
                    .build());
        } else if (rowData.getStateName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("State Name")
                    .errorMessage(StateErrorMessages.STATE_NAME_LENGTH_EXCEEDED)
                    .rejectedValue(rowData.getStateName())
                    .build());
        }
        
        // Validate State Code
        if (rowData.getStateCode() == null || rowData.getStateCode().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("State Code")
                    .errorMessage(StateErrorMessages.STATE_CODE_REQUIRED)
                    .rejectedValue(rowData.getStateCode())
                    .build());
        } else if (rowData.getStateCode().length() < 2 || rowData.getStateCode().length() > 10) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("State Code")
                    .errorMessage(StateErrorMessages.STATE_CODE_LENGTH_INVALID)
                    .rejectedValue(rowData.getStateCode())
                    .build());
        } else if (!CODE_PATTERN.matcher(rowData.getStateCode()).matches()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("State Code")
                    .errorMessage(StateErrorMessages.STATE_CODE_INVALID_FORMAT)
                    .rejectedValue(rowData.getStateCode())
                    .build());
        }
        
        // Validate Alternate State Code (optional)
        if (rowData.getStateCodeAlt() != null && !rowData.getStateCodeAlt().trim().isEmpty()) {
            if (rowData.getStateCodeAlt().length() > 10) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Alternate State Code")
                        .errorMessage(StateErrorMessages.STATE_CODE_ALT_LENGTH_EXCEEDED)
                        .rejectedValue(rowData.getStateCodeAlt())
                        .build());
            } else if (!CODE_PATTERN.matcher(rowData.getStateCodeAlt()).matches()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Alternate State Code")
                        .errorMessage(StateErrorMessages.STATE_CODE_ALT_INVALID_FORMAT)
                        .rejectedValue(rowData.getStateCodeAlt())
                        .build());
            }
        }
        
        return errors;
    }
    
    @Override
    public boolean isDuplicate(StateBulkUploadDto rowData) {
        // Check if state with same name or code already exists
        boolean existsByName = stateRepository.existsByStateNameIgnoreCase(rowData.getStateName());
        boolean existsByCode = stateRepository.existsByStateCodeIgnoreCase(rowData.getStateCode());
        
        return existsByName || existsByCode;
    }
}
