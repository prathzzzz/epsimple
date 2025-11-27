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
        
        log.debug("Parsing Excel file: name={}, size={}, contentType={}", 
                file.getOriginalFilename(), file.getSize(), file.getContentType());
        
        try (ReadableWorkbook workbook = new ReadableWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getFirstSheet();
            log.debug("Sheet name: {}", sheet.getName());
            
            // Get annotated fields
            Map<String, FieldInfo> fieldMap = getFieldMap(clazz);
            
            try (Stream<Row> rows = sheet.openStream()) {
                List<Row> rowList = rows.toList();
                log.debug("Total rows in sheet: {}", rowList.size());
                
                if (rowList.isEmpty()) {
                    throw new IllegalArgumentException("Excel file is empty");
                }
                
                // Log first few rows for debugging
                for (int i = 0; i < Math.min(3, rowList.size()); i++) {
                    Row r = rowList.get(i);
                    StringBuilder rowContent = new StringBuilder();
                    for (int j = 0; j < r.getCellCount(); j++) {
                        if (j > 0) rowContent.append(" | ");
                        rowContent.append("[").append(j).append("]=").append(getCellValueAsString(r, j));
                    }
                    log.debug("Row {} content: {}", i, rowContent);
                }
                
                // Read header row
                Row headerRow = rowList.get(0);
                Map<Integer, String> columnMapping = buildColumnMapping(headerRow, fieldMap);
                
                if (columnMapping.isEmpty()) {
                    throw new IllegalArgumentException("No valid columns found in Excel file");
                }
                
                // Read data rows
                for (int rowNumber = 1; rowNumber < rowList.size(); rowNumber++) {
                    Row row = rowList.get(rowNumber);
                    
                    if (isEmptyRow(row)) {
                        continue;
                    }
                    
                    try {
                        T dto = parseRow(row, columnMapping, fieldMap, clazz);
                        results.add(dto);
                    } catch (Exception e) {
                        log.error("Error parsing row {}: {}", rowNumber + 1, e.getMessage());
                        throw new BadRequestException(CommonErrorMessages.EXCEL_PARSE_ERROR + (rowNumber + 1) + ": " + e.getMessage());
                    }
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
        
        log.debug("Expected columns from DTO: {}", fieldMap.keySet());
        log.debug("Header row cell count: {}", headerRow.getCellCount());
        
        for (int i = 0; i < headerRow.getCellCount(); i++) {
            String rawHeaderName = getCellValueAsString(headerRow, i);
            String headerName = rawHeaderName.trim();
            log.debug("Column {}: raw='{}', trimmed='{}'", i, rawHeaderName, headerName);
            
            // Remove asterisk if present (required marker)
            String normalizedHeader = headerName.replaceAll("\\s*\\*$", "").trim();
            log.debug("Column {} normalized: '{}'", i, normalizedHeader);
            
            if (fieldMap.containsKey(normalizedHeader)) {
                columnMapping.put(i, normalizedHeader);
                log.debug("Column {} mapped to field: {}", i, normalizedHeader);
            } else {
                log.debug("Column {} '{}' not found in expected columns", i, normalizedHeader);
            }
        }
        
        log.debug("Final column mapping: {}", columnMapping);
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
                String trimmed = cellText.trim();
                
                // Fix floating-point precision issues: if the value looks like a decimal with trailing zeros
                // or unnecessary precision, clean it up
                if (trimmed.matches("^-?\\d+\\.\\d+$")) {
                    try {
                        // Parse as BigDecimal to preserve precision, then strip trailing zeros
                        BigDecimal bd = new BigDecimal(trimmed);
                        // Remove trailing zeros and unnecessary decimal point
                        return bd.stripTrailingZeros().toPlainString();
                    } catch (NumberFormatException e) {
                        // If parsing fails, return as-is
                        return trimmed;
                    }
                }
                
                return trimmed;
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
