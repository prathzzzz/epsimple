package com.eps.module.api.epsone.cost_item.processor;

import com.eps.module.api.epsone.cost_item.dto.CostItemBulkUploadDto;
import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.cost_item.validator.CostItemBulkUploadValidator;
import com.eps.module.api.epsone.cost_type.repository.CostTypeRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.cost.CostItem;
import com.eps.module.cost.CostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CostItemBulkUploadProcessor extends BulkUploadProcessor<CostItemBulkUploadDto, CostItem> {

    private final CostItemBulkUploadValidator validator;
    private final CostItemRepository costItemRepository;
    private final CostTypeRepository costTypeRepository;

    @Override
    protected BulkRowValidator<CostItemBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected CostItem convertToEntity(CostItemBulkUploadDto dto) {
        // Look up cost type by name
        CostType costType = costTypeRepository
                .findByTypeNameIgnoreCase(dto.getCostTypeName())
                .orElseThrow(() -> new RuntimeException("Cost type not found: " + dto.getCostTypeName()));

        return CostItem.builder()
                .costItemFor(capitalizeFirstLetter(dto.getCostItemFor()))
                .itemDescription(dto.getItemDescription())
                .costType(costType)
                .build();
    }

    @Override
    protected void saveEntity(CostItem entity) {
        costItemRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(CostItemBulkUploadDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("costItemFor", dto.getCostItemFor());
        map.put("itemDescription", dto.getItemDescription());
        map.put("costTypeName", dto.getCostTypeName());
        return map;
    }

    @Override
    protected boolean isEmptyRow(CostItemBulkUploadDto dto) {
        return (dto.getCostItemFor() == null || dto.getCostItemFor().trim().isEmpty()) &&
               (dto.getItemDescription() == null || dto.getItemDescription().trim().isEmpty()) &&
               (dto.getCostTypeName() == null || dto.getCostTypeName().trim().isEmpty());
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        String trimmed = input.trim();
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}
