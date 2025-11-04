package com.eps.module.api.epsone.payment_method.controller;

import com.eps.module.api.epsone.payment_method.dto.PaymentMethodRequestDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodResponseDto;
import com.eps.module.api.epsone.payment_method.service.PaymentMethodService;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping
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
    public ResponseEntity<?> updatePaymentMethod(
            @PathVariable Long id,
            @Valid @RequestBody PaymentMethodRequestDto requestDto) {
        PaymentMethodResponseDto response = paymentMethodService.updatePaymentMethod(id, requestDto);
        return ResponseBuilder.success(response, "Payment method updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseBuilder.success(null, "Payment method deleted successfully");
    }
}
