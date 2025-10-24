package com.eps.module.api.epsone.vendor.controller;


import com.eps.module.api.epsone.vendor.dto.VendorRequestDto;
import com.eps.module.api.epsone.vendor.dto.VendorResponseDto;
import com.eps.module.api.epsone.vendor.service.VendorService;
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

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
@Slf4j
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<ApiResponse<VendorResponseDto>> createVendor(
            @Valid @RequestBody VendorRequestDto requestDto) {
        log.info("Creating vendor with type ID: {}, person details ID: {}", 
                requestDto.getVendorTypeId(), requestDto.getVendorDetailsId());
        VendorResponseDto vendor = vendorService.createVendor(requestDto);
        return ResponseBuilder.success(vendor, "Vendor created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<VendorResponseDto>>> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Fetching vendors - page: {}, size: {}, sortBy: {}, sortDirection: {}", 
                page, size, sortBy, sortDirection);
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<VendorResponseDto> vendors = vendorService.getAllVendors(pageable);
        return ResponseBuilder.success(vendors, "Vendors retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<VendorResponseDto>>> searchVendors(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Searching vendors with term: '{}', page: {}, size: {}", searchTerm, page, size);
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<VendorResponseDto> vendors = vendorService.searchVendors(searchTerm, pageable);
        return ResponseBuilder.success(vendors, "Vendors search completed successfully", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<VendorResponseDto>>> getAllVendorsList() {
        log.info("Fetching all vendors as list");
        List<VendorResponseDto> vendors = vendorService.getAllVendorsList();
        return ResponseBuilder.success(vendors, "Vendors list retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/type/{vendorTypeId}")
    public ResponseEntity<ApiResponse<Page<VendorResponseDto>>> getVendorsByType(
            @PathVariable Long vendorTypeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Fetching vendors by type ID: {}", vendorTypeId);
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<VendorResponseDto> vendors = vendorService.getVendorsByType(vendorTypeId, pageable);
        return ResponseBuilder.success(vendors, "Vendors by type retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponseDto>> getVendorById(@PathVariable Long id) {
        log.info("Fetching vendor with id: {}", id);
        VendorResponseDto vendor = vendorService.getVendorById(id);
        return ResponseBuilder.success(vendor, "Vendor retrieved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponseDto>> updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody VendorRequestDto requestDto) {
        log.info("Updating vendor with id: {}", id);
        VendorResponseDto vendor = vendorService.updateVendor(id, requestDto);
        return ResponseBuilder.success(vendor, "Vendor updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVendor(@PathVariable Long id) {
        log.info("Deleting vendor with id: {}", id);
        vendorService.deleteVendor(id);
        return ResponseBuilder.success(null, "Vendor deleted successfully", HttpStatus.OK);
    }
}
