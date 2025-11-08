package com.eps.module.api.epsone.cost_category.processor;

import com.eps.module.api.epsone.cost_category.dto.CostCategoryBulkUploadDto;
import com.eps.module.api.epsone.cost_category.validator.CostCategoryBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.cost.CostCategory;
import com.eps.module.api.epsone.cost_category.repository.CostCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CostCategoryBulkUploadProcessor extends BulkUploadProcessor<CostCategoryBulkUploadDto, CostCategory> {

    private final CostCategoryBulkUploadValidator validator;
    private final CostCategoryRepository costCategoryRepository;

    @Override
    protected BulkRowValidator<CostCategoryBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected CostCategory convertToEntity(CostCategoryBulkUploadDto dto) {
        return CostCategory.builder()
                .categoryName(capitalizeFirstLetter(dto.getCategoryName()))
                .categoryDescription(dto.getCategoryDescription())
                .build();
    }

    @Override
    protected void saveEntity(CostCategory entity) {
        costCategoryRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(CostCategoryBulkUploadDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", dto.getCategoryName());
        map.put("categoryDescription", dto.getCategoryDescription());
        return map;
    }

    @Override
    protected boolean isEmptyRow(CostCategoryBulkUploadDto dto) {
        return (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) &&
               (dto.getCategoryDescription() == null || dto.getCategoryDescription().trim().isEmpty());
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        String trimmed = input.trim();
        return trimmed.substring(0,1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}
