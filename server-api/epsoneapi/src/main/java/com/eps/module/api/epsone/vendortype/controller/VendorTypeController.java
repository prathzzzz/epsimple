package com.eps.module.api.epsone.vendortype.controller;

import com.eps.module.api.epsone.vendortype.dto.VendorTypeRequestDto;
import com.eps.module.api.epsone.vendortype.dto.VendorTypeResponseDto;
import com.eps.module.api.epsone.vendortype.service.VendorTypeService;
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
@RequestMapping("/api/vendor-types")
@RequiredArgsConstructor
public class VendorTypeController {

    private final VendorTypeService vendorTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<VendorTypeResponseDto>> createVendorType(
            @Valid @RequestBody VendorTypeRequestDto requestDto) {
        log.info("POST /api/vendor-types - Creating new vendor type");
        VendorTypeResponseDto createdVendorType = vendorTypeService.createVendorType(requestDto);
        return ResponseBuilder.success(createdVendorType, "Vendor type created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<VendorTypeResponseDto>>> getAllVendorTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/vendor-types - Fetching all vendor types with pagination");
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<VendorTypeResponseDto> vendorTypes = vendorTypeService.getAllVendorTypes(pageable);
        
        return ResponseBuilder.success(vendorTypes, "Vendor types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<VendorTypeResponseDto>>> searchVendorTypes(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/vendor-types/search - Searching vendor types with keyword: {}", query);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<VendorTypeResponseDto> vendorTypes = vendorTypeService.searchVendorTypes(query, pageable);
        
        return ResponseBuilder.success(vendorTypes, "Vendor types search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<VendorTypeResponseDto>>> getAllVendorTypesList() {
        log.info("GET /api/vendor-types/list - Fetching all vendor types as list");
        List<VendorTypeResponseDto> vendorTypes = vendorTypeService.getAllVendorTypesList();
        return ResponseBuilder.success(vendorTypes, "Vendor types list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorTypeResponseDto>> getVendorTypeById(@PathVariable Long id) {
        log.info("GET /api/vendor-types/{} - Fetching vendor type by ID", id);
        VendorTypeResponseDto vendorType = vendorTypeService.getVendorTypeById(id);
        return ResponseBuilder.success(vendorType, "Vendor type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorTypeResponseDto>> updateVendorType(
            @PathVariable Long id,
            @Valid @RequestBody VendorTypeRequestDto requestDto) {
        log.info("PUT /api/vendor-types/{} - Updating vendor type", id);
        VendorTypeResponseDto updatedVendorType = vendorTypeService.updateVendorType(id, requestDto);
        return ResponseBuilder.success(updatedVendorType, "Vendor type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVendorType(@PathVariable Long id) {
        log.info("DELETE /api/vendor-types/{} - Deleting vendor type", id);
        vendorTypeService.deleteVendorType(id);
        return ResponseBuilder.success(null, "Vendor type deleted successfully");
    }
}
