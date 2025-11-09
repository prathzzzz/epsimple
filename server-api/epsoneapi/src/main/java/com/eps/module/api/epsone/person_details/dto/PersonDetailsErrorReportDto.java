package com.eps.module.api.epsone.person_details.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDetailsErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1)
    private Integer rowNumber;

    @ExcelColumn(value = "Person Type Name", order = 2)
    private String personTypeName;

    @ExcelColumn(value = "First Name", order = 3)
    private String firstName;

    @ExcelColumn(value = "Middle Name", order = 4)
    private String middleName;

    @ExcelColumn(value = "Last Name", order = 5)
    private String lastName;

    @ExcelColumn(value = "Contact Number", order = 6)
    private String contactNumber;

    @ExcelColumn(value = "Permanent Address", order = 7)
    private String permanentAddress;

    @ExcelColumn(value = "Correspondence Address", order = 8)
    private String correspondenceAddress;

    @ExcelColumn(value = "Error Message", order = 9)
    private String errorMessage;
}
