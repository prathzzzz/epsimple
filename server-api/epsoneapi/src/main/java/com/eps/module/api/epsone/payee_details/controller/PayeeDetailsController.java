package com.eps.module.api.epsone.payee_details.controller;

import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsRequestDto;
import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsResponseDto;
import com.eps.module.api.epsone.payee_details.service.PayeeDetailsService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payee-details")
@RequiredArgsConstructor
public class PayeeDetailsController {

    private final PayeeDetailsService payeeDetailsService;

    @PostMapping
    public ResponseEntity<ApiResponse<PayeeDetailsResponseDto>> createPayeeDetails(
            @Valid @RequestBody PayeeDetailsRequestDto requestDto) {
        log.info("Creating new payee details: {}", requestDto.getPayeeName());
        PayeeDetailsResponseDto createdPayeeDetails = payeeDetailsService.createPayeeDetails(requestDto);
        log.info("Successfully created payee details with ID: {}", createdPayeeDetails.getId());
        return ResponseBuilder.success(createdPayeeDetails, "Payee details created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PayeeDetailsResponseDto>>> getAllPayeeDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Fetching all payee details - Page: {}, Size: {}, SortBy: {}, Direction: {}", 
                page, size, sortBy, sortDirection);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<PayeeDetailsResponseDto> payeeDetailsPage = payeeDetailsService.getAllPayeeDetails(pageable);
        log.info("Retrieved {} payee details out of {} total", 
                payeeDetailsPage.getNumberOfElements(), payeeDetailsPage.getTotalElements());
        
        return ResponseBuilder.success(payeeDetailsPage, "Payee details retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PayeeDetailsResponseDto>>> searchPayeeDetails(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "payeeName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Searching payee details with term: '{}' - Page: {}, Size: {}", searchTerm, page, size);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<PayeeDetailsResponseDto> payeeDetailsPage = payeeDetailsService.searchPayeeDetails(searchTerm, pageable);
        log.info("Search found {} payee details", payeeDetailsPage.getTotalElements());
        
        return ResponseBuilder.success(payeeDetailsPage, "Search completed successfully", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PayeeDetailsResponseDto>>> getPayeeDetailsList() {
        log.info("Fetching all payee details as list");
        List<PayeeDetailsResponseDto> payeeDetailsList = payeeDetailsService.getPayeeDetailsList();
        log.info("Retrieved {} payee details", payeeDetailsList.size());
        return ResponseBuilder.success(payeeDetailsList, "Payee details list retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PayeeDetailsResponseDto>> getPayeeDetailsById(@PathVariable Long id) {
        log.info("Fetching payee details with ID: {}", id);
        PayeeDetailsResponseDto payeeDetails = payeeDetailsService.getPayeeDetailsById(id);
        log.info("Retrieved payee details: {}", payeeDetails.getPayeeName());
        return ResponseBuilder.success(payeeDetails, "Payee details retrieved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PayeeDetailsResponseDto>> updatePayeeDetails(
            @PathVariable Long id,
            @Valid @RequestBody PayeeDetailsRequestDto requestDto) {
        log.info("Updating payee details with ID: {}", id);
        PayeeDetailsResponseDto updatedPayeeDetails = payeeDetailsService.updatePayeeDetails(id, requestDto);
        log.info("Successfully updated payee details: {}", updatedPayeeDetails.getPayeeName());
        return ResponseBuilder.success(updatedPayeeDetails, "Payee details updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePayeeDetails(@PathVariable Long id) {
        log.info("Deleting payee details with ID: {}", id);
        payeeDetailsService.deletePayeeDetails(id);
        log.info("Successfully deleted payee details with ID: {}", id);
        return ResponseBuilder.success(null, "Payee details deleted successfully", HttpStatus.OK);
    }
}
