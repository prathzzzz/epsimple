package com.eps.module.api.epsone.asset_type.processor;

import com.eps.module.api.epsone.asset_type.dto.AssetTypeBulkUploadDto;
import com.eps.module.api.epsone.asset_type.repository.AssetTypeRepository;
import com.eps.module.api.epsone.asset_type.validator.AssetTypeBulkUploadValidator;
import com.eps.module.asset.AssetType;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssetTypeBulkUploadProcessor extends BulkUploadProcessor<AssetTypeBulkUploadDto, AssetType> {

    private final AssetTypeRepository assetTypeRepository;
    private final AssetTypeBulkUploadValidator assetTypeBulkUploadValidator;

    @Override
    public BulkRowValidator<AssetTypeBulkUploadDto> getValidator() {
        return assetTypeBulkUploadValidator;
    }

    @Override
    @Transactional
    public AssetType convertToEntity(AssetTypeBulkUploadDto dto) {
        return AssetType.builder()
                .typeName(dto.getTypeName() != null ? dto.getTypeName().trim() : null)
                .typeCode(dto.getTypeCode() != null ? dto.getTypeCode().trim().toUpperCase() : null)
                .description(dto.getDescription() != null && !dto.getDescription().isBlank() 
                        ? dto.getDescription().trim() 
                        : null)
                .build();
    }

    @Override
    public void saveEntity(AssetType entity) {
        assetTypeRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(AssetTypeBulkUploadDto dto) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("typeName", dto.getTypeName());
        data.put("typeCode", dto.getTypeCode());
        data.put("description", dto.getDescription());
        return data;
    }

    @Override
    public boolean isEmptyRow(AssetTypeBulkUploadDto dto) {
        return (dto.getTypeName() == null || dto.getTypeName().isBlank()) &&
               (dto.getTypeCode() == null || dto.getTypeCode().isBlank()) &&
               (dto.getDescription() == null || dto.getDescription().isBlank());
    }
}
