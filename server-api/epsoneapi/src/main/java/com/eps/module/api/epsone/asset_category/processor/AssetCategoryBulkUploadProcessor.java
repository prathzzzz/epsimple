package com.eps.module.api.epsone.asset_category.processor;

import com.eps.module.api.epsone.asset_category.dto.AssetCategoryBulkUploadDto;
import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.api.epsone.asset_category.validator.AssetCategoryBulkUploadValidator;
import com.eps.module.asset.AssetCategory;
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
public class AssetCategoryBulkUploadProcessor extends BulkUploadProcessor<AssetCategoryBulkUploadDto, AssetCategory> {

    private final AssetCategoryBulkUploadValidator validator;
    private final AssetCategoryRepository assetCategoryRepository;

    @Override
    public BulkRowValidator<AssetCategoryBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    public AssetCategory convertToEntity(AssetCategoryBulkUploadDto dto) {
        return AssetCategory.builder()
                .categoryName(dto.getCategoryName() != null ? dto.getCategoryName().trim() : null)
                .categoryCode(dto.getCategoryCode() != null ? dto.getCategoryCode().trim() : null)
                .assetCodeAlt(dto.getAssetCodeAlt() != null ? dto.getAssetCodeAlt().trim() : null)
                .description(dto.getDescription() != null && !dto.getDescription().isBlank() 
                        ? dto.getDescription().trim() 
                        : null)
                .depreciation(dto.getDepreciation())
                .build();
    }

    @Override
    public void saveEntity(AssetCategory entity) {
        assetCategoryRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(AssetCategoryBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("categoryName", dto.getCategoryName());
        rowData.put("categoryCode", dto.getCategoryCode());
        rowData.put("assetCodeAlt", dto.getAssetCodeAlt());
        rowData.put("description", dto.getDescription());
        rowData.put("depreciation", dto.getDepreciation());
        return rowData;
    }

    @Override
    public boolean isEmptyRow(AssetCategoryBulkUploadDto dto) {
        return (dto.getCategoryName() == null || dto.getCategoryName().isBlank()) &&
               (dto.getCategoryCode() == null || dto.getCategoryCode().isBlank()) &&
               (dto.getAssetCodeAlt() == null || dto.getAssetCodeAlt().isBlank()) &&
               (dto.getDescription() == null || dto.getDescription().isBlank()) &&
               dto.getDepreciation() == null;
    }
}
