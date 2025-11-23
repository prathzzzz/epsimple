package com.eps.module.api.epsone.expenditures_invoice.processor;

import com.eps.module.api.epsone.cost_item.constant.CostItemErrorMessages;
import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceBulkUploadDto;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.api.epsone.expenditures_invoice.validator.ExpendituresInvoiceBulkUploadValidator;
import com.eps.module.api.epsone.invoice.constant.InvoiceErrorMessages;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.managed_project.constant.ManagedProjectErrorMessages;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.CostItem;
import com.eps.module.cost.ExpendituresInvoice;
import com.eps.module.payment.Invoice;
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
public class ExpendituresInvoiceBulkUploadProcessor extends BulkUploadProcessor<ExpendituresInvoiceBulkUploadDto, ExpendituresInvoice> {

    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final CostItemRepository costItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final ManagedProjectRepository managedProjectRepository;
    private final ExpendituresInvoiceBulkUploadValidator validator;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    @Override
    protected ExpendituresInvoiceBulkUploadValidator getValidator() {
        return validator;
    }

    @Override
    @Transactional
    protected ExpendituresInvoice convertToEntity(ExpendituresInvoiceBulkUploadDto dto) {
        // Find Cost Item
        CostItem costItem = costItemRepository
                .findByCostItemForIgnoreCase(dto.getCostItemName().trim())
                .orElseThrow(() -> new ResourceNotFoundException(CostItemErrorMessages.COST_ITEM_NOT_FOUND + dto.getCostItemName()));

        // Find Invoice
        Invoice invoice = invoiceRepository
                .findAll()
                .stream()
                .filter(inv -> inv.getInvoiceNumber().equalsIgnoreCase(dto.getInvoiceNumber().trim()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(InvoiceErrorMessages.INVOICE_NOT_FOUND_GENERIC + dto.getInvoiceNumber()));

        // Find Managed Project
        ManagedProject managedProject = managedProjectRepository
                .findByProjectCodeIgnoreCase(dto.getManagedProjectCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException(ManagedProjectErrorMessages.MANAGED_PROJECT_NOT_FOUND + dto.getManagedProjectCode()));

        // Build ExpendituresInvoice entity
        return ExpendituresInvoice.builder()
                .costItem(costItem)
                .invoice(invoice)
                .managedProject(managedProject)
                .incurredDate(parseDate(dto.getIncurredDate()))
                .description(trimOrNull(dto.getDescription()))
                .build();
    }

    @Override
    @Transactional
    protected void saveEntity(ExpendituresInvoice entity) {
        expendituresInvoiceRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(ExpendituresInvoiceBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Cost Item Name", dto.getCostItemName());
        rowData.put("Invoice Number", dto.getInvoiceNumber());
        rowData.put("Managed Project Code", dto.getManagedProjectCode());
        rowData.put("Incurred Date", dto.getIncurredDate());
        rowData.put("Description", dto.getDescription());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(ExpendituresInvoiceBulkUploadDto dto) {
        return (dto.getCostItemName() == null || dto.getCostItemName().trim().isEmpty()) &&
                (dto.getInvoiceNumber() == null || dto.getInvoiceNumber().trim().isEmpty()) &&
                (dto.getManagedProjectCode() == null || dto.getManagedProjectCode().trim().isEmpty());
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

    private String trimOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
