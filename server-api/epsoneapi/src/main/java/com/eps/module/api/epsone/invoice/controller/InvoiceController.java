package com.eps.module.api.epsone.invoice.controller;

import com.eps.module.api.epsone.invoice.dto.InvoiceBulkUploadDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceErrorReportDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceRequestDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceResponseDto;
import com.eps.module.api.epsone.invoice.service.InvoiceService;
import com.eps.module.auth.rbac.annotation.RequirePermission;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    // ========== Bulk Upload Endpoints ==========

    @PostMapping("/bulk-upload")
    @RequirePermission("INVOICE:BULK_UPLOAD")
    public SseEmitter bulkUploadInvoices(@RequestParam("file") MultipartFile file) throws Exception {
        return bulkUploadControllerHelper.bulkUpload(file, invoiceService);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        return bulkUploadControllerHelper.downloadTemplate(invoiceService);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> exportErrorReport(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        return bulkUploadControllerHelper.exportErrors(progressData, invoiceService);
    }

    // ========== CRUD Endpoints ==========


    @PostMapping
    @RequirePermission("INVOICE:CREATE")
    public ResponseEntity<?> createInvoice(@Valid @RequestBody InvoiceRequestDto requestDto) {
        InvoiceResponseDto response = invoiceService.createInvoice(requestDto);
        return ResponseBuilder.success(response, "Invoice created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "invoiceDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InvoiceResponseDto> invoices = invoiceService.getAllInvoices(pageable);

        return ResponseBuilder.success(invoices, "Invoices retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchInvoices(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "invoiceDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InvoiceResponseDto> invoices = invoiceService.searchInvoices(searchTerm, pageable);

        return ResponseBuilder.success(invoices, "Invoice search completed");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getInvoicesList() {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesList();
        return ResponseBuilder.success(invoices, "Invoices list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    @RequirePermission("INVOICE:EXPORT")
    public ResponseEntity<byte[]> exportData() throws Exception {
        return bulkUploadControllerHelper.export(invoiceService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/payee/{payeeId}")
    public ResponseEntity<?> getInvoicesByPayeeId(@PathVariable Long payeeId) {
        List<InvoiceResponseDto> invoices = invoiceService.getInvoicesByPayeeId(payeeId);
        return ResponseBuilder.success(invoices, "Invoices retrieved for payee successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        InvoiceResponseDto invoice = invoiceService.getInvoiceById(id);
        return ResponseBuilder.success(invoice, "Invoice retrieved successfully");
    }

    @PutMapping("/{id}")
    @RequirePermission("INVOICE:UPDATE")
    public ResponseEntity<?> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceRequestDto requestDto) {
        InvoiceResponseDto response = invoiceService.updateInvoice(id, requestDto);
        return ResponseBuilder.success(response, "Invoice updated successfully");
    }

    @PutMapping("/{id}/payment-status")
    @RequirePermission("INVOICE:UPDATE")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String paymentStatus = request.get("paymentStatus");
        if (paymentStatus == null || paymentStatus.isBlank()) {
            return ResponseBuilder.error("Payment status is required", HttpStatus.BAD_REQUEST);
        }
        InvoiceResponseDto response = invoiceService.updatePaymentStatus(id, paymentStatus);
        return ResponseBuilder.success(response, "Payment status updated successfully");
    }

    @DeleteMapping("/{id}")
    @RequirePermission("INVOICE:DELETE")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseBuilder.success(null, "Invoice deleted successfully");
    }
}
