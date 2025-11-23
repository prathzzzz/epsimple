package com.eps.module.api.epsone.activity_work.validator;

import com.eps.module.api.epsone.activity_work.constant.ActivityWorkErrorMessages;
import com.eps.module.api.epsone.activity_work.dto.ActivityWorkBulkUploadDto;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.activities.repository.ActivitiesRepository;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityWorkBulkUploadValidator implements BulkRowValidator<ActivityWorkBulkUploadDto> {

    private final ActivityWorkRepository activityWorkRepository;
    private final ActivitiesRepository activitiesRepository;
    private final VendorRepository vendorRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    @Override
    public List<BulkUploadErrorDto> validate(ActivityWorkBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Activities Name
        if (rowData.getActivitiesName() == null || rowData.getActivitiesName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Activities Name")
                    .errorMessage(ActivityWorkErrorMessages.ACTIVITIES_NAME_REQUIRED)
                    .rejectedValue(rowData.getActivitiesName())
                    .build());
        } else {
            // Check if activities exists
            boolean exists = activitiesRepository.existsByActivityNameIgnoreCase(rowData.getActivitiesName().trim());
            if (!exists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Activities Name")
                        .errorMessage(String.format(ActivityWorkErrorMessages.ACTIVITIES_NOT_FOUND_NAME, rowData.getActivitiesName()))
                        .rejectedValue(rowData.getActivitiesName())
                        .build());
            }
        }

        // Validate Vendor Name
        if (rowData.getVendorName() == null || rowData.getVendorName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Name")
                    .errorMessage(ActivityWorkErrorMessages.VENDOR_NAME_REQUIRED)
                    .rejectedValue(rowData.getVendorName())
                    .build());
        } else {
            // Check if vendor exists
            boolean exists = vendorRepository.existsByVendorNameIgnoreCase(rowData.getVendorName().trim());
            if (!exists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Vendor Name")
                        .errorMessage(String.format(ActivityWorkErrorMessages.VENDOR_NOT_FOUND_NAME, rowData.getVendorName()))
                        .rejectedValue(rowData.getVendorName())
                        .build());
            }
        }

        // Validate Vendor Order Number length
        if (rowData.getVendorOrderNumber() != null && !rowData.getVendorOrderNumber().trim().isEmpty()) {
            if (rowData.getVendorOrderNumber().trim().length() > 100) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Vendor Order Number")
                        .errorMessage(ActivityWorkErrorMessages.VENDOR_ORDER_NUMBER_MAX_LENGTH)
                        .rejectedValue(rowData.getVendorOrderNumber())
                        .build());
            }
        }

        // Validate Work Order Date format
        if (rowData.getWorkOrderDate() != null && !rowData.getWorkOrderDate().trim().isEmpty()) {
            if (!isValidDate(rowData.getWorkOrderDate().trim())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Work Order Date")
                        .errorMessage(ActivityWorkErrorMessages.INVALID_DATE_FORMAT)
                        .rejectedValue(rowData.getWorkOrderDate())
                        .build());
            }
        }

        // Validate Work Start Date format
        if (rowData.getWorkStartDate() != null && !rowData.getWorkStartDate().trim().isEmpty()) {
            if (!isValidDate(rowData.getWorkStartDate().trim())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Work Start Date")
                        .errorMessage(ActivityWorkErrorMessages.INVALID_DATE_FORMAT)
                        .rejectedValue(rowData.getWorkStartDate())
                        .build());
            }
        }

        // Validate Work Completion Date format
        if (rowData.getWorkCompletionDate() != null && !rowData.getWorkCompletionDate().trim().isEmpty()) {
            if (!isValidDate(rowData.getWorkCompletionDate().trim())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Work Completion Date")
                        .errorMessage(ActivityWorkErrorMessages.INVALID_DATE_FORMAT)
                        .rejectedValue(rowData.getWorkCompletionDate())
                        .build());
            }
        }

        // Validate Status Type Code
        if (rowData.getStatusTypeCode() == null || rowData.getStatusTypeCode().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Status Type Code")
                    .errorMessage(ActivityWorkErrorMessages.STATUS_TYPE_CODE_REQUIRED)
                    .rejectedValue(rowData.getStatusTypeCode())
                    .build());
        } else {
            // Check if status type exists
            boolean exists = genericStatusTypeRepository.existsByStatusCodeIgnoreCase(rowData.getStatusTypeCode().trim());
            if (!exists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Status Type Code")
                        .errorMessage(String.format(ActivityWorkErrorMessages.STATUS_TYPE_CODE_NOT_FOUND, rowData.getStatusTypeCode()))
                        .rejectedValue(rowData.getStatusTypeCode())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(ActivityWorkBulkUploadDto rowData) {
        // For Activity Work, we check if the combination of activities + vendor + vendorOrderNumber exists
        if (rowData.getActivitiesName() == null || rowData.getVendorName() == null) {
            return false;
        }

        // Note: This is a simplified duplicate check. You may want to adjust based on business requirements
        return false; // Activity work can have multiple entries for same activity/vendor combination
    }

    private boolean isValidDate(String dateStr) {
        // First, try to parse as Excel serial number
        try {
            double excelSerialNumber = Double.parseDouble(dateStr);
            // Excel dates are typically between 1 (1900-01-01) and 60000 (2064-01-06)
            return excelSerialNumber >= 1 && excelSerialNumber < 100000;
        } catch (NumberFormatException e) {
            // Not a number, try date formats
        }
        
        // Try standard date formats
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate.parse(dateStr, formatter);
                return true;
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }
        return false;
    }
}
