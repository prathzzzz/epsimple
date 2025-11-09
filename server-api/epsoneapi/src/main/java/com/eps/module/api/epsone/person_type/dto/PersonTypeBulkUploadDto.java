package com.eps.module.api.epsone.person_type.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonTypeBulkUploadDto {

    @ExcelColumn(value = "Type Name", order = 1, required = true, example = "Individual")
    private String typeName;

    @ExcelColumn(value = "Description", order = 2, required = false, example = "Individual person or natural person")
    private String description;
}
