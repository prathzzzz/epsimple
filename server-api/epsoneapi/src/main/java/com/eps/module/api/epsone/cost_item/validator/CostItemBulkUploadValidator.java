package com.eps.module.api.epsone.cost_item.validator;

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
                    .errorMessage("Cost item for is required")
                    .rejectedValue(rowData.getCostItemFor())
                    .build());
        } else if (rowData.getCostItemFor().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Cost Item For")
                    .errorMessage("Cost item for cannot exceed 255 characters")
                    .rejectedValue(rowData.getCostItemFor())
                    .build());
        }

        // Validate Cost Type Name
        if (rowData.getCostTypeName() == null || rowData.getCostTypeName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Cost Type Name")
                    .errorMessage("Cost type name is required")
                    .rejectedValue(rowData.getCostTypeName())
                    .build());
        } else {
            // Check if cost type exists
            boolean typeExists = costTypeRepository.existsByTypeNameIgnoreCase(rowData.getCostTypeName());
            if (!typeExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Cost Type Name")
                        .errorMessage("Cost type '" + rowData.getCostTypeName() + "' does not exist")
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
