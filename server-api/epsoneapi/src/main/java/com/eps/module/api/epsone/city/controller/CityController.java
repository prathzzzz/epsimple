package com.eps.module.api.epsone.city.controller;

import com.eps.module.api.epsone.city.dto.CityRequestDto;
import com.eps.module.api.epsone.city.dto.CityResponseDto;
import com.eps.module.api.epsone.city.service.CityService;
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
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<CityResponseDto>> createCity(@Valid @RequestBody CityRequestDto requestDto) {
        log.info("POST /api/cities - Creating new city");
        CityResponseDto response = cityService.createCity(requestDto);
        return ResponseBuilder.success(response, "City created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CityResponseDto>>> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/cities - Fetching all cities with pagination");
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<CityResponseDto> cities = cityService.getAllCities(pageable);
        return ResponseBuilder.success(cities, "Cities retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CityResponseDto>>> searchCities(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/cities/search - Searching cities with term: {}", searchTerm);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<CityResponseDto> cities = cityService.searchCities(searchTerm, pageable);
        return ResponseBuilder.success(cities, "Cities search completed successfully");
    }

    @GetMapping("/state/{stateId}")
    public ResponseEntity<ApiResponse<Page<CityResponseDto>>> getCitiesByState(
            @PathVariable Long stateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/cities/state/{} - Fetching cities by state", stateId);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<CityResponseDto> cities = cityService.getCitiesByState(stateId, pageable);
        return ResponseBuilder.success(cities, "Cities retrieved successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<CityResponseDto>>> getCityList() {
        log.info("GET /api/cities/list - Fetching all cities as list");
        List<CityResponseDto> cities = cityService.getCityList();
        return ResponseBuilder.success(cities, "Cities list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CityResponseDto>> getCityById(@PathVariable Long id) {
        log.info("GET /api/cities/{} - Fetching city by ID", id);
        CityResponseDto city = cityService.getCityById(id);
        return ResponseBuilder.success(city, "City retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CityResponseDto>> updateCity(
            @PathVariable Long id,
            @Valid @RequestBody CityRequestDto requestDto) {
        log.info("PUT /api/cities/{} - Updating city", id);
        CityResponseDto updatedCity = cityService.updateCity(id, requestDto);
        return ResponseBuilder.success(updatedCity, "City updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCity(@PathVariable Long id) {
        log.info("DELETE /api/cities/{} - Deleting city", id);
        cityService.deleteCity(id);
        return ResponseBuilder.success(null, "City deleted successfully");
    }

    // Bulk upload endpoints
    @PostMapping("/bulk-upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/cities/bulk-upload - Starting bulk upload");
        return bulkUploadHelper.bulkUpload(file, cityService);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        log.info("GET /api/cities/export - Exporting cities to Excel");
        return bulkUploadHelper.export(cityService);
    }

    @GetMapping("/download-template")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        log.info("GET /api/cities/download-template - Downloading bulk upload template");
        return bulkUploadHelper.downloadTemplate(cityService);
    }

    @PostMapping("/export-errors")
    public ResponseEntity<byte[]> exportErrors(@RequestBody BulkUploadProgressDto progress) throws IOException {
        log.info("POST /api/cities/export-errors - Exporting error report");
        return bulkUploadHelper.exportErrors(progress, cityService);
    }
}

