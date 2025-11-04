package com.eps.module.api.epsone.invoice.service;

import com.eps.module.api.epsone.invoice.dto.InvoiceRequestDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceResponseDto;
import com.eps.module.api.epsone.invoice.mapper.InvoiceMapper;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.payment.Invoice;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PaymentDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PayeeRepository payeeRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final InvoiceMapper invoiceMapper;

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
