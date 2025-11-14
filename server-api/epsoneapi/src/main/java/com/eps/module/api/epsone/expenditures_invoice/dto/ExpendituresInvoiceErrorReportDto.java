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
public class ExpendituresInvoiceErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Cost Item Name", order = 2)
    private String costItemName;

    @ExcelColumn(value = "Invoice Number", order = 3)
    private String invoiceNumber;

    @ExcelColumn(value = "Managed Project Code", order = 4)
    private String managedProjectCode;

    @ExcelColumn(value = "Incurred Date", order = 5)
    private String incurredDate;

    @ExcelColumn(value = "Description", order = 6)
    private String description;

    @ExcelColumn(value = "Error", order = 7)
    private String error;
}
