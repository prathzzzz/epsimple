package com.eps.module.api.epsone.payee_type.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayeeTypeBulkUploadDto {

    @ExcelColumn(value = "Payee Type", order = 1, required = true, example = "Vendor")
    private String payeeType;

    @ExcelColumn(value = "Payee Category", order = 2, required = false, example = "Site and Asset Vendors")
    private String payeeCategory;

    @ExcelColumn(value = "Description", order = 3, required = false, example = "Vendors managing Site and providing Assets")
    private String description;
}
