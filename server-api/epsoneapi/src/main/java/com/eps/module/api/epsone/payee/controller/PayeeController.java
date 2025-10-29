package com.eps.module.api.epsone.payee.controller;

import com.eps.module.api.epsone.payee.dto.PayeeRequestDto;
import com.eps.module.api.epsone.payee.dto.PayeeResponseDto;
import com.eps.module.api.epsone.payee.service.PayeeService;
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

@RestController
@RequestMapping("/api/payees")
@RequiredArgsConstructor
@Slf4j
public class PayeeController {

    private final PayeeService payeeService;

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
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
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
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PayeeResponseDto> response = payeeService.searchPayees(searchTerm, pageable);
        return ResponseBuilder.success(response, "Payees search completed successfully", HttpStatus.OK);
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
}
