package com.eps.module.api.epsone.payee_type.controller;

import com.eps.module.api.epsone.payee_type.dto.PayeeTypeRequestDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeResponseDto;
import com.eps.module.api.epsone.payee_type.service.PayeeTypeService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/payee-types")
@RequiredArgsConstructor
@RequireAdmin
public class PayeeTypeController {

    private final PayeeTypeService payeeTypeService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    public ResponseEntity<?> createPayeeType(@Valid @RequestBody PayeeTypeRequestDto requestDto) {
        PayeeTypeResponseDto response = payeeTypeService.createPayeeType(requestDto);
        return ResponseBuilder.success(response, "Payee type created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllPayeeTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PayeeTypeResponseDto> payeeTypes = payeeTypeService.getAllPayeeTypes(pageable);

        return ResponseBuilder.success(payeeTypes, "Payee types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPayeeTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PayeeTypeResponseDto> payeeTypes = payeeTypeService.searchPayeeTypes(searchTerm, pageable);

        return ResponseBuilder.success(payeeTypes, "Payee types search completed");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPayeeTypesList() {
        List<PayeeTypeResponseDto> payeeTypes = payeeTypeService.getPayeeTypesList();
        return ResponseBuilder.success(payeeTypes, "Payee types list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayeeTypeById(@PathVariable Long id) {
        PayeeTypeResponseDto payeeType = payeeTypeService.getPayeeTypeById(id);
        return ResponseBuilder.success(payeeType, "Payee type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayeeType(
            @PathVariable Long id,
            @Valid @RequestBody PayeeTypeRequestDto requestDto) {
        PayeeTypeResponseDto response = payeeTypeService.updatePayeeType(id, requestDto);
        return ResponseBuilder.success(response, "Payee type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayeeType(@PathVariable Long id) {
        payeeTypeService.deletePayeeType(id);
        return ResponseBuilder.success(null, "Payee type deleted successfully");
    }

    // Bulk upload endpoints
    @PostMapping("/bulk-upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return bulkUploadHelper.bulkUpload(file, payeeTypeService);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export() throws IOException {
        return bulkUploadHelper.export(payeeTypeService);
    }

    @GetMapping("/download-template")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        return bulkUploadHelper.downloadTemplate(payeeTypeService);
    }

    @PostMapping("/export-errors")
    public ResponseEntity<byte[]> exportErrors(@RequestBody BulkUploadProgressDto progressData) throws IOException {
        return bulkUploadHelper.exportErrors(progressData, payeeTypeService);
    }
}
