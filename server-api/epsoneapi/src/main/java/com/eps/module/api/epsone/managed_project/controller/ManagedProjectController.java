package com.eps.module.api.epsone.managed_project.controller;

import com.eps.module.api.epsone.managed_project.dto.ManagedProjectRequestDto;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectResponseDto;
import com.eps.module.api.epsone.managed_project.service.ManagedProjectService;
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
@RequestMapping("/api/managed-projects")
@RequiredArgsConstructor
public class ManagedProjectController {

    private final ManagedProjectService managedProjectService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<ManagedProjectResponseDto>> createManagedProject(
            @Valid @RequestBody ManagedProjectRequestDto requestDto) {
        log.info("POST /api/managed-projects - Creating new managed project");
        ManagedProjectResponseDto responseDto = managedProjectService.createManagedProject(requestDto);
        return ResponseBuilder.success(responseDto, "Managed project created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ManagedProjectResponseDto>>> getAllManagedProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/managed-projects - Fetching all managed projects with pagination");
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ManagedProjectResponseDto> managedProjects = managedProjectService.getAllManagedProjects(pageable);
        return ResponseBuilder.success(managedProjects, "Managed projects retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ManagedProjectResponseDto>>> searchManagedProjects(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "projectName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/managed-projects/search - Searching managed projects with term: {}", searchTerm);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ManagedProjectResponseDto> managedProjects = managedProjectService.searchManagedProjects(searchTerm, pageable);
        return ResponseBuilder.success(managedProjects, "Managed projects search completed successfully");
    }

    @GetMapping("/bank/{bankId}")
    public ResponseEntity<ApiResponse<Page<ManagedProjectResponseDto>>> getManagedProjectsByBank(
            @PathVariable Long bankId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "projectName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/managed-projects/bank/{} - Fetching managed projects by bank", bankId);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ManagedProjectResponseDto> managedProjects = managedProjectService.getManagedProjectsByBank(bankId, pageable);
        return ResponseBuilder.success(managedProjects, "Managed projects retrieved successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ManagedProjectResponseDto>>> getAllManagedProjectsList() {
        log.info("GET /api/managed-projects/list - Fetching all managed projects as list");
        List<ManagedProjectResponseDto> managedProjects = managedProjectService.getAllManagedProjectsList();
        return ResponseBuilder.success(managedProjects, "Managed projects list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws IOException {
        log.info("GET /api/managed-projects/export - Exporting all managed projects");
        return bulkUploadHelper.export(managedProjectService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ManagedProjectResponseDto>> getManagedProjectById(@PathVariable Long id) {
        log.info("GET /api/managed-projects/{} - Fetching managed project by ID", id);
        ManagedProjectResponseDto managedProject = managedProjectService.getManagedProjectById(id);
        return ResponseBuilder.success(managedProject, "Managed project retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ManagedProjectResponseDto>> updateManagedProject(
            @PathVariable Long id,
            @Valid @RequestBody ManagedProjectRequestDto requestDto) {
        log.info("PUT /api/managed-projects/{} - Updating managed project", id);
        ManagedProjectResponseDto responseDto = managedProjectService.updateManagedProject(id, requestDto);
        return ResponseBuilder.success(responseDto, "Managed project updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteManagedProject(@PathVariable Long id) {
        log.info("DELETE /api/managed-projects/{} - Deleting managed project", id);
        managedProjectService.deleteManagedProject(id);
        return ResponseBuilder.success(null, "Managed project deleted successfully");
    }

    // ========== Bulk Upload Endpoints ==========

    @PostMapping("/bulk/upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/managed-projects/bulk/upload - Starting bulk upload with file: {}", file.getOriginalFilename());
        return bulkUploadHelper.bulkUpload(file, managedProjectService);
    }

    @GetMapping("/bulk/export-template")
    public ResponseEntity<byte[]> exportTemplate() throws IOException {
        log.info("GET /api/managed-projects/bulk/export-template - Exporting template");
        return bulkUploadHelper.downloadTemplate(managedProjectService);
    }

    @PostMapping("/bulk/export-error-report")
    public ResponseEntity<byte[]> exportErrorReport(@RequestBody BulkUploadProgressDto progressData) throws IOException {
        log.info("POST /api/managed-projects/bulk/export-error-report - Exporting error report");
        return bulkUploadHelper.exportErrors(progressData, managedProjectService);
    }
}
