package com.eps.module.common.bulk.excel;

import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for generating Excel files using FastExcel
 */
@Slf4j
@Component
public class ExcelExportUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    /**
     * Export data to Excel file
     */
    public <T> byte[] exportToExcel(List<T> data, Class<T> clazz, String sheetName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (Workbook workbook = new Workbook(outputStream, "EPSimple", "1.0")) {
            Worksheet worksheet = workbook.newWorksheet(sheetName);
            
            // Get annotated fields
            List<FieldInfo> fieldInfos = getAnnotatedFields(clazz);
            
            // Write header row
            writeHeaderRow(worksheet, fieldInfos);
            
            // Write data rows
            writeDataRows(worksheet, data, fieldInfos);
            
            // Auto-size columns
            for (int i = 0; i < fieldInfos.size(); i++) {
                worksheet.width(i, 20);
            }
            
            workbook.finish();
        }
        
        return outputStream.toByteArray();
    }
    
    /**
     * Generate Excel template with headers and example row
     */
    public <T> byte[] generateTemplate(Class<T> clazz, String sheetName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (Workbook workbook = new Workbook(outputStream, "EPSimple", "1.0")) {
            Worksheet worksheet = workbook.newWorksheet(sheetName);
            
            // Get annotated fields
            List<FieldInfo> fieldInfos = getAnnotatedFields(clazz);
            
            // Write header row
            writeHeaderRow(worksheet, fieldInfos);
            
            // Write example row
            writeExampleRow(worksheet, fieldInfos);
            
            // Auto-size columns
            for (int i = 0; i < fieldInfos.size(); i++) {
                worksheet.width(i, 20);
            }
            
            workbook.finish();
        }
        
        return outputStream.toByteArray();
    }
    
    /**
     * Get list of fields annotated with @ExcelColumn
     */
    private <T> List<FieldInfo> getAnnotatedFields(Class<T> clazz) {
        List<FieldInfo> fieldInfos = new ArrayList<>();
        
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                fieldInfos.add(new FieldInfo(field, annotation));
            }
        }
        
        // Sort by order
        fieldInfos.sort(Comparator.comparingInt(f -> f.annotation.order()));
        
        return fieldInfos;
    }
    
    /**
     * Write header row
     */
    private void writeHeaderRow(Worksheet worksheet, List<FieldInfo> fieldInfos) {
        for (int i = 0; i < fieldInfos.size(); i++) {
            FieldInfo fieldInfo = fieldInfos.get(i);
            String header = fieldInfo.annotation.value();
            if (fieldInfo.annotation.required()) {
                header += " *";
            }
            worksheet.value(0, i, header);
            worksheet.style(0, i).bold().fillColor("4472C4").fontColor("FFFFFF").set();
        }
    }
    
    /**
     * Write data rows
     */
    private <T> void writeDataRows(Worksheet worksheet, List<T> data, List<FieldInfo> fieldInfos) {
        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            T item = data.get(rowIndex);
            
            for (int colIndex = 0; colIndex < fieldInfos.size(); colIndex++) {
                FieldInfo fieldInfo = fieldInfos.get(colIndex);
                Object value = getFieldValue(item, fieldInfo.field);
                
                if (value != null) {
                    setCellValue(worksheet, rowIndex + 1, colIndex, value);
                }
            }
        }
    }
    
    /**
     * Write example row
     */
    private void writeExampleRow(Worksheet worksheet, List<FieldInfo> fieldInfos) {
        for (int i = 0; i < fieldInfos.size(); i++) {
            FieldInfo fieldInfo = fieldInfos.get(i);
            String example = fieldInfo.annotation.example();
            
            if (!example.isEmpty()) {
                worksheet.value(1, i, example);
                worksheet.style(1, i).italic().fontColor("999999").set();
            }
        }
    }
    
    /**
     * Get field value using reflection
     */
    private Object getFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("Error accessing field {}: {}", field.getName(), e.getMessage());
            return null;
        }
    }
    
    /**
     * Set cell value with appropriate type handling
     */
    private void setCellValue(Worksheet worksheet, int row, int col, Object value) {
        switch (value) {
            case String s -> worksheet.value(row, col, s);
            case Number n -> worksheet.value(row, col, n.toString());
            case Boolean b -> worksheet.value(row, col, b);
            case LocalDate d -> worksheet.value(row, col, d.format(DATE_FORMATTER));
            case LocalDateTime dt -> worksheet.value(row, col, dt.format(DATETIME_FORMATTER));
            case null, default -> worksheet.value(row, col, value != null ? value.toString() : "");
        }
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
