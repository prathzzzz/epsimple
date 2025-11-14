package com.eps.module.api.epsone.invoice.processor;

import com.eps.module.api.epsone.invoice.dto.InvoiceBulkUploadDto;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.invoice.validator.InvoiceBulkUploadValidator;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.payment.Invoice;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PayeeDetails;
import com.eps.module.payment.PaymentDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceBulkUploadProcessor extends BulkUploadProcessor<InvoiceBulkUploadDto, Invoice> {

    private final InvoiceRepository invoiceRepository;
    private final PayeeDetailsRepository payeeDetailsRepository;
    private final PayeeRepository payeeRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final InvoiceBulkUploadValidator validator;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    };

    @Override
    protected InvoiceBulkUploadValidator getValidator() {
        return validator;
    }

    @Override
    @Transactional
    protected Invoice convertToEntity(InvoiceBulkUploadDto dto) {
        // Find Payee by PayeeDetails name
        PayeeDetails payeeDetails = payeeDetailsRepository
                .findByPayeeNameIgnoreCase(dto.getPayeeName().trim())
                .orElseThrow(() -> new RuntimeException("Payee details not found: " + dto.getPayeeName()));

        Payee payee = payeeRepository.findByPayeeDetailsId(payeeDetails.getId())
                .orElseThrow(() -> new RuntimeException("Payee not found for payee details: " + dto.getPayeeName()));

        // Find Payment Details (optional)
        PaymentDetails paymentDetails = null;
        if (dto.getPaymentTransactionNumber() != null && !dto.getPaymentTransactionNumber().trim().isEmpty()) {
            paymentDetails = paymentDetailsRepository
                    .findByTransactionNumberIgnoreCase(dto.getPaymentTransactionNumber().trim())
                    .orElse(null);
        }

        // Build Invoice entity
        Invoice invoice = Invoice.builder()
                .invoiceNumber(dto.getInvoiceNumber().trim())
                .invoiceDate(parseDate(dto.getInvoiceDate()))
                .invoiceReceivedDate(parseDate(dto.getInvoiceReceivedDate()))
                .orderNumber(trimOrNull(dto.getOrderNumber()))
                .vendorName(trimOrNull(dto.getVendorName()))
                .paymentDueDate(parseDate(dto.getPaymentDueDate()))
                .paymentStatus(trimOrNull(dto.getPaymentStatus()))
                .paidDate(parseDate(dto.getPaidDate()))
                .quantity(parseBigDecimal(dto.getQuantity()))
                .unit(trimOrNull(dto.getUnit()))
                .unitPrice(parseBigDecimal(dto.getUnitPrice()))
                .taxCgstPercentage(parseBigDecimal(dto.getTaxCgstPercentage()))
                .taxSgstPercentage(parseBigDecimal(dto.getTaxSgstPercentage()))
                .taxIgstPercentage(parseBigDecimal(dto.getTaxIgstPercentage()))
                .basicAmount(parseBigDecimal(dto.getBasicAmount()))
                .cgst(parseBigDecimal(dto.getCgst()))
                .sgst(parseBigDecimal(dto.getSgst()))
                .igst(parseBigDecimal(dto.getIgst()))
                .amount1(parseBigDecimal(dto.getAmount1()))
                .amount2(parseBigDecimal(dto.getAmount2()))
                .discountPercentage(parseBigDecimal(dto.getDiscountPercentage()))
                .discountAmount(parseBigDecimal(dto.getDiscountAmount()))
                .tds(parseBigDecimal(dto.getTds()))
                .advanceAmount(parseBigDecimal(dto.getAdvanceAmount()))
                .totalAmount(parseBigDecimal(dto.getTotalAmount()))
                .totalInvoiceValue(parseBigDecimal(dto.getTotalInvoiceValue()))
                .netPayable(parseBigDecimal(dto.getNetPayable()))
                .machineSerialNumber(trimOrNull(dto.getMachineSerialNumber()))
                .masterPoNumber(trimOrNull(dto.getMasterPoNumber()))
                .masterPoDate(parseDate(dto.getMasterPoDate()))
                .dispatchOrderNumber(trimOrNull(dto.getDispatchOrderNumber()))
                .dispatchOrderDate(parseDate(dto.getDispatchOrderDate()))
                .utrDetail(trimOrNull(dto.getUtrDetail()))
                .billedByVendorGst(trimOrNull(dto.getBilledByVendorGst()))
                .billedToEpsGst(trimOrNull(dto.getBilledToEpsGst()))
                .remarks(trimOrNull(dto.getRemarks()))
                .build();

        // Set relationships
        invoice.setPayee(payee);
        invoice.setPaymentDetails(paymentDetails);

        return invoice;
    }

    @Override
    @Transactional
    protected void saveEntity(Invoice entity) {
        invoiceRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(InvoiceBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Invoice Number", dto.getInvoiceNumber());
        rowData.put("Invoice Date", dto.getInvoiceDate());
        rowData.put("Invoice Received Date", dto.getInvoiceReceivedDate());
        rowData.put("Order Number", dto.getOrderNumber());
        rowData.put("Vendor Name", dto.getVendorName());
        rowData.put("Payee Name", dto.getPayeeName());
        rowData.put("Payment Transaction Number", dto.getPaymentTransactionNumber());
        rowData.put("Payment Due Date", dto.getPaymentDueDate());
        rowData.put("Payment Status", dto.getPaymentStatus());
        rowData.put("Paid Date", dto.getPaidDate());
        rowData.put("Quantity", dto.getQuantity());
        rowData.put("Unit", dto.getUnit());
        rowData.put("Unit Price", dto.getUnitPrice());
        rowData.put("Tax CGST Percentage", dto.getTaxCgstPercentage());
        rowData.put("Tax SGST Percentage", dto.getTaxSgstPercentage());
        rowData.put("Tax IGST Percentage", dto.getTaxIgstPercentage());
        rowData.put("Basic Amount", dto.getBasicAmount());
        rowData.put("CGST", dto.getCgst());
        rowData.put("SGST", dto.getSgst());
        rowData.put("IGST", dto.getIgst());
        rowData.put("Amount 1", dto.getAmount1());
        rowData.put("Amount 2", dto.getAmount2());
        rowData.put("Discount Percentage", dto.getDiscountPercentage());
        rowData.put("Discount Amount", dto.getDiscountAmount());
        rowData.put("TDS", dto.getTds());
        rowData.put("Advance Amount", dto.getAdvanceAmount());
        rowData.put("Total Amount", dto.getTotalAmount());
        rowData.put("Total Invoice Value", dto.getTotalInvoiceValue());
        rowData.put("Net Payable", dto.getNetPayable());
        rowData.put("Machine Serial Number", dto.getMachineSerialNumber());
        rowData.put("Master PO Number", dto.getMasterPoNumber());
        rowData.put("Master PO Date", dto.getMasterPoDate());
        rowData.put("Dispatch Order Number", dto.getDispatchOrderNumber());
        rowData.put("Dispatch Order Date", dto.getDispatchOrderDate());
        rowData.put("UTR Detail", dto.getUtrDetail());
        rowData.put("Billed By Vendor GST", dto.getBilledByVendorGst());
        rowData.put("Billed To EPS GST", dto.getBilledToEpsGst());
        rowData.put("Remarks", dto.getRemarks());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(InvoiceBulkUploadDto dto) {
        return (dto.getInvoiceNumber() == null || dto.getInvoiceNumber().trim().isEmpty()) &&
                (dto.getInvoiceDate() == null || dto.getInvoiceDate().trim().isEmpty()) &&
                (dto.getPayeeName() == null || dto.getPayeeName().trim().isEmpty()) &&
                (dto.getTotalInvoiceValue() == null || dto.getTotalInvoiceValue().trim().isEmpty());
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
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
