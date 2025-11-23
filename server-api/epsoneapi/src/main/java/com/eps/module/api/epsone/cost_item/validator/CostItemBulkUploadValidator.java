package com.eps.module.api.epsone.cost_item.validator;

import com.eps.module.api.epsone.cost_item.constant.CostItemErrorMessages;
import com.eps.module.api.epsone.cost_item.dto.CostItemBulkUploadDto;
import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.cost_type.repository.CostTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CostItemBulkUploadValidator implements BulkRowValidator<CostItemBulkUploadDto> {

    private final CostItemRepository costItemRepository;
    private final CostTypeRepository costTypeRepository;

    @Override
    public List<BulkUploadErrorDto> validate(CostItemBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Cost Item For
        if (rowData.getCostItemFor() == null || rowData.getCostItemFor().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Cost Item For")
                    .errorMessage(CostItemErrorMessages.COST_ITEM_FOR_REQUIRED)
                    .rejectedValue(rowData.getCostItemFor())
                    .build());
        } else if (rowData.getCostItemFor().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Cost Item For")
                    .errorMessage(CostItemErrorMessages.COST_ITEM_FOR_TOO_LONG)
                    .rejectedValue(rowData.getCostItemFor())
                    .build());
        }

        // Validate Cost Type Name
        if (rowData.getCostTypeName() == null || rowData.getCostTypeName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Cost Type Name")
                    .errorMessage(CostItemErrorMessages.COST_TYPE_NAME_REQUIRED)
                    .rejectedValue(rowData.getCostTypeName())
                    .build());
        } else {
            // Check if cost type exists
            boolean typeExists = costTypeRepository.existsByTypeNameIgnoreCase(rowData.getCostTypeName());
            if (!typeExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Cost Type Name")
                        .errorMessage(String.format(CostItemErrorMessages.COST_TYPE_NOT_FOUND_NAME, rowData.getCostTypeName()))
                        .rejectedValue(rowData.getCostTypeName())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(CostItemBulkUploadDto rowData) {
        // Check if cost item with same costItemFor already exists
        return costItemRepository.existsByCostItemForIgnoreCase(rowData.getCostItemFor());
    }
}
