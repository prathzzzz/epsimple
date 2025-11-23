package com.eps.module.api.epsone.expenditures_voucher.validator;

import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.expenditures_voucher.constant.ExpendituresVoucherErrorMessages;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherBulkUploadDto;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
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
public class ExpendituresVoucherBulkUploadValidator implements BulkRowValidator<ExpendituresVoucherBulkUploadDto> {

    private final CostItemRepository costItemRepository;
    private final VoucherRepository voucherRepository;
    private final ManagedProjectRepository managedProjectRepository;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    @Override
    public List<BulkUploadErrorDto> validate(ExpendituresVoucherBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Cost Item Name (Required, FK)
        if (rowData.getCostItemName() == null || rowData.getCostItemName().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Cost Item Name", ExpendituresVoucherErrorMessages.COST_ITEM_NAME_REQUIRED, rowData.getCostItemName()));
        } else {
            String costItemName = rowData.getCostItemName().trim();
            boolean exists = costItemRepository.existsByCostItemForIgnoreCase(costItemName);
            if (!exists) {
                errors.add(createError(rowNumber, "Cost Item Name", ExpendituresVoucherErrorMessages.COST_ITEM_NOT_FOUND_NAME + costItemName, costItemName));
            }
        }

        // Validate Voucher Number (Required, FK)
        if (rowData.getVoucherNumber() == null || rowData.getVoucherNumber().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Voucher Number", ExpendituresVoucherErrorMessages.VOUCHER_NUMBER_REQUIRED, rowData.getVoucherNumber()));
        } else {
            String voucherNumber = rowData.getVoucherNumber().trim();
            boolean exists = voucherRepository.existsByVoucherNumberIgnoreCase(voucherNumber);
            if (!exists) {
                errors.add(createError(rowNumber, "Voucher Number", ExpendituresVoucherErrorMessages.VOUCHER_NOT_FOUND_NUMBER + voucherNumber, voucherNumber));
            }
        }

        // Validate Managed Project Code (Required, FK)
        if (rowData.getManagedProjectCode() == null || rowData.getManagedProjectCode().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Managed Project Code", ExpendituresVoucherErrorMessages.MANAGED_PROJECT_CODE_REQUIRED, rowData.getManagedProjectCode()));
        } else {
            String projectCode = rowData.getManagedProjectCode().trim();
            boolean exists = managedProjectRepository.existsByProjectCodeIgnoreCase(projectCode);
            if (!exists) {
                errors.add(createError(rowNumber, "Managed Project Code", ExpendituresVoucherErrorMessages.MANAGED_PROJECT_NOT_FOUND_CODE + projectCode, projectCode));
            }
        }

        // Validate Incurred Date (Optional)
        if (rowData.getIncurredDate() != null && !rowData.getIncurredDate().trim().isEmpty()) {
            if (!isValidDate(rowData.getIncurredDate().trim())) {
                errors.add(createError(rowNumber, "Incurred Date", ExpendituresVoucherErrorMessages.INCURRED_DATE_INVALID, rowData.getIncurredDate()));
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(ExpendituresVoucherBulkUploadDto rowData) {
        // ExpendituresVoucher doesn't have a unique constraint, so we can't check for duplicates
        // Multiple expenditures can be linked to the same cost item, voucher, or project
        return false;
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

    private BulkUploadErrorDto createError(int rowNumber, String fieldName, String message, String rejectedValue) {
        return BulkUploadErrorDto.builder()
                .rowNumber(rowNumber)
                .fieldName(fieldName)
                .errorMessage(message)
                .rejectedValue(rejectedValue)
                .build();
    }
}
