package com.eps.module.api.epsone.location.controller;

import com.eps.module.api.epsone.location.dto.LocationRequestDto;
import com.eps.module.api.epsone.location.dto.LocationResponseDto;
import com.eps.module.api.epsone.location.service.LocationService;
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

@Slf4j
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<LocationResponseDto>> createLocation(
            @Valid @RequestBody LocationRequestDto locationRequestDto) {
        log.info("POST /api/locations - Creating location: {}", locationRequestDto.getLocationName());
        LocationResponseDto response = locationService.createLocation(locationRequestDto);
        return ResponseBuilder.success(response, "Location created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LocationResponseDto>>> getAllLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("GET /api/locations - Fetching all locations: page={}, size={}, sortBy={}, sortDirection={}",
                page, size, sortBy, sortDirection);
        
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<LocationResponseDto> response = locationService.getAllLocations(pageable);
        return ResponseBuilder.success(response, "Locations fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<LocationResponseDto>>> searchLocations(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "locationName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("GET /api/locations/search - Searching locations with term: {}", searchTerm);
        
        Sort sort = sortDirection.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<LocationResponseDto> response = locationService.searchLocations(searchTerm, pageable);
        return ResponseBuilder.success(response, "Locations search completed successfully", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<LocationResponseDto>>> getAllLocationsList() {
        log.info("GET /api/locations/list - Fetching all locations as list");
        List<LocationResponseDto> response = locationService.getAllLocationsList();
        return ResponseBuilder.success(response, "Locations list fetched successfully", HttpStatus.OK);
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws IOException {
        log.info("GET /api/locations/export - Exporting all locations");
        return bulkUploadHelper.export(locationService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponseDto>> getLocationById(@PathVariable Long id) {
        log.info("GET /api/locations/{} - Fetching location by ID", id);
        LocationResponseDto response = locationService.getLocationById(id);
        return ResponseBuilder.success(response, "Location fetched successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponseDto>> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequestDto locationRequestDto) {
        log.info("PUT /api/locations/{} - Updating location", id);
        LocationResponseDto response = locationService.updateLocation(id, locationRequestDto);
        return ResponseBuilder.success(response, "Location updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLocation(@PathVariable Long id) {
        log.info("DELETE /api/locations/{} - Deleting location", id);
        locationService.deleteLocation(id);
        return ResponseBuilder.success(null, "Location deleted successfully", HttpStatus.OK);
    }

    // ========== Bulk Upload Endpoints ==========

    @PostMapping("/bulk/upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/locations/bulk/upload - Starting bulk upload with file: {}", file.getOriginalFilename());
        return bulkUploadHelper.bulkUpload(file, locationService);
    }

    @GetMapping("/bulk/export-template")
    public ResponseEntity<byte[]> exportTemplate() throws IOException {
        log.info("GET /api/locations/bulk/export-template - Exporting template");
        return bulkUploadHelper.downloadTemplate(locationService);
    }

    @PostMapping("/bulk/export-error-report")
    public ResponseEntity<byte[]> exportErrorReport(@RequestBody BulkUploadProgressDto progressData) throws IOException {
        log.info("POST /api/locations/bulk/export-error-report - Exporting error report");
        return bulkUploadHelper.exportErrors(progressData, locationService);
    }
}
