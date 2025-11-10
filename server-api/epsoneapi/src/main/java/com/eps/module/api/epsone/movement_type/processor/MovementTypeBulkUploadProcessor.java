package com.eps.module.api.epsone.movement_type.processor;

import com.eps.module.api.epsone.movement_type.dto.MovementTypeBulkUploadDto;
import com.eps.module.api.epsone.movement_type.repository.MovementTypeRepository;
import com.eps.module.api.epsone.movement_type.validator.MovementTypeBulkUploadValidator;
import com.eps.module.asset.AssetMovementType;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovementTypeBulkUploadProcessor extends BulkUploadProcessor<MovementTypeBulkUploadDto, AssetMovementType> {

    private final MovementTypeRepository movementTypeRepository;
    private final MovementTypeBulkUploadValidator movementTypeBulkUploadValidator;

    @Override
    public BulkRowValidator<MovementTypeBulkUploadDto> getValidator() {
        return movementTypeBulkUploadValidator;
    }

    @Override
    public AssetMovementType convertToEntity(MovementTypeBulkUploadDto dto) {
        return AssetMovementType.builder()
                .movementType(dto.getMovementType() != null ? dto.getMovementType().trim() : null)
                .description(dto.getDescription() != null && !dto.getDescription().isBlank() 
                        ? dto.getDescription().trim() 
                        : null)
                .build();
    }

    @Override
    public void saveEntity(AssetMovementType entity) {
        movementTypeRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(MovementTypeBulkUploadDto dto) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("movementType", dto.getMovementType());
        data.put("description", dto.getDescription());
        return data;
    }

    @Override
    public boolean isEmptyRow(MovementTypeBulkUploadDto dto) {
        return (dto.getMovementType() == null || dto.getMovementType().isBlank()) &&
               (dto.getDescription() == null || dto.getDescription().isBlank());
    }
}
