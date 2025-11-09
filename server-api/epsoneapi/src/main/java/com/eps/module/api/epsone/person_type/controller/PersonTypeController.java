package com.eps.module.api.epsone.person_type.controller;

import com.eps.module.api.epsone.person_type.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.person_type.dto.PersonTypeResponseDto;
import com.eps.module.api.epsone.person_type.service.PersonTypeService;
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
@RequestMapping("/api/person-types")
@RequiredArgsConstructor
public class PersonTypeController {

    private final PersonTypeService personTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<PersonTypeResponseDto>> createPersonType(
            @Valid @RequestBody PersonTypeRequestDto requestDto) {
        log.info("POST /api/person-types - Creating new person type");
        PersonTypeResponseDto createdPersonType = personTypeService.createPersonType(requestDto);
        return ResponseBuilder.success(createdPersonType, "Person type created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PersonTypeResponseDto>>> getAllPersonTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/person-types - Fetching all person types with pagination");
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonTypeResponseDto> personTypes = personTypeService.getAllPersonTypes(pageable);
        
        return ResponseBuilder.success(personTypes, "Person types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PersonTypeResponseDto>>> searchPersonTypes(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/person-types/search - Searching person types with keyword: {}", query);
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonTypeResponseDto> personTypes = personTypeService.searchPersonTypes(query, pageable);
        
        return ResponseBuilder.success(personTypes, "Person types search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PersonTypeResponseDto>>> getAllPersonTypesList() {
        log.info("GET /api/person-types/list - Fetching all person types as list");
        List<PersonTypeResponseDto> personTypes = personTypeService.getAllPersonTypesList();
        return ResponseBuilder.success(personTypes, "Person types list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonTypeResponseDto>> getPersonTypeById(@PathVariable Long id) {
        log.info("GET /api/person-types/{} - Fetching person type by ID", id);
        PersonTypeResponseDto personType = personTypeService.getPersonTypeById(id);
        return ResponseBuilder.success(personType, "Person type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonTypeResponseDto>> updatePersonType(
            @PathVariable Long id,
            @Valid @RequestBody PersonTypeRequestDto requestDto) {
        log.info("PUT /api/person-types/{} - Updating person type", id);
        PersonTypeResponseDto updatedPersonType = personTypeService.updatePersonType(id, requestDto);
        return ResponseBuilder.success(updatedPersonType, "Person type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePersonType(@PathVariable Long id) {
        log.info("DELETE /api/person-types/{} - Deleting person type", id);
        personTypeService.deletePersonType(id);
        return ResponseBuilder.success(null, "Person type deleted successfully");
    }

    // ========== Bulk Upload Endpoints ==========

    @PostMapping("/bulk-upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/person-types/bulk-upload - Starting bulk upload");
        return personTypeService.bulkUpload(file);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        log.info("GET /api/person-types/export - Exporting all person types to Excel");
        return personTypeService.exportToExcel();
    }

    @GetMapping("/download-template")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        log.info("GET /api/person-types/download-template - Downloading bulk upload template");
        return personTypeService.downloadTemplate();
    }

    @PostMapping("/export-errors")
    public ResponseEntity<byte[]> exportErrorReport(@RequestBody BulkUploadProgressDto progressData) throws IOException {
        log.info("POST /api/person-types/export-errors - Exporting error report");
        return personTypeService.exportErrorReport(progressData);
    }
}
