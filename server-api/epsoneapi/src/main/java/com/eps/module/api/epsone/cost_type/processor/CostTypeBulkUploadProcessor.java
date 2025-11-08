package com.eps.module.api.epsone.cost_type.processor;

import com.eps.module.api.epsone.cost_category.repository.CostCategoryRepository;
import com.eps.module.api.epsone.cost_type.dto.CostTypeBulkUploadDto;
import com.eps.module.api.epsone.cost_type.repository.CostTypeRepository;
import com.eps.module.api.epsone.cost_type.validator.CostTypeBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.cost.CostCategory;
import com.eps.module.cost.CostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CostTypeBulkUploadProcessor extends BulkUploadProcessor<CostTypeBulkUploadDto, CostType> {

    private final CostTypeBulkUploadValidator validator;
    private final CostTypeRepository costTypeRepository;
    private final CostCategoryRepository costCategoryRepository;

    @Override
    protected BulkRowValidator<CostTypeBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected CostType convertToEntity(CostTypeBulkUploadDto dto) {
        // Look up cost category by name
        CostCategory costCategory = costCategoryRepository
                .findByCategoryNameIgnoreCase(dto.getCostCategoryName())
                .orElseThrow(() -> new RuntimeException("Cost category not found: " + dto.getCostCategoryName()));

        return CostType.builder()
                .typeName(capitalizeFirstLetter(dto.getTypeName()))
                .typeDescription(dto.getTypeDescription())
                .costCategory(costCategory)
                .build();
    }

    @Override
    protected void saveEntity(CostType entity) {
        costTypeRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(CostTypeBulkUploadDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("typeName", dto.getTypeName());
        map.put("typeDescription", dto.getTypeDescription());
        map.put("costCategoryName", dto.getCostCategoryName());
        return map;
    }

    @Override
    protected boolean isEmptyRow(CostTypeBulkUploadDto dto) {
        return (dto.getTypeName() == null || dto.getTypeName().trim().isEmpty()) &&
               (dto.getTypeDescription() == null || dto.getTypeDescription().trim().isEmpty()) &&
               (dto.getCostCategoryName() == null || dto.getCostCategoryName().trim().isEmpty());
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        String trimmed = input.trim();
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}
