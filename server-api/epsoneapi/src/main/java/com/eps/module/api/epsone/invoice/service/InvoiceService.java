package com.eps.module.api.epsone.invoice.service;

import com.eps.module.api.epsone.invoice.dto.InvoiceBulkUploadDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceRequestDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.payment.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InvoiceService extends BulkUploadService<InvoiceBulkUploadDto, Invoice> {

    InvoiceResponseDto createInvoice(InvoiceRequestDto requestDto);

    Page<InvoiceResponseDto> getAllInvoices(Pageable pageable);

    Page<InvoiceResponseDto> searchInvoices(String searchTerm, Pageable pageable);

    List<InvoiceResponseDto> getInvoicesList();

    List<InvoiceResponseDto> getInvoicesByPayeeId(Long payeeId);

    InvoiceResponseDto getInvoiceById(Long id);

    InvoiceResponseDto updateInvoice(Long id, InvoiceRequestDto requestDto);

    InvoiceResponseDto updatePaymentStatus(Long id, String paymentStatus);

    void deleteInvoice(Long id);
}
