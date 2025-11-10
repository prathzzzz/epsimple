package com.eps.module.api.epsone.payee.controller;

import com.eps.module.api.epsone.payee.dto.PayeeRequestDto;
import com.eps.module.api.epsone.payee.dto.PayeeResponseDto;
import com.eps.module.api.epsone.payee.service.PayeeService;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/payees")
@RequiredArgsConstructor
@Slf4j
public class PayeeController {

    private final PayeeService payeeService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    public ResponseEntity<?> createPayee(@Valid @RequestBody PayeeRequestDto requestDto) {
        log.info("Received request to create payee");
        PayeeResponseDto response = payeeService.createPayee(requestDto);
        return ResponseBuilder.success(response, "Payee created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllPayees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Received request to get all payees");
        String sanitizedSortBy = sanitizeSortBy(sortBy);
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sanitizedSortBy));
        Page<PayeeResponseDto> response = payeeService.getAllPayees(pageable);
        return ResponseBuilder.success(response, "Payees retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPayees(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Received request to search payees with term: {}", searchTerm);
        String sanitizedSortBy = sanitizeSortBy(sortBy);
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sanitizedSortBy));
        Page<PayeeResponseDto> response = payeeService.searchPayees(searchTerm, pageable);
        return ResponseBuilder.success(response, "Payees search completed successfully", HttpStatus.OK);
    }

    /**
     * Map client-facing sortBy values to real entity properties to avoid invalid paths in JPQL.
     * Extend this mapping if the frontend sends other friendly names.
     */
    private String sanitizeSortBy(String sortBy) {
        if (sortBy == null) return "id";
        switch (sortBy) {
            // frontend uses "payeeName" as a friendly column name — map it to the nested property
            case "payeeName":
                return "payeeDetails.payeeName";
            case "accountNumber":
                return "payeeDetails.accountNumber";
            case "bankName":
                return "payeeDetails.bank.bankName";
            case "payeeType":
                return "payeeType.payeeType";
            default:
                return sortBy;
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPayeesList() {
        log.info("Received request to get payees list");
        List<PayeeResponseDto> response = payeeService.getPayeesList();
        return ResponseBuilder.success(response, "Payees list retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayeeById(@PathVariable Long id) {
        log.info("Received request to get payee by id: {}", id);
        PayeeResponseDto response = payeeService.getPayeeById(id);
        return ResponseBuilder.success(response, "Payee retrieved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayee(
            @PathVariable Long id,
            @Valid @RequestBody PayeeRequestDto requestDto) {
        log.info("Received request to update payee with id: {}", id);
        PayeeResponseDto response = payeeService.updatePayee(id, requestDto);
        return ResponseBuilder.success(response, "Payee updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayee(@PathVariable Long id) {
        log.info("Received request to delete payee with id: {}", id);
        payeeService.deletePayee(id);
        return ResponseBuilder.success(null, "Payee deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<?> countPayees() {
        log.info("Received request to count payees");
        long count = payeeService.countPayees();
        return ResponseBuilder.success(count, "Payee count retrieved successfully", HttpStatus.OK);
    }

    // ========== Bulk Upload Endpoints ==========

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("Received bulk upload request for Payees");
        return bulkUploadControllerHelper.bulkUpload(file, payeeService);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        log.info("Received request to download Payee bulk upload template");
        return bulkUploadControllerHelper.downloadTemplate(payeeService);
    }

    @GetMapping("/bulk-upload/export")
    public ResponseEntity<byte[]> exportData() throws Exception {
        log.info("Received request to export Payee data");
        return bulkUploadControllerHelper.export(payeeService);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> exportErrors(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        log.info("Received request to export Payee bulk upload errors");
        return bulkUploadControllerHelper.exportErrors(progressData, payeeService);
    }
}
