package com.eps.module.api.epsone.warehouse.controller;

import com.eps.module.api.epsone.warehouse.dto.WarehouseRequestDto;
import com.eps.module.api.epsone.warehouse.dto.WarehouseResponseDto;
import com.eps.module.api.epsone.warehouse.service.WarehouseService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@RequireAdmin
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    // ========== Bulk Upload Endpoints ==========

    @PostMapping("/bulk/upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/warehouses/bulk/upload - Starting bulk upload with file: {}", file.getOriginalFilename());
        return bulkUploadHelper.bulkUpload(file, warehouseService);
    }

    @GetMapping("/bulk/export-template")
    public ResponseEntity<byte[]> exportTemplate() throws IOException {
        log.info("GET /api/warehouses/bulk/export-template - Exporting template");
        return bulkUploadHelper.downloadTemplate(warehouseService);
    }

    @PostMapping("/bulk/export-error-report")
    public ResponseEntity<byte[]> exportErrorReport(@RequestBody BulkUploadProgressDto progressData) throws IOException {
        log.info("POST /api/warehouses/bulk/export-error-report - Exporting error report");
        return bulkUploadHelper.exportErrors(progressData, warehouseService);
    }

    // ========== CRUD Endpoints ==========

    @PostMapping
    public ResponseEntity<ApiResponse<WarehouseResponseDto>> createWarehouse(
            @Valid @RequestBody WarehouseRequestDto requestDto) {
        log.info("POST /api/warehouses - Creating warehouse: {}", requestDto.getWarehouseName());
        WarehouseResponseDto response = warehouseService.createWarehouse(requestDto);
        return ResponseBuilder.success(response, "Warehouse created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<WarehouseResponseDto>>> getAllWarehouses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/warehouses - Fetching all warehouses: page={}, size={}, sortBy={}, sortOrder={}",
                page, size, sortBy, sortOrder);
        Page<WarehouseResponseDto> response = warehouseService.getAllWarehouses(page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Warehouses retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<WarehouseResponseDto>>> searchWarehouses(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/warehouses/search - Searching warehouses with term: {}", searchTerm);
        Page<WarehouseResponseDto> response = warehouseService.searchWarehouses(searchTerm, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(response, "Warehouses search completed", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<WarehouseResponseDto>>> getAllWarehousesList() {
        log.info("GET /api/warehouses/list - Fetching all warehouses as list");
        List<WarehouseResponseDto> response = warehouseService.getAllWarehousesList();
        return ResponseBuilder.success(response, "Warehouses list retrieved successfully", HttpStatus.OK);
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws IOException {
        log.info("GET /api/warehouses/export - Exporting all warehouses");
        return bulkUploadHelper.export(warehouseService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponseDto>> getWarehouseById(@PathVariable Long id) {
        log.info("GET /api/warehouses/{} - Fetching warehouse by ID", id);
        WarehouseResponseDto response = warehouseService.getWarehouseById(id);
        return ResponseBuilder.success(response, "Warehouse retrieved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponseDto>> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequestDto requestDto) {
        log.info("PUT /api/warehouses/{} - Updating warehouse", id);
        WarehouseResponseDto response = warehouseService.updateWarehouse(id, requestDto);
        return ResponseBuilder.success(response, "Warehouse updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long id) {
        log.info("DELETE /api/warehouses/{} - Deleting warehouse", id);
        warehouseService.deleteWarehouse(id);
        return ResponseBuilder.success(null, "Warehouse deleted successfully", HttpStatus.OK);
    }
}
