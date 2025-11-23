package com.eps.module.api.epsone.expenditures_invoice.validator;

import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.expenditures_invoice.constant.ExpendituresInvoiceErrorMessages;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceBulkUploadDto;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
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
public class ExpendituresInvoiceBulkUploadValidator implements BulkRowValidator<ExpendituresInvoiceBulkUploadDto> {

    private final CostItemRepository costItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final ManagedProjectRepository managedProjectRepository;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    @Override
    public List<BulkUploadErrorDto> validate(ExpendituresInvoiceBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Cost Item Name (Required, FK)
        if (rowData.getCostItemName() == null || rowData.getCostItemName().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Cost Item Name", ExpendituresInvoiceErrorMessages.COST_ITEM_NAME_REQUIRED, rowData.getCostItemName()));
        } else {
            String costItemName = rowData.getCostItemName().trim();
            boolean exists = costItemRepository.existsByCostItemForIgnoreCase(costItemName);
            if (!exists) {
                errors.add(createError(rowNumber, "Cost Item Name", ExpendituresInvoiceErrorMessages.COST_ITEM_NOT_FOUND_NAME + costItemName, costItemName));
            }
        }

        // Validate Invoice Number (Required, FK)
        if (rowData.getInvoiceNumber() == null || rowData.getInvoiceNumber().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Invoice Number", ExpendituresInvoiceErrorMessages.INVOICE_NUMBER_REQUIRED, rowData.getInvoiceNumber()));
        } else {
            String invoiceNumber = rowData.getInvoiceNumber().trim();
            boolean exists = invoiceRepository.existsByInvoiceNumber(invoiceNumber);
            if (!exists) {
                errors.add(createError(rowNumber, "Invoice Number", ExpendituresInvoiceErrorMessages.INVOICE_NOT_FOUND_NUMBER + invoiceNumber, invoiceNumber));
            }
        }

        // Validate Managed Project Code (Required, FK)
        if (rowData.getManagedProjectCode() == null || rowData.getManagedProjectCode().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Managed Project Code", ExpendituresInvoiceErrorMessages.MANAGED_PROJECT_CODE_REQUIRED, rowData.getManagedProjectCode()));
        } else {
            String projectCode = rowData.getManagedProjectCode().trim();
            boolean exists = managedProjectRepository.existsByProjectCodeIgnoreCase(projectCode);
            if (!exists) {
                errors.add(createError(rowNumber, "Managed Project Code", ExpendituresInvoiceErrorMessages.MANAGED_PROJECT_NOT_FOUND_CODE + projectCode, projectCode));
            }
        }

        // Validate Incurred Date (Optional)
        if (rowData.getIncurredDate() != null && !rowData.getIncurredDate().trim().isEmpty()) {
            if (!isValidDate(rowData.getIncurredDate().trim())) {
                errors.add(createError(rowNumber, "Incurred Date", ExpendituresInvoiceErrorMessages.INCURRED_DATE_INVALID, rowData.getIncurredDate()));
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(ExpendituresInvoiceBulkUploadDto rowData) {
        // ExpendituresInvoice doesn't have a unique constraint, so we can't check for duplicates
        // Multiple expenditures can be linked to the same cost item, invoice, or project
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
