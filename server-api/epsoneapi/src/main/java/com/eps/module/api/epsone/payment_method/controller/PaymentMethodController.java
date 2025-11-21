package com.eps.module.api.epsone.payment_method.controller;

import com.eps.module.api.epsone.payment_method.dto.PaymentMethodBulkUploadDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodRequestDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodResponseDto;
import com.eps.module.api.epsone.payment_method.service.PaymentMethodService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
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

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    @RequireAdmin
    public ResponseEntity<?> createPaymentMethod(@Valid @RequestBody PaymentMethodRequestDto requestDto) {
        PaymentMethodResponseDto response = paymentMethodService.createPaymentMethod(requestDto);
        return ResponseBuilder.success(response, "Payment method created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllPaymentMethods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PaymentMethodResponseDto> paymentMethods = paymentMethodService.getAllPaymentMethods(pageable);

        return ResponseBuilder.success(paymentMethods, "Payment methods retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPaymentMethods(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PaymentMethodResponseDto> paymentMethods = paymentMethodService.searchPaymentMethods(searchTerm, pageable);

        return ResponseBuilder.success(paymentMethods, "Payment methods search completed");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPaymentMethodsList() {
        List<PaymentMethodResponseDto> paymentMethods = paymentMethodService.getPaymentMethodsList();
        return ResponseBuilder.success(paymentMethods, "Payment methods list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethodResponseDto paymentMethod = paymentMethodService.getPaymentMethodById(id);
        return ResponseBuilder.success(paymentMethod, "Payment method retrieved successfully");
    }

    @PutMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<?> updatePaymentMethod(
            @PathVariable Long id,
            @Valid @RequestBody PaymentMethodRequestDto requestDto) {
        PaymentMethodResponseDto response = paymentMethodService.updatePaymentMethod(id, requestDto);
        return ResponseBuilder.success(response, "Payment method updated successfully");
    }

    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<?> deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseBuilder.success(null, "Payment method deleted successfully");
    }

    @PostMapping("/bulk-upload")
    @RequireAdmin
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws java.io.IOException {
        return bulkUploadControllerHelper.bulkUpload(file, paymentMethodService);
    }

    @GetMapping("/export")
    @RequireAdmin
    public ResponseEntity<byte[]> exportPaymentMethods() throws java.io.IOException {
        return bulkUploadControllerHelper.export(paymentMethodService);
    }

    @GetMapping("/download-template")
    @RequireAdmin
    public ResponseEntity<byte[]> downloadTemplate() throws java.io.IOException {
        return bulkUploadControllerHelper.downloadTemplate(paymentMethodService);
    }

    @PostMapping("/export-errors")
    @RequireAdmin
    public ResponseEntity<byte[]> exportErrors(@RequestBody com.eps.module.common.bulk.dto.BulkUploadProgressDto progressData) throws java.io.IOException {
        return bulkUploadControllerHelper.exportErrors(progressData, paymentMethodService);
    }
}
