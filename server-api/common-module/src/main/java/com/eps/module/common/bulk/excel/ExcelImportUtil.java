package com.eps.module.common.bulk.excel;

import com.eps.module.common.constant.CommonErrorMessages;
import com.eps.module.common.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Utility class for reading Excel files using FastExcel Reader
 */
@Slf4j
@Component
public class ExcelImportUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    /**
     * Parse Excel file and convert to list of DTOs
     */
    public <T> List<T> parseExcelFile(MultipartFile file, Class<T> clazz) throws IOException {
        List<T> results = new ArrayList<>();
        
        try (ReadableWorkbook workbook = new ReadableWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getFirstSheet();
            
            // Get annotated fields
            Map<String, FieldInfo> fieldMap = getFieldMap(clazz);
            
            try (Stream<Row> rows = sheet.openStream()) {
                Iterator<Row> rowIterator = rows.iterator();
                
                if (!rowIterator.hasNext()) {
                    throw new IllegalArgumentException("Excel file is empty");
                }
                
                // Read header row
                Row headerRow = rowIterator.next();
                Map<Integer, String> columnMapping = buildColumnMapping(headerRow, fieldMap);
                
                if (columnMapping.isEmpty()) {
                    throw new IllegalArgumentException("No valid columns found in Excel file");
                }
                
                // Read data rows
                int rowNumber = 2; // Start from 2 (1 is header)
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    
                    if (isEmptyRow(row)) {
                        rowNumber++;
                        continue;
                    }
                    
                    try {
                        T dto = parseRow(row, columnMapping, fieldMap, clazz);
                        results.add(dto);
                    } catch (Exception e) {
                        log.error("Error parsing row {}: {}", rowNumber, e.getMessage());
                        throw new BadRequestException(CommonErrorMessages.EXCEL_PARSE_ERROR + rowNumber + ": " + e.getMessage());
                    }
                    
                    rowNumber++;
                }
            }
        }
        
        if (results.isEmpty()) {
            throw new IllegalArgumentException("Excel file must contain at least one data row");
        }
        
        return results;
    }
    
    /**
     * Get map of field names to FieldInfo
     */
    private <T> Map<String, FieldInfo> getFieldMap(Class<T> clazz) {
        Map<String, FieldInfo> fieldMap = new HashMap<>();
        
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                fieldMap.put(annotation.value(), new FieldInfo(field, annotation));
            }
        }
        
        return fieldMap;
    }
    
    /**
     * Build column index to field name mapping
     */
    private Map<Integer, String> buildColumnMapping(Row headerRow, Map<String, FieldInfo> fieldMap) {
        Map<Integer, String> columnMapping = new HashMap<>();
        
        for (int i = 0; i < headerRow.getCellCount(); i++) {
            String headerName = getCellValueAsString(headerRow, i).trim();
            // Remove asterisk if present (required marker)
            headerName = headerName.replaceAll("\\s*\\*$", "");
            
            if (fieldMap.containsKey(headerName)) {
                columnMapping.put(i, headerName);
            }
        }
        
        return columnMapping;
    }
    
    /**
     * Parse a single row into DTO
     */
    private <T> T parseRow(Row row, Map<Integer, String> columnMapping, 
                          Map<String, FieldInfo> fieldMap, Class<T> clazz) throws Exception {
        T dto = clazz.getDeclaredConstructor().newInstance();
        
        for (Map.Entry<Integer, String> entry : columnMapping.entrySet()) {
            int colIndex = entry.getKey();
            String fieldName = entry.getValue();
            FieldInfo fieldInfo = fieldMap.get(fieldName);
            
            String cellValue = getCellValueAsString(row, colIndex).trim();
            if (!cellValue.isEmpty()) {
                Object value = parseCellValue(cellValue, fieldInfo.field.getType());
                setFieldValue(dto, fieldInfo.field, value);
            }
        }
        
        return dto;
    }
    
    /**
     * Parse cell value based on target field type
     */
    private Object parseCellValue(String cellValue, Class<?> targetType) {
        if (cellValue.isEmpty()) {
            return null;
        }
        
        try {
            return switch (targetType.getName()) {
                case "java.lang.String" -> cellValue;
                case "java.lang.Integer", "int" -> {
                    // Handle decimal values from Excel
                    if (cellValue.contains(".")) {
                        yield (int) Double.parseDouble(cellValue);
                    }
                    yield Integer.parseInt(cellValue);
                }
                case "java.lang.Long", "long" -> {
                    if (cellValue.contains(".")) {
                        yield (long) Double.parseDouble(cellValue);
                    }
                    yield Long.parseLong(cellValue);
                }
                case "java.lang.Double", "double" -> Double.parseDouble(cellValue);
                case "java.math.BigDecimal" -> new BigDecimal(cellValue);
                case "java.lang.Boolean", "boolean" -> Boolean.parseBoolean(cellValue);
                case "java.time.LocalDate" -> parseLocalDate(cellValue);
                case "java.time.LocalDateTime" -> LocalDateTime.parse(cellValue, DATETIME_FORMATTER);
                default -> cellValue;
            };
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid value '" + cellValue + "' for type " + targetType.getSimpleName());
        }
    }
    
    /**
     * Parse LocalDate with multiple format support
     */
    private LocalDate parseLocalDate(String dateValue) {
        if (dateValue == null || dateValue.trim().isEmpty()) {
            return null;
        }
        
        // Try ISO format first (yyyy-MM-dd)
        try {
            return LocalDate.parse(dateValue, ISO_DATE_FORMATTER);
        } catch (DateTimeParseException e1) {
            // Try dd-MM-yyyy format
            try {
                return LocalDate.parse(dateValue, DATE_FORMATTER);
            } catch (DateTimeParseException e2) {
                // Try ISO standard format (default)
                try {
                    return LocalDate.parse(dateValue);
                } catch (DateTimeParseException e3) {
                    throw new DateTimeParseException("Unable to parse date: " + dateValue, dateValue, 0);
                }
            }
        }
    }
    
    /**
     * Get cell value as string
     */
    private String getCellValueAsString(Row row, int columnIndex) {
        try {
            if (columnIndex >= row.getCellCount()) {
                return "";
            }
            
            // Use getCellText() which returns the cached computed value for formulas
            // This works for STRING, NUMBER, and FORMULA cells
            String cellText = row.getCellText(columnIndex);
            if (cellText != null && !cellText.isEmpty()) {
                return cellText.trim();
            }
            
            return "";

        } catch (Exception e) {
            log.warn("Error reading cell at column {}: {}", columnIndex, e.getMessage());
            return "";
        }
    }
    
    /**
     * Set field value using reflection
     */
    private void setFieldValue(Object obj, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(obj, value);
    }
    
    /**
     * Check if row is empty
     */
    private boolean isEmptyRow(Row row) {
        for (int i = 0; i < row.getCellCount(); i++) {
            String value = getCellValueAsString(row, i);
            if (!value.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Helper class to hold field information
     */
    private static class FieldInfo {
        Field field;
        ExcelColumn annotation;
        
        FieldInfo(Field field, ExcelColumn annotation) {
            this.field = field;
            this.annotation = annotation;
        }
    }
}
