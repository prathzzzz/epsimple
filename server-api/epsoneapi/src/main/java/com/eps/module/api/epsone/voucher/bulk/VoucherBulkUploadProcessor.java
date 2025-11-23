package com.eps.module.api.epsone.voucher.bulk;

import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.api.epsone.voucher.dto.VoucherBulkUploadDto;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.common.exception.BadRequestException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PaymentDetails;
import com.eps.module.payment.Voucher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VoucherBulkUploadProcessor extends BulkUploadProcessor<VoucherBulkUploadDto, Voucher> {

    private final VoucherBulkUploadValidator validator;
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
    public BulkRowValidator<VoucherBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    public Voucher convertToEntity(VoucherBulkUploadDto dto) {
        Payee payee = payeeRepository.findByPayeeNameIgnoreCase(dto.getPayeeName().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Payee not found: " + dto.getPayeeName()));

        PaymentDetails paymentDetails = null;
        if (dto.getPaymentTransactionNumber() != null && !dto.getPaymentTransactionNumber().trim().isEmpty()) {
            paymentDetails = paymentDetailsRepository.findByTransactionNumberIgnoreCase(dto.getPaymentTransactionNumber().trim())
                    .orElse(null);
        }

        return Voucher.builder()
                .voucherNumber(dto.getVoucherNumber().trim())
                .voucherDate(parseDate(dto.getVoucherDate()))
                .orderNumber(trimOrNull(dto.getOrderNumber()))
                .payee(payee)
                .paymentDetails(paymentDetails)
                .paymentDueDate(parseOptionalDate(dto.getPaymentDueDate()))
                .paymentStatus(trimOrNull(dto.getPaymentStatus()))
                .quantity(parseBigDecimal(dto.getQuantity()))
                .unit(trimOrNull(dto.getUnit()))
                .unitPrice(parseBigDecimal(dto.getUnitPrice()))
                .taxCgst(parseBigDecimal(dto.getTaxCgst()))
                .taxSgst(parseBigDecimal(dto.getTaxSgst()))
                .taxIgst(parseBigDecimal(dto.getTaxIgst()))
                .amount1(parseBigDecimal(dto.getAmount1()))
                .amount2(parseBigDecimal(dto.getAmount2()))
                .discountPercentage(parseBigDecimal(dto.getDiscountPercentage()))
                .discountAmount(parseBigDecimal(dto.getDiscountAmount()))
                .finalAmount(parseBigDecimal(dto.getFinalAmount()))
                .remarks(trimOrNull(dto.getRemarks()))
                .build();
    }

    @Override
    protected void saveEntity(Voucher entity) {
        voucherRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(VoucherBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("voucherNumber", dto.getVoucherNumber());
        rowData.put("voucherDate", dto.getVoucherDate());
        rowData.put("orderNumber", dto.getOrderNumber());
        rowData.put("payeeName", dto.getPayeeName());
        rowData.put("paymentTransactionNumber", dto.getPaymentTransactionNumber());
        rowData.put("paymentDueDate", dto.getPaymentDueDate());
        rowData.put("paymentStatus", dto.getPaymentStatus());
        rowData.put("quantity", dto.getQuantity());
        rowData.put("unit", dto.getUnit());
        rowData.put("unitPrice", dto.getUnitPrice());
        rowData.put("taxCgst", dto.getTaxCgst());
        rowData.put("taxSgst", dto.getTaxSgst());
        rowData.put("taxIgst", dto.getTaxIgst());
        rowData.put("amount1", dto.getAmount1());
        rowData.put("amount2", dto.getAmount2());
        rowData.put("discountPercentage", dto.getDiscountPercentage());
        rowData.put("discountAmount", dto.getDiscountAmount());
        rowData.put("finalAmount", dto.getFinalAmount());
        rowData.put("remarks", dto.getRemarks());
        return rowData;
    }

    @Override
    public boolean isEmptyRow(VoucherBulkUploadDto dto) {
        return (dto.getVoucherNumber() == null || dto.getVoucherNumber().trim().isEmpty()) &&
               (dto.getVoucherDate() == null || dto.getVoucherDate().trim().isEmpty()) &&
               (dto.getPayeeName() == null || dto.getPayeeName().trim().isEmpty()) &&
               (dto.getFinalAmount() == null || dto.getFinalAmount().trim().isEmpty());
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateStr.trim(), formatter);
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }
        throw new BadRequestException("Unable to parse date: " + dateStr);
    }

    private LocalDate parseOptionalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return parseDate(dateStr);
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String trimOrNull(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}
