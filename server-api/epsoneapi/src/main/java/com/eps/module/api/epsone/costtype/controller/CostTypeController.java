package com.eps.module.api.epsone.costtype.controller;

import com.eps.module.api.epsone.costtype.dto.CostTypeRequestDto;
import com.eps.module.api.epsone.costtype.dto.CostTypeResponseDto;
import com.eps.module.api.epsone.costtype.service.CostTypeService;
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
@RequestMapping("/api/cost-types")
@RequiredArgsConstructor
public class CostTypeController {

    private final CostTypeService costTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<CostTypeResponseDto>> createCostType(
            @Valid @RequestBody CostTypeRequestDto requestDto) {
        CostTypeResponseDto response = costTypeService.createCostType(requestDto);
        return ResponseBuilder.success(response, "Cost type created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CostTypeResponseDto>>> getAllCostTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<CostTypeResponseDto> response = costTypeService.getAllCostTypes(pageable);
        return ResponseBuilder.success(response, "Cost types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CostTypeResponseDto>>> searchCostTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "typeName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<CostTypeResponseDto> response = costTypeService.searchCostTypes(searchTerm, pageable);
        return ResponseBuilder.success(response, "Cost types search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<CostTypeResponseDto>>> getAllCostTypesList() {
        List<CostTypeResponseDto> response = costTypeService.getAllCostTypesList();
        return ResponseBuilder.success(response, "Cost types list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CostTypeResponseDto>> getCostTypeById(@PathVariable Long id) {
        CostTypeResponseDto response = costTypeService.getCostTypeById(id);
        return ResponseBuilder.success(response, "Cost type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CostTypeResponseDto>> updateCostType(
            @PathVariable Long id,
            @Valid @RequestBody CostTypeRequestDto requestDto) {
        CostTypeResponseDto response = costTypeService.updateCostType(id, requestDto);
        return ResponseBuilder.success(response, "Cost type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCostType(@PathVariable Long id) {
        costTypeService.deleteCostType(id);
        return ResponseBuilder.success(null, "Cost type deleted successfully");
    }
}
