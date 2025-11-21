package com.eps.module.api.epsone.state.controller;

import com.eps.module.api.epsone.state.dto.StateRequestDto;
import com.eps.module.api.epsone.state.dto.StateResponseDto;
import com.eps.module.api.epsone.state.service.StateService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    @RequireAdmin
    public ResponseEntity<ApiResponse<StateResponseDto>> createState(
            @Valid @RequestBody StateRequestDto stateRequestDto) {
        log.info("POST /api/states - Creating new state");
        StateResponseDto state = stateService.createState(stateRequestDto);
        return ResponseBuilder.success(state, "State created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StateResponseDto>> getStateById(@PathVariable Long id) {
        log.info("GET /api/states/{} - Fetching state by ID", id);
        StateResponseDto state = stateService.getStateById(id);
        return ResponseBuilder.success(state, "State retrieved successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StateResponseDto>>> getAllStates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/states - Fetching all states with pagination");
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<StateResponseDto> states = stateService.getAllStates(pageable);
        
        return ResponseBuilder.success(states, "States retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<StateResponseDto>>> searchStates(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/states/search - Searching states with keyword: {}", search);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<StateResponseDto> states = stateService.searchStates(search, pageable);
        
        return ResponseBuilder.success(states, "States search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<StateResponseDto>>> getAllStatesList() {
        log.info("GET /api/states/list - Fetching all states as list");
        List<StateResponseDto> states = stateService.getAllStatesList();
        return ResponseBuilder.success(states, "States list retrieved successfully");
    }

    @PutMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<StateResponseDto>> updateState(
            @PathVariable Long id,
            @Valid @RequestBody StateRequestDto stateRequestDto) {
        log.info("PUT /api/states/{} - Updating state", id);
        StateResponseDto state = stateService.updateState(id, stateRequestDto);
        return ResponseBuilder.success(state, "State updated successfully");
    }

    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<Void>> deleteState(@PathVariable Long id) {
        log.info("DELETE /api/states/{} - Deleting state", id);
        stateService.deleteState(id);
        return ResponseBuilder.success(null, "State deleted successfully");
    }
    
    // Bulk Operations
    
    @PostMapping(value = "/bulk-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequireAdmin
    public SseEmitter bulkUploadStates(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/states/bulk-upload - Starting bulk upload");
        return bulkUploadHelper.bulkUpload(file, stateService);
    }
    
    @GetMapping("/export")
    @RequireAdmin
    public ResponseEntity<byte[]> exportStates() throws IOException {
        log.info("GET /api/states/export - Exporting all states to Excel");
        return bulkUploadHelper.export(stateService);
    }
    
    @GetMapping("/download-template")
    @RequireAdmin
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        log.info("GET /api/states/download-template - Downloading bulk upload template");
        return bulkUploadHelper.downloadTemplate(stateService);
    }
    
    @PostMapping("/export-errors")
    @RequireAdmin
    public ResponseEntity<byte[]> exportBulkUploadErrors(
            @RequestBody com.eps.module.common.bulk.dto.BulkUploadProgressDto progressData) throws IOException {
        log.info("POST /api/states/export-errors - Exporting bulk upload errors");
        return bulkUploadHelper.exportErrors(progressData, stateService);
    }
}
