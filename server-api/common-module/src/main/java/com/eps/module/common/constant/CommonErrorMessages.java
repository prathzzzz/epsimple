package com.eps.module.common.constant;

public class CommonErrorMessages {
    public static final String EXCEL_PARSE_ERROR = "Error parsing row ";
    public static final String EXCEL_EMPTY_FILE = "Excel file is empty";
    public static final String EXCEL_NO_COLUMNS = "No valid columns found in Excel file";
    public static final String EXCEL_NO_DATA = "Excel file must contain at least one data row";
    public static final String EXCEL_INVALID_VALUE = "Invalid value '%s' for type %s";
    
    public static final String SEEDER_FAILURE = "Failed to seed ";
    
    public static final String DTO_CONVERSION_ERROR = "Error converting DTO to entity: ";
    public static final String ENTITY_SAVE_ERROR = "Error saving entity: ";
    
    private CommonErrorMessages() {}
}
