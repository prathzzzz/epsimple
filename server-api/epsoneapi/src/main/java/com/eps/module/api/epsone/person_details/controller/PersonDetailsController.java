package com.eps.module.api.epsone.person_details.controller;

import com.eps.module.api.epsone.person_details.dto.PersonDetailsRequestDto;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsResponseDto;
import com.eps.module.api.epsone.person_details.service.PersonDetailsService;
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

@Slf4j
@RestController
@RequestMapping("/api/person-details")
@RequiredArgsConstructor
public class PersonDetailsController {

    private final PersonDetailsService personDetailsService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    @RequirePermission("PERSON_DETAILS:CREATE")
    public ResponseEntity<ApiResponse<PersonDetailsResponseDto>> createPersonDetails(@Valid @RequestBody PersonDetailsRequestDto requestDto) {
        log.info("POST /api/person-details - Creating new person details");
        PersonDetailsResponseDto response = personDetailsService.createPersonDetails(requestDto);
        return ResponseBuilder.success(response, "Person details created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PersonDetailsResponseDto>>> getAllPersonDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/person-details - Fetching all person details with pagination");
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.getAllPersonDetails(pageable);
        return ResponseBuilder.success(personDetails, "Person details retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PersonDetailsResponseDto>>> searchPersonDetails(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/person-details/search - Searching person details with term: {}", searchTerm);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.searchPersonDetails(searchTerm, pageable);
        return ResponseBuilder.success(personDetails, "Person details search completed successfully");
    }

    @GetMapping("/person-type/{personTypeId}")
    public ResponseEntity<ApiResponse<Page<PersonDetailsResponseDto>>> getPersonDetailsByPersonType(
            @PathVariable Long personTypeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/person-details/person-type/{} - Fetching person details by person type", personTypeId);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.getPersonDetailsByPersonType(personTypeId, pageable);
        return ResponseBuilder.success(personDetails, "Person details retrieved successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PersonDetailsResponseDto>>> getPersonDetailsList() {
        log.info("GET /api/person-details/list - Fetching all person details as list");
        List<PersonDetailsResponseDto> personDetails = personDetailsService.getPersonDetailsList();
        return ResponseBuilder.success(personDetails, "Person details list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonDetailsResponseDto>> getPersonDetailsById(@PathVariable Long id) {
        log.info("GET /api/person-details/{} - Fetching person details by ID", id);
        PersonDetailsResponseDto personDetails = personDetailsService.getPersonDetailsById(id);
        return ResponseBuilder.success(personDetails, "Person details retrieved successfully");
    }

    @PutMapping("/{id}")
    @RequirePermission("PERSON_DETAILS:UPDATE")
    public ResponseEntity<ApiResponse<PersonDetailsResponseDto>> updatePersonDetails(
            @PathVariable Long id,
            @Valid @RequestBody PersonDetailsRequestDto requestDto) {
        log.info("PUT /api/person-details/{} - Updating person details", id);
        PersonDetailsResponseDto updatedPersonDetails = personDetailsService.updatePersonDetails(id, requestDto);
        return ResponseBuilder.success(updatedPersonDetails, "Person details updated successfully");
    }

    @DeleteMapping("/{id}")
    @RequirePermission("PERSON_DETAILS:DELETE")
    public ResponseEntity<ApiResponse<Void>> deletePersonDetails(@PathVariable Long id) {
        log.info("DELETE /api/person-details/{} - Deleting person details", id);
        personDetailsService.deletePersonDetails(id);
        return ResponseBuilder.success(null, "Person details deleted successfully");
    }

    // ========== Bulk Upload Endpoints ==========

    @PostMapping("/bulk-upload")
    @RequirePermission("PERSON_DETAILS:BULK_UPLOAD")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/person-details/bulk-upload - Starting bulk upload");
        return bulkUploadControllerHelper.bulkUpload(file, personDetailsService);
    }

    @GetMapping("/download-template")
    @RequirePermission("PERSON_DETAILS:EXPORT")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        log.info("GET /api/person-details/download-template - Downloading bulk upload template");
        return bulkUploadControllerHelper.downloadTemplate(personDetailsService);
    }

    @GetMapping("/export")
    @RequirePermission("PERSON_DETAILS:EXPORT")
    public ResponseEntity<byte[]> exportData() throws IOException {
        log.info("GET /api/person-details/export - Exporting all person details");
        return bulkUploadControllerHelper.export(personDetailsService);
    }

    @PostMapping("/export-errors")
    @RequirePermission("PERSON_DETAILS:EXPORT")
    public ResponseEntity<byte[]> exportErrors(
            @RequestBody BulkUploadProgressDto progressData) throws IOException {
        log.info("POST /api/person-details/export-errors - Exporting bulk upload error report");
        return bulkUploadControllerHelper.exportErrors(progressData, personDetailsService);
    }
}
