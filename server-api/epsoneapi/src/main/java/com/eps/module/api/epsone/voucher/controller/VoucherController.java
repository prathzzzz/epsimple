package com.eps.module.api.epsone.voucher.controller;

import com.eps.module.api.epsone.voucher.dto.VoucherRequestDto;
import com.eps.module.api.epsone.voucher.dto.VoucherResponseDto;
import com.eps.module.api.epsone.voucher.service.VoucherService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    public ResponseEntity<?> createVoucher(@Valid @RequestBody VoucherRequestDto requestDto) {
        VoucherResponseDto response = voucherService.createVoucher(requestDto);
        return ResponseBuilder.success(response, "Voucher created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllVouchers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VoucherResponseDto> vouchers = voucherService.getAllVouchers(pageable);

        return ResponseBuilder.success(vouchers, "Vouchers retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchVouchers(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VoucherResponseDto> vouchers = voucherService.searchVouchers(searchTerm, pageable);

        return ResponseBuilder.success(vouchers, "Vouchers search completed");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getVouchersList() {
        List<VoucherResponseDto> vouchers = voucherService.getVouchersList();
        return ResponseBuilder.success(vouchers, "Vouchers list retrieved successfully");
    }

    @GetMapping("/payee/{payeeId}")
    public ResponseEntity<?> getVouchersByPayee(
            @PathVariable Long payeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VoucherResponseDto> vouchers = voucherService.getVouchersByPayee(payeeId, pageable);

        return ResponseBuilder.success(vouchers, "Vouchers retrieved for payee");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVoucherById(@PathVariable Long id) {
        VoucherResponseDto voucher = voucherService.getVoucherById(id);
        return ResponseBuilder.success(voucher, "Voucher retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVoucher(
            @PathVariable Long id,
            @Valid @RequestBody VoucherRequestDto requestDto) {
        VoucherResponseDto response = voucherService.updateVoucher(id, requestDto);
        return ResponseBuilder.success(response, "Voucher updated successfully");
    }

    @PutMapping("/{id}/payment-status")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String paymentStatus = request.get("paymentStatus");
        if (paymentStatus == null || paymentStatus.isBlank()) {
            return ResponseBuilder.error("Payment status is required", HttpStatus.BAD_REQUEST);
        }
        VoucherResponseDto response = voucherService.updatePaymentStatus(id, paymentStatus);
        return ResponseBuilder.success(response, "Payment status updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseBuilder.success(null, "Voucher deleted successfully");
    }

    // ==================== Bulk Upload Endpoints ====================

    @PostMapping("/bulk-upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return bulkUploadControllerHelper.bulkUpload(file, voucherService);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        return bulkUploadControllerHelper.downloadTemplate(voucherService);
    }

    @PostMapping(value = "/bulk-upload/errors", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportErrors(@RequestBody BulkUploadProgressDto progressDto) throws IOException {
        return bulkUploadControllerHelper.exportErrors(progressDto, voucherService);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws Exception {
        return bulkUploadControllerHelper.export(voucherService);
    }
}
