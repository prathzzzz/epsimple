package com.eps.module.api.epsone.activity_work.processor;

import com.eps.module.activity.Activities;
import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activities.repository.ActivitiesRepository;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkBulkUploadDto;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.activity_work.validator.ActivityWorkBulkUploadValidator;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.status.GenericStatusType;
import com.eps.module.vendor.Vendor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityWorkBulkUploadProcessor extends BulkUploadProcessor<ActivityWorkBulkUploadDto, ActivityWork> {

    private final ActivityWorkRepository activityWorkRepository;
    private final ActivitiesRepository activitiesRepository;
    private final VendorRepository vendorRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final ActivityWorkBulkUploadValidator validator;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    @Override
    protected ActivityWorkBulkUploadValidator getValidator() {
        return validator;
    }

    @Override
    @Transactional
    protected ActivityWork convertToEntity(ActivityWorkBulkUploadDto dto) {
        // Find activities by name
        Activities activities = activitiesRepository
                .findByActivityNameIgnoreCase(dto.getActivitiesName().trim())
                .orElseThrow(() -> new RuntimeException("Activities not found: " + dto.getActivitiesName()));

        // Find vendor by name
        Vendor vendor = vendorRepository
                .findByVendorNameIgnoreCase(dto.getVendorName().trim())
                .orElseThrow(() -> new RuntimeException("Vendor not found: " + dto.getVendorName()));

        // Find status type by code
        GenericStatusType statusType = genericStatusTypeRepository
                .findByStatusCodeIgnoreCase(dto.getStatusTypeCode().trim())
                .orElseThrow(() -> new RuntimeException("Status type not found: " + dto.getStatusTypeCode()));

        // Build ActivityWork entity
        ActivityWork activityWork = ActivityWork.builder()
                .vendorOrderNumber(dto.getVendorOrderNumber() != null && !dto.getVendorOrderNumber().trim().isEmpty()
                        ? dto.getVendorOrderNumber().trim() : null)
                .workOrderDate(parseDate(dto.getWorkOrderDate()))
                .workStartDate(parseDate(dto.getWorkStartDate()))
                .workCompletionDate(parseDate(dto.getWorkCompletionDate()))
                .build();

        // Set relationships
        activityWork.setActivities(activities);
        activityWork.setVendor(vendor);
        activityWork.setStatusType(statusType);

        return activityWork;
    }

    @Override
    @Transactional
    protected void saveEntity(ActivityWork entity) {
        activityWorkRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(ActivityWorkBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Activities Name", dto.getActivitiesName());
        rowData.put("Vendor Name", dto.getVendorName());
        rowData.put("Vendor Order Number", dto.getVendorOrderNumber());
        rowData.put("Work Order Date", dto.getWorkOrderDate());
        rowData.put("Work Start Date", dto.getWorkStartDate());
        rowData.put("Work Completion Date", dto.getWorkCompletionDate());
        rowData.put("Status Type Code", dto.getStatusTypeCode());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(ActivityWorkBulkUploadDto dto) {
        return (dto.getActivitiesName() == null || dto.getActivitiesName().trim().isEmpty()) &&
                (dto.getVendorName() == null || dto.getVendorName().trim().isEmpty()) &&
                (dto.getVendorOrderNumber() == null || dto.getVendorOrderNumber().trim().isEmpty()) &&
                (dto.getWorkOrderDate() == null || dto.getWorkOrderDate().trim().isEmpty()) &&
                (dto.getWorkStartDate() == null || dto.getWorkStartDate().trim().isEmpty()) &&
                (dto.getWorkCompletionDate() == null || dto.getWorkCompletionDate().trim().isEmpty()) &&
                (dto.getStatusTypeCode() == null || dto.getStatusTypeCode().trim().isEmpty());
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        String trimmed = dateStr.trim();
        
        // Try to parse as Excel serial number first
        try {
            double excelSerialNumber = Double.parseDouble(trimmed);
            if (excelSerialNumber >= 1 && excelSerialNumber < 100000) {
                // Excel's epoch is 1899-12-30 (due to Excel's leap year bug)
                return LocalDate.of(1899, 12, 30).plusDays((long) excelSerialNumber);
            }
        } catch (NumberFormatException e) {
            // Not a number, try date formats
        }
        
        // Try standard date formats
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(trimmed, formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }
        
        // If all parsing fails, return null (validation already passed, so this shouldn't happen)
        log.warn("Failed to parse date: {} - This should have been caught by validation", dateStr);
        return null;
    }
}
