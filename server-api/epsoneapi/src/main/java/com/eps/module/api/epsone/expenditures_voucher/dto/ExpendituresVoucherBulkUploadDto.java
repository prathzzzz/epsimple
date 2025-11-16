package com.eps.module.api.epsone.expenditures_voucher.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpendituresVoucherBulkUploadDto {

    @ExcelColumn(value = "Cost Item Name", order = 1, required = true, example = "Office Supplies")
    private String costItemName;

    @ExcelColumn(value = "Voucher Number", order = 2, required = true, example = "VCH-2024-001")
    private String voucherNumber;

    @ExcelColumn(value = "Managed Project Code", order = 3, required = true, example = "PRJ-2024-001")
    private String managedProjectCode;

    @ExcelColumn(value = "Incurred Date", order = 4, example = "2024-01-15")
    private String incurredDate;

    @ExcelColumn(value = "Description", order = 5, example = "Payment for office supplies")
    private String description;
}
