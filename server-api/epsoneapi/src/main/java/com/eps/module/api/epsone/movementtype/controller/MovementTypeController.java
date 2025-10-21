package com.eps.module.api.epsone.movementtype.controller;

import com.eps.module.api.epsone.movementtype.dto.MovementTypeRequestDto;
import com.eps.module.api.epsone.movementtype.dto.MovementTypeResponseDto;
import com.eps.module.api.epsone.movementtype.service.MovementTypeService;
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
@RequestMapping("/api/movement-types")
@RequiredArgsConstructor
public class MovementTypeController {

    private final MovementTypeService movementTypeService;

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
}
