package com.eps.module.api.epsone.landlord.controller;

import com.eps.module.api.epsone.landlord.dto.LandlordRequestDto;
import com.eps.module.api.epsone.landlord.dto.LandlordResponseDto;
import com.eps.module.api.epsone.landlord.service.LandlordService;
import com.eps.module.auth.rbac.annotation.RequirePermission;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/landlords")
@RequiredArgsConstructor
@Slf4j
public class LandlordController {

    private final LandlordService landlordService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    @RequirePermission("LANDLORD:CREATE")
    public ResponseEntity<ApiResponse<LandlordResponseDto>> createLandlord(
            @Valid @RequestBody LandlordRequestDto requestDto) {
        log.info("Creating landlord with person details ID: {}", requestDto.getLandlordDetailsId());
        LandlordResponseDto landlord = landlordService.createLandlord(requestDto);
        return ResponseBuilder.success(landlord, "Landlord created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    @RequirePermission("LANDLORD:READ")
    public ResponseEntity<ApiResponse<Page<LandlordResponseDto>>> getAllLandlords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Fetching landlords - page: {}, size: {}, sortBy: {}, sortDirection: {}", 
                page, size, sortBy, sortDirection);
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LandlordResponseDto> landlords = landlordService.getAllLandlords(pageable);
        return ResponseBuilder.success(landlords, "Landlords retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    @RequirePermission("LANDLORD:READ")
    public ResponseEntity<ApiResponse<Page<LandlordResponseDto>>> searchLandlords(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("Searching landlords with term: '{}', page: {}, size: {}", searchTerm, page, size);
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LandlordResponseDto> landlords = landlordService.searchLandlords(searchTerm, pageable);
        return ResponseBuilder.success(landlords, "Landlords search completed successfully", HttpStatus.OK);
    }

    @GetMapping("/list")
    @RequirePermission("LANDLORD:READ")
    public ResponseEntity<ApiResponse<List<LandlordResponseDto>>> getAllLandlordsList() {
        log.info("Fetching all landlords as list");
        List<LandlordResponseDto> landlords = landlordService.getAllLandlordsList();
        return ResponseBuilder.success(landlords, "Landlords list retrieved successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @RequirePermission("LANDLORD:READ")
    public ResponseEntity<ApiResponse<LandlordResponseDto>> getLandlordById(@PathVariable Long id) {
        log.info("Fetching landlord with id: {}", id);
        LandlordResponseDto landlord = landlordService.getLandlordById(id);
        return ResponseBuilder.success(landlord, "Landlord retrieved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @RequirePermission("LANDLORD:UPDATE")
    public ResponseEntity<ApiResponse<LandlordResponseDto>> updateLandlord(
            @PathVariable Long id,
            @Valid @RequestBody LandlordRequestDto requestDto) {
        log.info("Updating landlord with id: {}", id);
        LandlordResponseDto landlord = landlordService.updateLandlord(id, requestDto);
        return ResponseBuilder.success(landlord, "Landlord updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("LANDLORD:DELETE")
    public ResponseEntity<ApiResponse<Void>> deleteLandlord(@PathVariable Long id) {
        log.info("Deleting landlord with id: {}", id);
        landlordService.deleteLandlord(id);
        return ResponseBuilder.success(null, "Landlord deleted successfully", HttpStatus.OK);
    }

    // Bulk Upload Endpoints
    @PostMapping("/bulk-upload")
    @RequirePermission("LANDLORD:BULK_UPLOAD")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/landlords/bulk-upload - Starting bulk upload");
        return bulkUploadControllerHelper.bulkUpload(file, landlordService);
    }

    @GetMapping("/download-template")
    @RequirePermission("LANDLORD:EXPORT")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        log.info("GET /api/landlords/download-template - Downloading bulk upload template");
        return bulkUploadControllerHelper.downloadTemplate(landlordService);
    }

    @GetMapping("/export")
    @RequirePermission("LANDLORD:EXPORT")
    public ResponseEntity<byte[]> exportData() throws IOException {
        log.info("GET /api/landlords/export - Exporting all landlords");
        return bulkUploadControllerHelper.export(landlordService);
    }

    @PostMapping("/export-errors")
    @RequirePermission("LANDLORD:EXPORT")
    public ResponseEntity<byte[]> exportErrors(
            @RequestBody BulkUploadProgressDto progressData) throws IOException {
        log.info("POST /api/landlords/export-errors - Exporting bulk upload error report");
        return bulkUploadControllerHelper.exportErrors(progressData, landlordService);
    }
}
