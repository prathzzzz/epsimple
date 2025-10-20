package com.eps.module.api.epsone.bank.controller;

import com.eps.module.api.epsone.bank.dto.BankRequestDto;
import com.eps.module.api.epsone.bank.dto.BankResponseDto;
import com.eps.module.api.epsone.bank.service.BankService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BankResponseDto>> createBank(
            @Valid @ModelAttribute BankRequestDto bankRequestDto,
            @RequestParam(value = "logo", required = false) MultipartFile logo) {
        log.info("POST /api/banks - Creating new bank");
        BankResponseDto bank = bankService.createBankWithLogo(bankRequestDto, logo);
        return ResponseBuilder.success(bank, "Bank created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankResponseDto>> getBankById(@PathVariable Long id) {
        log.info("GET /api/banks/{} - Fetching bank by ID", id);
        BankResponseDto bank = bankService.getBankById(id);
        return ResponseBuilder.success(bank, "Bank retrieved successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BankResponseDto>>> getAllBanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/banks - Fetching all banks with pagination");
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<BankResponseDto> banks = bankService.getAllBanks(pageable);
        
        return ResponseBuilder.success(banks, "Banks retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BankResponseDto>>> searchBanks(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/banks/search - Searching banks with keyword: {}", search);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<BankResponseDto> banks = bankService.searchBanks(search, pageable);
        
        return ResponseBuilder.success(banks, "Banks search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<BankResponseDto>>> getAllBanksList() {
        log.info("GET /api/banks/list - Fetching all banks as list");
        List<BankResponseDto> banks = bankService.getAllBanksList();
        return ResponseBuilder.success(banks, "Banks list retrieved successfully");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BankResponseDto>> updateBank(
            @PathVariable Long id,
            @Valid @ModelAttribute BankRequestDto bankRequestDto,
            @RequestParam(value = "logo", required = false) MultipartFile logo) {
        log.info("PUT /api/banks/{} - Updating bank", id);
        BankResponseDto bank = bankService.updateBankWithLogo(id, bankRequestDto, logo);
        return ResponseBuilder.success(bank, "Bank updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBank(@PathVariable Long id) {
        log.info("DELETE /api/banks/{} - Deleting bank", id);
        bankService.deleteBank(id);
        return ResponseBuilder.success(null, "Bank deleted successfully");
    }
}
