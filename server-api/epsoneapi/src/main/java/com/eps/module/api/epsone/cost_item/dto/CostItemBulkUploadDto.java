package com.eps.module.api.epsone.cost_item.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostItemBulkUploadDto {

    @ExcelColumn(value = "Cost Item For", order = 1, required = true, example = "Server Maintenance")
    private String costItemFor;

    @ExcelColumn(value = "Item Description", order = 2, required = false, example = "Regular server maintenance and updates")
    private String itemDescription;

    @ExcelColumn(value = "Cost Type Name", order = 3, required = true, example = "Hardware Purchase")
    private String costTypeName;

    // For export only - populated from relationships
    private String costCategoryName;
}
