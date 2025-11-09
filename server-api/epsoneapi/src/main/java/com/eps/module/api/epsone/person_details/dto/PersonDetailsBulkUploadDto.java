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
public class PersonDetailsBulkUploadDto {

    @ExcelColumn(value = "Person Type Name", order = 1, example = "Employee")
    private String personTypeName;

    @ExcelColumn(value = "First Name", order = 2, example = "John")
    private String firstName;

    @ExcelColumn(value = "Middle Name", order = 3, example = "Michael")
    private String middleName;

    @ExcelColumn(value = "Last Name", order = 4, example = "Doe")
    private String lastName;

    @ExcelColumn(value = "Contact Number", order = 5, example = "9876543210")
    private String contactNumber;

    @ExcelColumn(value = "Permanent Address", order = 6, example = "123 Main St, City, State - 123456")
    private String permanentAddress;

    @ExcelColumn(value = "Correspondence Address", order = 7, example = "456 Oak Ave, City, State - 654321")
    private String correspondenceAddress;
}
