package com.eps.module.api.epsone.invoice.service;

import com.eps.module.api.epsone.invoice.dto.InvoiceBulkUploadDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceErrorReportDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceRequestDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceResponseDto;
import com.eps.module.api.epsone.invoice.mapper.InvoiceMapper;
import com.eps.module.api.epsone.invoice.processor.InvoiceBulkUploadProcessor;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.payment.Invoice;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PaymentDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl extends BaseBulkUploadService<InvoiceBulkUploadDto, Invoice> implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PayeeRepository payeeRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceBulkUploadProcessor invoiceBulkUploadProcessor;

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<InvoiceBulkUploadDto, Invoice> getProcessor() {
        return invoiceBulkUploadProcessor;
    }

    @Override
    public Class<InvoiceBulkUploadDto> getBulkUploadDtoClass() {
        return InvoiceBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Invoice";
    }

    @Override
    public List<Invoice> getAllEntitiesForExport() {
        return invoiceRepository.findAllForExport();
    }

    @Override
    public Function<Invoice, InvoiceBulkUploadDto> getEntityToDtoMapper() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        return invoice -> {
            String payeeName = invoice.getPayee() != null && invoice.getPayee().getPayeeDetails() != null 
                    ? invoice.getPayee().getPayeeDetails().getPayeeName() : "";
            
            String paymentTxnNumber = invoice.getPaymentDetails() != null 
                    ? invoice.getPaymentDetails().getTransactionNumber() : "";

            return InvoiceBulkUploadDto.builder()
                    .invoiceNumber(invoice.getInvoiceNumber())
                    .invoiceDate(invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().format(dateFormatter) : "")
                    .invoiceReceivedDate(invoice.getInvoiceReceivedDate() != null ? invoice.getInvoiceReceivedDate().format(dateFormatter) : "")
                    .orderNumber(invoice.getOrderNumber())
                    .vendorName(invoice.getVendorName())
                    .payeeName(payeeName)
                    .paymentTransactionNumber(paymentTxnNumber)
                    .paymentDueDate(invoice.getPaymentDueDate() != null ? invoice.getPaymentDueDate().format(dateFormatter) : "")
                    .paymentStatus(invoice.getPaymentStatus())
                    .paidDate(invoice.getPaidDate() != null ? invoice.getPaidDate().format(dateFormatter) : "")
                    .quantity(invoice.getQuantity() != null ? invoice.getQuantity().toString() : "")
                    .unit(invoice.getUnit())
                    .unitPrice(invoice.getUnitPrice() != null ? invoice.getUnitPrice().toString() : "")
                    .taxCgstPercentage(invoice.getTaxCgstPercentage() != null ? invoice.getTaxCgstPercentage().toString() : "")
                    .taxSgstPercentage(invoice.getTaxSgstPercentage() != null ? invoice.getTaxSgstPercentage().toString() : "")
                    .taxIgstPercentage(invoice.getTaxIgstPercentage() != null ? invoice.getTaxIgstPercentage().toString() : "")
                    .basicAmount(invoice.getBasicAmount() != null ? invoice.getBasicAmount().toString() : "")
                    .cgst(invoice.getCgst() != null ? invoice.getCgst().toString() : "")
                    .sgst(invoice.getSgst() != null ? invoice.getSgst().toString() : "")
                    .igst(invoice.getIgst() != null ? invoice.getIgst().toString() : "")
                    .amount1(invoice.getAmount1() != null ? invoice.getAmount1().toString() : "")
                    .amount2(invoice.getAmount2() != null ? invoice.getAmount2().toString() : "")
                    .discountPercentage(invoice.getDiscountPercentage() != null ? invoice.getDiscountPercentage().toString() : "")
                    .discountAmount(invoice.getDiscountAmount() != null ? invoice.getDiscountAmount().toString() : "")
                    .tds(invoice.getTds() != null ? invoice.getTds().toString() : "")
                    .advanceAmount(invoice.getAdvanceAmount() != null ? invoice.getAdvanceAmount().toString() : "")
                    .totalAmount(invoice.getTotalAmount() != null ? invoice.getTotalAmount().toString() : "")
                    .totalInvoiceValue(invoice.getTotalInvoiceValue() != null ? invoice.getTotalInvoiceValue().toString() : "")
                    .netPayable(invoice.getNetPayable() != null ? invoice.getNetPayable().toString() : "")
                    .machineSerialNumber(invoice.getMachineSerialNumber())
                    .masterPoNumber(invoice.getMasterPoNumber())
                    .masterPoDate(invoice.getMasterPoDate() != null ? invoice.getMasterPoDate().format(dateFormatter) : "")
                    .dispatchOrderNumber(invoice.getDispatchOrderNumber())
                    .dispatchOrderDate(invoice.getDispatchOrderDate() != null ? invoice.getDispatchOrderDate().format(dateFormatter) : "")
                    .utrDetail(invoice.getUtrDetail())
                    .billedByVendorGst(invoice.getBilledByVendorGst())
                    .billedToEpsGst(invoice.getBilledToEpsGst())
                    .remarks(invoice.getRemarks())
                    .build();
        };
    }

    @Override
    public InvoiceErrorReportDto buildErrorReportDto(BulkUploadErrorDto errorDto) {
        return InvoiceErrorReportDto.builder()
                .rowNumber(errorDto.getRowNumber())
                .invoiceNumber((String) errorDto.getRowData().get("Invoice Number"))
                .invoiceDate((String) errorDto.getRowData().get("Invoice Date"))
                .invoiceReceivedDate((String) errorDto.getRowData().get("Invoice Received Date"))
                .orderNumber((String) errorDto.getRowData().get("Order Number"))
                .vendorName((String) errorDto.getRowData().get("Vendor Name"))
                .payeeName((String) errorDto.getRowData().get("Payee Name"))
                .paymentTransactionNumber((String) errorDto.getRowData().get("Payment Transaction Number"))
                .paymentDueDate((String) errorDto.getRowData().get("Payment Due Date"))
                .paymentStatus((String) errorDto.getRowData().get("Payment Status"))
                .paidDate((String) errorDto.getRowData().get("Paid Date"))
                .quantity((String) errorDto.getRowData().get("Quantity"))
                .unit((String) errorDto.getRowData().get("Unit"))
                .unitPrice((String) errorDto.getRowData().get("Unit Price"))
                .taxCgstPercentage((String) errorDto.getRowData().get("Tax CGST Percentage"))
                .taxSgstPercentage((String) errorDto.getRowData().get("Tax SGST Percentage"))
                .taxIgstPercentage((String) errorDto.getRowData().get("Tax IGST Percentage"))
                .basicAmount((String) errorDto.getRowData().get("Basic Amount"))
                .cgst((String) errorDto.getRowData().get("CGST"))
                .sgst((String) errorDto.getRowData().get("SGST"))
                .igst((String) errorDto.getRowData().get("IGST"))
                .amount1((String) errorDto.getRowData().get("Amount 1"))
                .amount2((String) errorDto.getRowData().get("Amount 2"))
                .discountPercentage((String) errorDto.getRowData().get("Discount Percentage"))
                .discountAmount((String) errorDto.getRowData().get("Discount Amount"))
                .tds((String) errorDto.getRowData().get("TDS"))
                .advanceAmount((String) errorDto.getRowData().get("Advance Amount"))
                .totalAmount((String) errorDto.getRowData().get("Total Amount"))
                .totalInvoiceValue((String) errorDto.getRowData().get("Total Invoice Value"))
                .netPayable((String) errorDto.getRowData().get("Net Payable"))
                .machineSerialNumber((String) errorDto.getRowData().get("Machine Serial Number"))
                .masterPoNumber((String) errorDto.getRowData().get("Master PO Number"))
                .masterPoDate((String) errorDto.getRowData().get("Master PO Date"))
                .dispatchOrderNumber((String) errorDto.getRowData().get("Dispatch Order Number"))
                .dispatchOrderDate((String) errorDto.getRowData().get("Dispatch Order Date"))
                .utrDetail((String) errorDto.getRowData().get("UTR Detail"))
                .billedByVendorGst((String) errorDto.getRowData().get("Billed By Vendor GST"))
                .billedToEpsGst((String) errorDto.getRowData().get("Billed To EPS GST"))
                .remarks((String) errorDto.getRowData().get("Remarks"))
                .error(errorDto.getErrorMessage())
                .build();
    }

    @Override
    public Class<InvoiceErrorReportDto> getErrorReportDtoClass() {
        return InvoiceErrorReportDto.class;
    }

    // ========== CRUD Methods ==========


    @Override
    @Transactional
    public InvoiceResponseDto createInvoice(InvoiceRequestDto requestDto) {
        log.info("Creating invoice with number: {}", requestDto.getInvoiceNumber());

        // Validate unique invoice number
        if (invoiceRepository.existsByInvoiceNumber(requestDto.getInvoiceNumber())) {
            throw new IllegalArgumentException(
                    "Invoice number '" + requestDto.getInvoiceNumber() + "' already exists");
        }

        // Validate that payee exists
        Payee payee = payeeRepository.findById(requestDto.getPayeeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payee not found with ID: " + requestDto.getPayeeId()));

        // Validate payment details if provided
        PaymentDetails paymentDetails = null;
        if (requestDto.getPaymentDetailsId() != null) {
            paymentDetails = paymentDetailsRepository.findById(requestDto.getPaymentDetailsId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Payment details not found with ID: " + requestDto.getPaymentDetailsId()));
        }

        Invoice invoice = invoiceMapper.toEntity(requestDto);
        invoice.setPayee(payee);
        invoice.setPaymentDetails(paymentDetails);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        log.info("Invoice created successfully with ID: {}", savedInvoice.getId());
        return invoiceMapper.toResponseDto(savedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponseDto> getAllInvoices(Pageable pageable) {
        log.info("Fetching all invoices with pagination: {}", pageable);
        Page<Invoice> invoices = invoiceRepository.findAll(pageable);
        return invoices.map(invoiceMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponseDto> searchInvoices(String searchTerm, Pageable pageable) {
        log.info("Searching invoices with term: {}", searchTerm);
        Page<Invoice> invoices = invoiceRepository.searchInvoices(searchTerm, pageable);
        return invoices.map(invoiceMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getInvoicesList() {
        log.info("Fetching all invoices as list");
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponseDto> getInvoicesByPayeeId(Long payeeId) {
        log.info("Fetching invoices for payee ID: {}", payeeId);
        
        // Validate payee exists
        if (!payeeRepository.existsById(payeeId)) {
            throw new IllegalArgumentException("Payee not found with ID: " + payeeId);
        }
        
        return invoiceRepository.findByPayeeId(payeeId).stream()
                .map(invoiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceById(Long id) {
        log.info("Fetching invoice with ID: {}", id);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + id));
        return invoiceMapper.toResponseDto(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponseDto updateInvoice(Long id, InvoiceRequestDto requestDto) {
        log.info("Updating invoice with ID: {}", id);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + id));

        // Validate unique invoice number if it's being changed
        if (!invoice.getInvoiceNumber().equals(requestDto.getInvoiceNumber()) &&
                invoiceRepository.existsByInvoiceNumberAndIdNot(requestDto.getInvoiceNumber(), id)) {
            throw new IllegalArgumentException(
                    "Invoice number '" + requestDto.getInvoiceNumber() + "' already exists");
        }

        // Validate that payee exists if it's being updated
        if (requestDto.getPayeeId() != null && !requestDto.getPayeeId().equals(invoice.getPayee().getId())) {
            Payee payee = payeeRepository.findById(requestDto.getPayeeId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Payee not found with ID: " + requestDto.getPayeeId()));
            invoice.setPayee(payee);
        }

        // Validate payment details if provided
        if (requestDto.getPaymentDetailsId() != null) {
            PaymentDetails paymentDetails = paymentDetailsRepository.findById(requestDto.getPaymentDetailsId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Payment details not found with ID: " + requestDto.getPaymentDetailsId()));
            invoice.setPaymentDetails(paymentDetails);
        } else {
            invoice.setPaymentDetails(null);
        }

        invoiceMapper.updateEntityFromDto(requestDto, invoice);
        Invoice updatedInvoice = invoiceRepository.save(invoice);

        log.info("Invoice updated successfully with ID: {}", id);
        return invoiceMapper.toResponseDto(updatedInvoice);
    }

    @Override
    @Transactional
    public InvoiceResponseDto updatePaymentStatus(Long id, String paymentStatus) {
        log.info("Updating payment status for invoice ID: {} to: {}", id, paymentStatus);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + id));

        invoice.setPaymentStatus(paymentStatus);
        Invoice updatedInvoice = invoiceRepository.save(invoice);

        log.info("Payment status updated successfully for invoice ID: {}", id);
        return invoiceMapper.toResponseDto(updatedInvoice);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        log.info("Deleting invoice with ID: {}", id);

        if (!invoiceRepository.existsById(id)) {
            throw new IllegalArgumentException("Invoice not found with ID: " + id);
        }

        invoiceRepository.deleteById(id);
        log.info("Invoice deleted successfully with ID: {}", id);
    }
}
