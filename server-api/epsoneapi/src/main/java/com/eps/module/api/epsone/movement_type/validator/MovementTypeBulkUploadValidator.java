package com.eps.module.api.epsone.movement_type.validator;

import com.eps.module.api.epsone.movement_type.constant.MovementTypeErrorMessages;
import com.eps.module.api.epsone.movement_type.dto.MovementTypeBulkUploadDto;
import com.eps.module.api.epsone.movement_type.repository.MovementTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovementTypeBulkUploadValidator implements BulkRowValidator<MovementTypeBulkUploadDto> {

    private final MovementTypeRepository movementTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(MovementTypeBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate movement type
        if (dto.getMovementType() == null || dto.getMovementType().isBlank()) {
            errors.add(createError(rowNumber, MovementTypeErrorMessages.MOVEMENT_TYPE_REQUIRED));
        } else {
            if (dto.getMovementType().length() > 100) {
                errors.add(createError(rowNumber, MovementTypeErrorMessages.MOVEMENT_TYPE_LENGTH_EXCEEDED));
            }
        }

        // Description is optional, but validate length if provided
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            if (dto.getDescription().length() > 5000) {
                errors.add(createError(rowNumber, MovementTypeErrorMessages.DESCRIPTION_LENGTH_EXCEEDED));
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(MovementTypeBulkUploadDto dto) {
        if (dto.getMovementType() == null || dto.getMovementType().isBlank()) {
            return false;
        }
        return movementTypeRepository.existsByMovementTypeIgnoreCase(dto.getMovementType().trim());
    }

    private BulkUploadErrorDto createError(int rowNumber, String message) {
        return BulkUploadErrorDto.builder()
                .rowNumber(rowNumber)
                .errorMessage(message)
                .errorType("VALIDATION")
                .build();
    }
}
