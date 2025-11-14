package com.eps.module.api.epsone.invoice.validator;

import com.eps.module.api.epsone.invoice.dto.InvoiceBulkUploadDto;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceBulkUploadValidator implements BulkRowValidator<InvoiceBulkUploadDto> {

    private final InvoiceRepository invoiceRepository;
    private final PayeeDetailsRepository payeeDetailsRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    @Override
    public List<BulkUploadErrorDto> validate(InvoiceBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Invoice Number (Required, Max 100, Unique)
        if (rowData.getInvoiceNumber() == null || rowData.getInvoiceNumber().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Invoice Number", "Invoice number is required", rowData.getInvoiceNumber()));
        } else {
            String invoiceNumber = rowData.getInvoiceNumber().trim();
            if (invoiceNumber.length() > 100) {
                errors.add(createError(rowNumber, "Invoice Number", "Invoice number cannot exceed 100 characters", invoiceNumber));
            }
            if (invoiceRepository.existsByInvoiceNumber(invoiceNumber)) {
                errors.add(createError(rowNumber, "Invoice Number", "Invoice number '" + invoiceNumber + "' already exists", invoiceNumber));
            }
        }

        // Validate Invoice Date (Required)
        if (rowData.getInvoiceDate() == null || rowData.getInvoiceDate().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Invoice Date", "Invoice date is required", rowData.getInvoiceDate()));
        } else if (!isValidDate(rowData.getInvoiceDate().trim())) {
            errors.add(createError(rowNumber, "Invoice Date", "Invalid date format. Use yyyy-MM-dd, dd/MM/yyyy, or MM/dd/yyyy", rowData.getInvoiceDate()));
        }

        // Validate Invoice Received Date (Optional)
        if (rowData.getInvoiceReceivedDate() != null && !rowData.getInvoiceReceivedDate().trim().isEmpty()) {
            if (!isValidDate(rowData.getInvoiceReceivedDate().trim())) {
                errors.add(createError(rowNumber, "Invoice Received Date", "Invalid date format", rowData.getInvoiceReceivedDate()));
            }
        }

        // Validate Order Number (Optional, Max 100)
        if (rowData.getOrderNumber() != null && rowData.getOrderNumber().trim().length() > 100) {
            errors.add(createError(rowNumber, "Order Number", "Order number cannot exceed 100 characters", rowData.getOrderNumber()));
        }

        // Validate Vendor Name (Optional, Max 255)
        if (rowData.getVendorName() != null && rowData.getVendorName().trim().length() > 255) {
            errors.add(createError(rowNumber, "Vendor Name", "Vendor name cannot exceed 255 characters", rowData.getVendorName()));
        }

        // Validate Payee Name (Required, FK)
        if (rowData.getPayeeName() == null || rowData.getPayeeName().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Payee Name", "Payee name is required", rowData.getPayeeName()));
        } else {
            String payeeName = rowData.getPayeeName().trim();
            boolean exists = payeeDetailsRepository.existsByPayeeNameIgnoreCase(payeeName);
            if (!exists) {
                errors.add(createError(rowNumber, "Payee Name", "Payee '" + payeeName + "' not found", payeeName));
            }
        }

        // Validate Payment Transaction Number (Optional, FK if provided)
        if (rowData.getPaymentTransactionNumber() != null && !rowData.getPaymentTransactionNumber().trim().isEmpty()) {
            String txnNumber = rowData.getPaymentTransactionNumber().trim();
            boolean exists = paymentDetailsRepository.existsByTransactionNumberIgnoreCase(txnNumber);
            if (!exists) {
                errors.add(createError(rowNumber, "Payment Transaction Number", "Payment transaction '" + txnNumber + "' not found", txnNumber));
            }
        }

        // Validate Date Fields
        validateOptionalDate(rowData.getPaymentDueDate(), "Payment Due Date", rowNumber, errors);
        validateOptionalDate(rowData.getPaidDate(), "Paid Date", rowNumber, errors);
        validateOptionalDate(rowData.getMasterPoDate(), "Master PO Date", rowNumber, errors);
        validateOptionalDate(rowData.getDispatchOrderDate(), "Dispatch Order Date", rowNumber, errors);

        // Validate Payment Status (Optional, Max 20)
        if (rowData.getPaymentStatus() != null && rowData.getPaymentStatus().trim().length() > 20) {
            errors.add(createError(rowNumber, "Payment Status", "Payment status cannot exceed 20 characters", rowData.getPaymentStatus()));
        }

        // Validate Numeric Fields
        validateOptionalBigDecimal(rowData.getQuantity(), "Quantity", 10, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getUnitPrice(), "Unit Price", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getTaxCgstPercentage(), "Tax CGST Percentage", 5, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getTaxSgstPercentage(), "Tax SGST Percentage", 5, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getTaxIgstPercentage(), "Tax IGST Percentage", 5, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getBasicAmount(), "Basic Amount", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getCgst(), "CGST", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getSgst(), "SGST", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getIgst(), "IGST", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getAmount1(), "Amount 1", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getAmount2(), "Amount 2", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getDiscountPercentage(), "Discount Percentage", 5, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getDiscountAmount(), "Discount Amount", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getTds(), "TDS", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getAdvanceAmount(), "Advance Amount", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getTotalAmount(), "Total Amount", 12, 2, rowNumber, errors);
        validateOptionalBigDecimal(rowData.getNetPayable(), "Net Payable", 12, 2, rowNumber, errors);

        // Validate Total Invoice Value (Required)
        if (rowData.getTotalInvoiceValue() == null || rowData.getTotalInvoiceValue().trim().isEmpty()) {
            errors.add(createError(rowNumber, "Total Invoice Value", "Total invoice value is required", rowData.getTotalInvoiceValue()));
        } else {
            validateOptionalBigDecimal(rowData.getTotalInvoiceValue(), "Total Invoice Value", 12, 2, rowNumber, errors);
        }

        // Validate String Length Fields
        validateMaxLength(rowData.getUnit(), "Unit", 50, rowNumber, errors);
        validateMaxLength(rowData.getMachineSerialNumber(), "Machine Serial Number", 100, rowNumber, errors);
        validateMaxLength(rowData.getMasterPoNumber(), "Master PO Number", 100, rowNumber, errors);
        validateMaxLength(rowData.getDispatchOrderNumber(), "Dispatch Order Number", 100, rowNumber, errors);
        validateMaxLength(rowData.getUtrDetail(), "UTR Detail", 255, rowNumber, errors);
        validateMaxLength(rowData.getBilledByVendorGst(), "Billed By Vendor GST", 100, rowNumber, errors);
        validateMaxLength(rowData.getBilledToEpsGst(), "Billed To EPS GST", 100, rowNumber, errors);

        return errors;
    }

    @Override
    public boolean isDuplicate(InvoiceBulkUploadDto rowData) {
        if (rowData.getInvoiceNumber() == null || rowData.getInvoiceNumber().trim().isEmpty()) {
            return false;
        }
        return invoiceRepository.existsByInvoiceNumber(rowData.getInvoiceNumber().trim());
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

    private void validateOptionalDate(String dateStr, String fieldName, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (dateStr != null && !dateStr.trim().isEmpty() && !isValidDate(dateStr.trim())) {
            errors.add(createError(rowNumber, fieldName, "Invalid date format. Use yyyy-MM-dd, dd/MM/yyyy, or MM/dd/yyyy", dateStr));
        }
    }

    private void validateOptionalBigDecimal(String value, String fieldName, int precision, int scale, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                new BigDecimal(value.trim());
            } catch (NumberFormatException e) {
                errors.add(createError(rowNumber, fieldName, "Invalid number format", value));
            }
        }
    }

    private void validateMaxLength(String value, String fieldName, int maxLength, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (value != null && value.trim().length() > maxLength) {
            errors.add(createError(rowNumber, fieldName, fieldName + " cannot exceed " + maxLength + " characters", value));
        }
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
