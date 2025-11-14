package com.eps.module.api.epsone.expenditures_invoice.service;

import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceBulkUploadDto;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceRequestDto;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.cost.ExpendituresInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExpendituresInvoiceService extends BulkUploadService<ExpendituresInvoiceBulkUploadDto, ExpendituresInvoice> {

    ExpendituresInvoiceResponseDto createExpendituresInvoice(ExpendituresInvoiceRequestDto requestDto);

    Page<ExpendituresInvoiceResponseDto> getAllExpendituresInvoices(Pageable pageable);

    Page<ExpendituresInvoiceResponseDto> searchExpendituresInvoices(String searchTerm, Pageable pageable);

    ExpendituresInvoiceResponseDto getExpendituresInvoiceById(Long id);

    ExpendituresInvoiceResponseDto updateExpendituresInvoice(Long id, ExpendituresInvoiceRequestDto requestDto);

    void deleteExpendituresInvoice(Long id);

    Page<ExpendituresInvoiceResponseDto> getExpendituresInvoicesByProjectId(Long projectId, Pageable pageable);

    List<ExpendituresInvoiceResponseDto> getExpendituresInvoicesByInvoiceId(Long invoiceId);

    List<ExpendituresInvoiceResponseDto> getAllExpendituresInvoicesList();
}
