package com.eps.module.api.epsone.expendituresvoucher.controller;

import com.eps.module.api.epsone.expendituresvoucher.dto.ExpendituresVoucherRequestDto;
import com.eps.module.api.epsone.expendituresvoucher.dto.ExpendituresVoucherResponseDto;
import com.eps.module.api.epsone.expendituresvoucher.service.ExpendituresVoucherService;
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
@RequestMapping("/api/expenditures/vouchers")
@RequiredArgsConstructor
public class ExpendituresVoucherController {

    private final ExpendituresVoucherService expendituresVoucherService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExpendituresVoucherResponseDto>> createExpendituresVoucher(
            @Valid @RequestBody ExpendituresVoucherRequestDto requestDto) {
        log.info("POST /api/expenditures/vouchers - Creating expenditures voucher");
        ExpendituresVoucherResponseDto responseDto = expendituresVoucherService.createExpendituresVoucher(requestDto);
        return ResponseBuilder.success(responseDto, "Expenditures voucher created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExpendituresVoucherResponseDto>>> getAllExpendituresVouchers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("GET /api/expenditures/vouchers - Fetching all expenditures vouchers");

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ExpendituresVoucherResponseDto> responsePage = expendituresVoucherService.getAllExpendituresVouchers(pageable);
        return ResponseBuilder.success(responsePage, "Expenditures vouchers fetched successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ExpendituresVoucherResponseDto>>> searchExpendituresVouchers(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("GET /api/expenditures/vouchers/search - Searching expenditures vouchers with term: {}", searchTerm);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ExpendituresVoucherResponseDto> responsePage = expendituresVoucherService.searchExpendituresVouchers(searchTerm, pageable);
        return ResponseBuilder.success(responsePage, "Expenditures vouchers searched successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ExpendituresVoucherResponseDto>>> getAllExpendituresVouchersList() {
        log.info("GET /api/expenditures/vouchers/list - Fetching all expenditures vouchers as list");
        List<ExpendituresVoucherResponseDto> responseList = expendituresVoucherService.getAllExpendituresVouchersList();
        return ResponseBuilder.success(responseList, "Expenditures vouchers list fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpendituresVoucherResponseDto>> getExpendituresVoucherById(@PathVariable Long id) {
        log.info("GET /api/expenditures/vouchers/{} - Fetching expenditures voucher", id);
        ExpendituresVoucherResponseDto responseDto = expendituresVoucherService.getExpendituresVoucherById(id);
        return ResponseBuilder.success(responseDto, "Expenditures voucher fetched successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpendituresVoucherResponseDto>> updateExpendituresVoucher(
            @PathVariable Long id,
            @Valid @RequestBody ExpendituresVoucherRequestDto requestDto) {
        log.info("PUT /api/expenditures/vouchers/{} - Updating expenditures voucher", id);
        ExpendituresVoucherResponseDto responseDto = expendituresVoucherService.updateExpendituresVoucher(id, requestDto);
        return ResponseBuilder.success(responseDto, "Expenditures voucher updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpendituresVoucher(@PathVariable Long id) {
        log.info("DELETE /api/expenditures/vouchers/{} - Deleting expenditures voucher", id);
        expendituresVoucherService.deleteExpendituresVoucher(id);
        return ResponseBuilder.success(null, "Expenditures voucher deleted successfully");
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<Page<ExpendituresVoucherResponseDto>>> getExpendituresVouchersByProjectId(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        log.info("GET /api/expenditures/vouchers/project/{} - Fetching expenditures vouchers for project", projectId);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ExpendituresVoucherResponseDto> responsePage = expendituresVoucherService.getExpendituresVouchersByProjectId(projectId, pageable);
        return ResponseBuilder.success(responsePage, "Expenditures vouchers for project fetched successfully");
    }

    @GetMapping("/voucher/{voucherId}")
    public ResponseEntity<ApiResponse<List<ExpendituresVoucherResponseDto>>> getExpendituresVouchersByVoucherId(
            @PathVariable Long voucherId) {
        log.info("GET /api/expenditures/vouchers/voucher/{} - Fetching expenditures for voucher", voucherId);
        List<ExpendituresVoucherResponseDto> responseList = expendituresVoucherService.getExpendituresVouchersByVoucherId(voucherId);
        return ResponseBuilder.success(responseList, "Expenditures for voucher fetched successfully");
    }
}
