package com.eps.module.api.epsone.movement_type.controller;

import com.eps.module.api.epsone.movement_type.dto.MovementTypeRequestDto;
import com.eps.module.api.epsone.movement_type.dto.MovementTypeResponseDto;
import com.eps.module.api.epsone.movement_type.service.MovementTypeService;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/movement-types")
@RequiredArgsConstructor
public class MovementTypeController {

    private final MovementTypeService movementTypeService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    @PostMapping
    public ResponseEntity<ApiResponse<MovementTypeResponseDto>> createMovementType(
            @Valid @RequestBody MovementTypeRequestDto requestDto) {
        MovementTypeResponseDto response = movementTypeService.createMovementType(requestDto);
        return ResponseBuilder.success(response, "Movement type created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MovementTypeResponseDto>>> getAllMovementTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<MovementTypeResponseDto> response = movementTypeService.getAllMovementTypes(pageable);
        return ResponseBuilder.success(response, "Movement types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<MovementTypeResponseDto>>> searchMovementTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "movementType") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<MovementTypeResponseDto> response = movementTypeService.searchMovementTypes(searchTerm, pageable);
        return ResponseBuilder.success(response, "Movement types search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MovementTypeResponseDto>>> getAllMovementTypesList() {
        List<MovementTypeResponseDto> response = movementTypeService.getAllMovementTypesList();
        return ResponseBuilder.success(response, "Movement types list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws Exception {
        return bulkUploadControllerHelper.export(movementTypeService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovementTypeResponseDto>> getMovementTypeById(@PathVariable Long id) {
        MovementTypeResponseDto response = movementTypeService.getMovementTypeById(id);
        return ResponseBuilder.success(response, "Movement type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MovementTypeResponseDto>> updateMovementType(
            @PathVariable Long id,
            @Valid @RequestBody MovementTypeRequestDto requestDto) {
        MovementTypeResponseDto response = movementTypeService.updateMovementType(id, requestDto);
        return ResponseBuilder.success(response, "Movement type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMovementType(@PathVariable Long id) {
        movementTypeService.deleteMovementType(id);
        return ResponseBuilder.success(null, "Movement type deleted successfully");
    }

    // ========== Bulk Upload Endpoints ==========

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter bulkUploadMovementTypes(@RequestParam("file") MultipartFile file) throws Exception {
        return bulkUploadControllerHelper.bulkUpload(file, movementTypeService);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        return bulkUploadControllerHelper.downloadTemplate(movementTypeService);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> downloadErrorReport(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        return bulkUploadControllerHelper.exportErrors(progressData, movementTypeService);
    }
}
