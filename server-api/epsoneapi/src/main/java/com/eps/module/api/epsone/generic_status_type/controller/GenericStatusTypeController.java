package com.eps.module.api.epsone.generic_status_type.controller;

import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeRequestDto;
import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeResponseDto;
import com.eps.module.api.epsone.generic_status_type.service.GenericStatusTypeService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generic-status-types")
@RequiredArgsConstructor
public class GenericStatusTypeController {

    private final GenericStatusTypeService genericStatusTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<GenericStatusTypeResponseDto>> createGenericStatusType(
            @Valid @RequestBody GenericStatusTypeRequestDto genericStatusTypeRequestDto) {
        GenericStatusTypeResponseDto response = genericStatusTypeService.createGenericStatusType(genericStatusTypeRequestDto);
        return ResponseBuilder.success(response, "Generic status type created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GenericStatusTypeResponseDto>>> getAllGenericStatusTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<GenericStatusTypeResponseDto> genericStatusTypes = genericStatusTypeService.getAllGenericStatusTypes(pageable);

        return ResponseBuilder.success(genericStatusTypes, "Generic status types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<GenericStatusTypeResponseDto>>> searchGenericStatusTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<GenericStatusTypeResponseDto> genericStatusTypes = genericStatusTypeService.searchGenericStatusTypes(searchTerm, pageable);

        return ResponseBuilder.success(genericStatusTypes, "Generic status types search completed");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<GenericStatusTypeResponseDto>>> getGenericStatusTypeList() {
        List<GenericStatusTypeResponseDto> genericStatusTypes = genericStatusTypeService.getGenericStatusTypeList();
        return ResponseBuilder.success(genericStatusTypes, "Generic status type list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GenericStatusTypeResponseDto>> getGenericStatusTypeById(@PathVariable Long id) {
        GenericStatusTypeResponseDto genericStatusType = genericStatusTypeService.getGenericStatusTypeById(id);
        return ResponseBuilder.success(genericStatusType, "Generic status type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GenericStatusTypeResponseDto>> updateGenericStatusType(
            @PathVariable Long id,
            @Valid @RequestBody GenericStatusTypeRequestDto genericStatusTypeRequestDto) {
        GenericStatusTypeResponseDto response = genericStatusTypeService.updateGenericStatusType(id, genericStatusTypeRequestDto);
        return ResponseBuilder.success(response, "Generic status type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGenericStatusType(@PathVariable Long id) {
        genericStatusTypeService.deleteGenericStatusType(id);
        return ResponseBuilder.success(null, "Generic status type deleted successfully");
    }
}
