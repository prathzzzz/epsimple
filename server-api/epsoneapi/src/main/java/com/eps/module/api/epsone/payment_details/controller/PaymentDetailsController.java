package com.eps.module.api.epsone.payment_details.controller;

import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsRequestDto;
import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsResponseDto;
import com.eps.module.api.epsone.payment_details.service.PaymentDetailsService;
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
@RequestMapping("/api/payment-details")
@RequiredArgsConstructor
public class PaymentDetailsController {

    private final PaymentDetailsService paymentDetailsService;

    @PostMapping
    public ResponseEntity<?> createPaymentDetails(@Valid @RequestBody PaymentDetailsRequestDto requestDto) {
        PaymentDetailsResponseDto response = paymentDetailsService.createPaymentDetails(requestDto);
        return ResponseBuilder.success(response, "Payment details created successfully");
    }

    @GetMapping
    public ResponseEntity<?> getAllPaymentDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PaymentDetailsResponseDto> paymentDetails = paymentDetailsService.getAllPaymentDetails(pageable);

        return ResponseBuilder.success(paymentDetails, "Payment details retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPaymentDetails(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PaymentDetailsResponseDto> paymentDetails = paymentDetailsService.searchPaymentDetails(searchTerm, pageable);

        return ResponseBuilder.success(paymentDetails, "Payment details search completed");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPaymentDetailsList() {
        List<PaymentDetailsResponseDto> paymentDetails = paymentDetailsService.getPaymentDetailsList();
        return ResponseBuilder.success(paymentDetails, "Payment details list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentDetailsById(@PathVariable Long id) {
        PaymentDetailsResponseDto paymentDetails = paymentDetailsService.getPaymentDetailsById(id);
        return ResponseBuilder.success(paymentDetails, "Payment details retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePaymentDetails(
            @PathVariable Long id,
            @Valid @RequestBody PaymentDetailsRequestDto requestDto) {
        PaymentDetailsResponseDto response = paymentDetailsService.updatePaymentDetails(id, requestDto);
        return ResponseBuilder.success(response, "Payment details updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentDetails(@PathVariable Long id) {
        paymentDetailsService.deletePaymentDetails(id);
        return ResponseBuilder.success(null, "Payment details deleted successfully");
    }
}
