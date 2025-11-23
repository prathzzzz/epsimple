package com.eps.module.api.epsone.expenditures_voucher.processor;

import com.eps.module.api.epsone.cost_item.constant.CostItemErrorMessages;
import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherBulkUploadDto;
import com.eps.module.api.epsone.expenditures_voucher.repository.ExpendituresVoucherRepository;
import com.eps.module.api.epsone.expenditures_voucher.validator.ExpendituresVoucherBulkUploadValidator;
import com.eps.module.api.epsone.managed_project.constant.ManagedProjectErrorMessages;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.voucher.constant.VoucherErrorMessages;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.CostItem;
import com.eps.module.cost.ExpendituresVoucher;
import com.eps.module.payment.Voucher;
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
public class ExpendituresVoucherBulkUploadProcessor extends BulkUploadProcessor<ExpendituresVoucherBulkUploadDto, ExpendituresVoucher> {

    private final ExpendituresVoucherRepository expendituresVoucherRepository;
    private final CostItemRepository costItemRepository;
    private final VoucherRepository voucherRepository;
    private final ManagedProjectRepository managedProjectRepository;
    private final ExpendituresVoucherBulkUploadValidator validator;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    @Override
    protected ExpendituresVoucherBulkUploadValidator getValidator() {
        return validator;
    }

    @Override
    @Transactional
    protected ExpendituresVoucher convertToEntity(ExpendituresVoucherBulkUploadDto dto) {
        // Find Cost Item
        CostItem costItem = costItemRepository
                .findByCostItemForIgnoreCase(dto.getCostItemName().trim())
                .orElseThrow(() -> new ResourceNotFoundException(CostItemErrorMessages.COST_ITEM_NOT_FOUND + dto.getCostItemName()));

        // Find Voucher
        Voucher voucher = voucherRepository
                .findAll()
                .stream()
                .filter(v -> v.getVoucherNumber().equalsIgnoreCase(dto.getVoucherNumber().trim()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(VoucherErrorMessages.VOUCHER_NOT_FOUND + dto.getVoucherNumber()));

        // Find Managed Project
        ManagedProject managedProject = managedProjectRepository
                .findByProjectCodeIgnoreCase(dto.getManagedProjectCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException(ManagedProjectErrorMessages.MANAGED_PROJECT_NOT_FOUND + dto.getManagedProjectCode()));

        // Build ExpendituresVoucher entity
        return ExpendituresVoucher.builder()
                .costItem(costItem)
                .voucher(voucher)
                .managedProject(managedProject)
                .incurredDate(parseDate(dto.getIncurredDate()))
                .description(trimOrNull(dto.getDescription()))
                .build();
    }

    @Override
    @Transactional
    protected void saveEntity(ExpendituresVoucher entity) {
        expendituresVoucherRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(ExpendituresVoucherBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Cost Item Name", dto.getCostItemName());
        rowData.put("Voucher Number", dto.getVoucherNumber());
        rowData.put("Managed Project Code", dto.getManagedProjectCode());
        rowData.put("Incurred Date", dto.getIncurredDate());
        rowData.put("Description", dto.getDescription());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(ExpendituresVoucherBulkUploadDto dto) {
        return (dto.getCostItemName() == null || dto.getCostItemName().trim().isEmpty()) &&
                (dto.getVoucherNumber() == null || dto.getVoucherNumber().trim().isEmpty()) &&
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
