package com.eps.module.api.epsone.expenditures_invoice.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpendituresInvoiceBulkUploadDto {

    @ExcelColumn(value = "Cost Item Name", order = 1, required = true, example = "Software License")
    private String costItemName;

    @ExcelColumn(value = "Invoice Number", order = 2, required = true, example = "INV-2024-001")
    private String invoiceNumber;

    @ExcelColumn(value = "Managed Project Code", order = 3, required = true, example = "PRJ-2024-001")
    private String managedProjectCode;

    @ExcelColumn(value = "Incurred Date", order = 4, example = "2024-01-15")
    private String incurredDate;

    @ExcelColumn(value = "Description", order = 5, example = "Payment for software licenses")
    private String description;
}
