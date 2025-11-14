package com.eps.module.api.epsone.expenditures_invoice.controller;

import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceRequestDto;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceResponseDto;
import com.eps.module.api.epsone.expenditures_invoice.service.ExpendituresInvoiceService;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/expenditures/invoices")
@RequiredArgsConstructor
public class ExpendituresInvoiceController {

    private final ExpendituresInvoiceService expendituresInvoiceService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    // ========== Bulk Upload Endpoints ==========

    @PostMapping("/bulk-upload")
    public SseEmitter bulkUploadExpendituresInvoices(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("POST /api/expenditures/invoices/bulk-upload - Starting bulk upload");
        return bulkUploadControllerHelper.bulkUpload(file, expendituresInvoiceService);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        log.info("GET /api/expenditures/invoices/bulk-upload/template - Downloading template");
        return bulkUploadControllerHelper.downloadTemplate(expendituresInvoiceService);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> exportErrorReport(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        log.info("POST /api/expenditures/invoices/bulk-upload/errors - Exporting error report");
        return bulkUploadControllerHelper.exportErrors(progressData, expendituresInvoiceService);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws Exception {
        log.info("GET /api/expenditures/invoices/export - Exporting all data");
        return bulkUploadControllerHelper.export(expendituresInvoiceService);
    }

    // ========== CRUD Endpoints ==========

    @PostMapping
    public ResponseEntity<ApiResponse<ExpendituresInvoiceResponseDto>> createExpendituresInvoice(
            @Valid @RequestBody ExpendituresInvoiceRequestDto requestDto) {
        log.info("POST /api/expenditures/invoices - Creating expenditures invoice");
        ExpendituresInvoiceResponseDto responseDto = expendituresInvoiceService.createExpendituresInvoice(requestDto);
        return ResponseBuilder.success(responseDto, "Expenditures invoice created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExpendituresInvoiceResponseDto>>> getAllExpendituresInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("GET /api/expenditures/invoices - Fetching all expenditures invoices");
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ExpendituresInvoiceResponseDto> responsePage = expendituresInvoiceService.getAllExpendituresInvoices(pageable);
        return ResponseBuilder.success(responsePage, "Expenditures invoices fetched successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ExpendituresInvoiceResponseDto>>> searchExpendituresInvoices(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("GET /api/expenditures/invoices/search - Searching expenditures invoices with term: {}", searchTerm);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ExpendituresInvoiceResponseDto> responsePage = expendituresInvoiceService.searchExpendituresInvoices(searchTerm, pageable);
        return ResponseBuilder.success(responsePage, "Expenditures invoices searched successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ExpendituresInvoiceResponseDto>>> getAllExpendituresInvoicesList() {
        log.info("GET /api/expenditures/invoices/list - Fetching all expenditures invoices as list");
        List<ExpendituresInvoiceResponseDto> responseList = expendituresInvoiceService.getAllExpendituresInvoicesList();
        return ResponseBuilder.success(responseList, "Expenditures invoices list fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpendituresInvoiceResponseDto>> getExpendituresInvoiceById(@PathVariable Long id) {
        log.info("GET /api/expenditures/invoices/{} - Fetching expenditures invoice", id);
        ExpendituresInvoiceResponseDto responseDto = expendituresInvoiceService.getExpendituresInvoiceById(id);
        return ResponseBuilder.success(responseDto, "Expenditures invoice fetched successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpendituresInvoiceResponseDto>> updateExpendituresInvoice(
            @PathVariable Long id,
            @Valid @RequestBody ExpendituresInvoiceRequestDto requestDto) {
        log.info("PUT /api/expenditures/invoices/{} - Updating expenditures invoice", id);
        ExpendituresInvoiceResponseDto responseDto = expendituresInvoiceService.updateExpendituresInvoice(id, requestDto);
        return ResponseBuilder.success(responseDto, "Expenditures invoice updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpendituresInvoice(@PathVariable Long id) {
        log.info("DELETE /api/expenditures/invoices/{} - Deleting expenditures invoice", id);
        expendituresInvoiceService.deleteExpendituresInvoice(id);
        return ResponseBuilder.success(null, "Expenditures invoice deleted successfully");
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<Page<ExpendituresInvoiceResponseDto>>> getExpendituresInvoicesByProjectId(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("GET /api/expenditures/invoices/project/{} - Fetching expenditures invoices for project", projectId);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ExpendituresInvoiceResponseDto> responsePage = expendituresInvoiceService.getExpendituresInvoicesByProjectId(projectId, pageable);
        return ResponseBuilder.success(responsePage, "Expenditures invoices for project fetched successfully");
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<ApiResponse<List<ExpendituresInvoiceResponseDto>>> getExpendituresInvoicesByInvoiceId(
            @PathVariable Long invoiceId) {
        log.info("GET /api/expenditures/invoices/invoice/{} - Fetching expenditures for invoice", invoiceId);
        List<ExpendituresInvoiceResponseDto> responseList = expendituresInvoiceService.getExpendituresInvoicesByInvoiceId(invoiceId);
        return ResponseBuilder.success(responseList, "Expenditures for invoice fetched successfully");
    }
}
