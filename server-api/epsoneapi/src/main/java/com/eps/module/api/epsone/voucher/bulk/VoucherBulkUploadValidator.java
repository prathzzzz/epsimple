package com.eps.module.api.epsone.voucher.bulk;

import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.api.epsone.voucher.dto.VoucherBulkUploadDto;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VoucherBulkUploadValidator implements BulkRowValidator<VoucherBulkUploadDto> {

    private final VoucherRepository voucherRepository;
    private final PayeeRepository payeeRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;

    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("M/d/yyyy")
    );

    @Override
    public List<BulkUploadErrorDto> validate(VoucherBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate required fields
        if (dto.getVoucherNumber() == null || dto.getVoucherNumber().trim().isEmpty()) {
            errors.add(createError(rowNumber, "voucherNumber", "Voucher number is required", dto.getVoucherNumber()));
        }

        if (dto.getVoucherDate() == null || dto.getVoucherDate().trim().isEmpty()) {
            errors.add(createError(rowNumber, "voucherDate", "Voucher date is required", dto.getVoucherDate()));
        } else if (!isValidDate(dto.getVoucherDate())) {
            errors.add(createError(rowNumber, "voucherDate", "Invalid date format. Use yyyy-MM-dd, dd/MM/yyyy, dd-MM-yyyy, or M/d/yyyy", dto.getVoucherDate()));
        }

        if (dto.getPayeeName() == null || dto.getPayeeName().trim().isEmpty()) {
            errors.add(createError(rowNumber, "payeeName", "Payee name is required", dto.getPayeeName()));
        } else {
            // Validate payee exists
            if (!payeeRepository.existsByPayeeNameIgnoreCase(dto.getPayeeName().trim())) {
                errors.add(createError(rowNumber, "payeeName", "Payee not found: " + dto.getPayeeName(), dto.getPayeeName()));
            }
        }

        if (dto.getFinalAmount() == null || dto.getFinalAmount().trim().isEmpty()) {
            errors.add(createError(rowNumber, "finalAmount", "Final amount is required", dto.getFinalAmount()));
        } else if (!isValidBigDecimal(dto.getFinalAmount())) {
            errors.add(createError(rowNumber, "finalAmount", "Invalid final amount format", dto.getFinalAmount()));
        }

        // Validate payment details if provided
        if (dto.getPaymentTransactionNumber() != null && !dto.getPaymentTransactionNumber().trim().isEmpty()) {
            if (!paymentDetailsRepository.existsByTransactionNumberIgnoreCase(dto.getPaymentTransactionNumber().trim())) {
                errors.add(createError(rowNumber, "paymentTransactionNumber", "Payment details not found: " + dto.getPaymentTransactionNumber(), dto.getPaymentTransactionNumber()));
            }
        }

        // Validate optional dates
        if (dto.getPaymentDueDate() != null && !dto.getPaymentDueDate().trim().isEmpty() && !isValidDate(dto.getPaymentDueDate())) {
            errors.add(createError(rowNumber, "paymentDueDate", "Invalid date format", dto.getPaymentDueDate()));
        }

        // Validate numeric fields if provided
        if (dto.getQuantity() != null && !dto.getQuantity().trim().isEmpty() && !isValidBigDecimal(dto.getQuantity())) {
            errors.add(createError(rowNumber, "quantity", "Invalid quantity format", dto.getQuantity()));
        }

        if (dto.getUnitPrice() != null && !dto.getUnitPrice().trim().isEmpty() && !isValidBigDecimal(dto.getUnitPrice())) {
            errors.add(createError(rowNumber, "unitPrice", "Invalid unit price format", dto.getUnitPrice()));
        }

        if (dto.getTaxCgst() != null && !dto.getTaxCgst().trim().isEmpty() && !isValidBigDecimal(dto.getTaxCgst())) {
            errors.add(createError(rowNumber, "taxCgst", "Invalid tax CGST format", dto.getTaxCgst()));
        }

        if (dto.getTaxSgst() != null && !dto.getTaxSgst().trim().isEmpty() && !isValidBigDecimal(dto.getTaxSgst())) {
            errors.add(createError(rowNumber, "taxSgst", "Invalid tax SGST format", dto.getTaxSgst()));
        }

        if (dto.getTaxIgst() != null && !dto.getTaxIgst().trim().isEmpty() && !isValidBigDecimal(dto.getTaxIgst())) {
            errors.add(createError(rowNumber, "taxIgst", "Invalid tax IGST format", dto.getTaxIgst()));
        }

        if (dto.getAmount1() != null && !dto.getAmount1().trim().isEmpty() && !isValidBigDecimal(dto.getAmount1())) {
            errors.add(createError(rowNumber, "amount1", "Invalid amount 1 format", dto.getAmount1()));
        }

        if (dto.getAmount2() != null && !dto.getAmount2().trim().isEmpty() && !isValidBigDecimal(dto.getAmount2())) {
            errors.add(createError(rowNumber, "amount2", "Invalid amount 2 format", dto.getAmount2()));
        }

        if (dto.getDiscountPercentage() != null && !dto.getDiscountPercentage().trim().isEmpty() && !isValidBigDecimal(dto.getDiscountPercentage())) {
            errors.add(createError(rowNumber, "discountPercentage", "Invalid discount percentage format", dto.getDiscountPercentage()));
        }

        if (dto.getDiscountAmount() != null && !dto.getDiscountAmount().trim().isEmpty() && !isValidBigDecimal(dto.getDiscountAmount())) {
            errors.add(createError(rowNumber, "discountAmount", "Invalid discount amount format", dto.getDiscountAmount()));
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(VoucherBulkUploadDto dto) {
        return voucherRepository.existsByVoucherNumberIgnoreCase(dto.getVoucherNumber().trim());
    }

    private boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate.parse(dateStr.trim(), formatter);
                return true;
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }
        return false;
    }

    private boolean isValidBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            new BigDecimal(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private BulkUploadErrorDto createError(int rowNumber, String fieldName, String message, String rejectedValue) {
        return BulkUploadErrorDto.builder()
                .rowNumber(rowNumber)
                .fieldName(fieldName)
                .errorMessage(message)
                .rejectedValue(rejectedValue)
                .errorType("VALIDATION")
                .build();
    }
}
