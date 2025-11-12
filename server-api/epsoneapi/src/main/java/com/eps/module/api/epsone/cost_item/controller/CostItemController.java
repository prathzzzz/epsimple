package com.eps.module.api.epsone.cost_item.controller;

import com.eps.module.api.epsone.cost_item.dto.CostItemRequestDto;
import com.eps.module.api.epsone.cost_item.dto.CostItemResponseDto;
import com.eps.module.api.epsone.cost_item.service.CostItemService;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
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

import java.util.List;

@RestController
@RequestMapping("/api/cost-items")
@RequiredArgsConstructor
@Slf4j
public class CostItemController {

    private final CostItemService costItemService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    @PostMapping
    public ResponseEntity<?> createCostItem(@Valid @RequestBody CostItemRequestDto requestDto) {
        log.info("POST /api/cost-items - Creating cost item");
        CostItemResponseDto response = costItemService.createCostItem(requestDto);
        return ResponseBuilder.success(response, "Cost item created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllCostItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("GET /api/cost-items - Fetching all cost items");

        // Map DTO field names to entity field names for sorting
        String entitySortField = mapSortFieldToEntity(sortBy);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, entitySortField));

        Page<CostItemResponseDto> costItemsPage = costItemService.getAllCostItems(pageable);
        return ResponseBuilder.success(costItemsPage, "Cost items fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCostItems(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        log.info("GET /api/cost-items/search - Searching cost items with term: {}", searchTerm);

        // Map DTO field names to entity field names for sorting
        String entitySortField = mapSortFieldToEntity(sortBy);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, entitySortField));

        Page<CostItemResponseDto> costItemsPage = costItemService.searchCostItems(searchTerm, pageable);
        return ResponseBuilder.success(costItemsPage, "Cost items search completed", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllCostItemsList() {
        log.info("GET /api/cost-items/list - Fetching all cost items as list");
        List<CostItemResponseDto> costItems = costItemService.getAllCostItemsList();
        return ResponseBuilder.success(costItems, "Cost items list fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCostItemById(@PathVariable Long id) {
        log.info("GET /api/cost-items/{} - Fetching cost item by ID", id);
        CostItemResponseDto response = costItemService.getCostItemById(id);
        return ResponseBuilder.success(response, "Cost item fetched successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCostItem(
            @PathVariable Long id,
            @Valid @RequestBody CostItemRequestDto requestDto) {
        log.info("PUT /api/cost-items/{} - Updating cost item", id);
        CostItemResponseDto response = costItemService.updateCostItem(id, requestDto);
        return ResponseBuilder.success(response, "Cost item updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCostItem(@PathVariable Long id) {
        log.info("DELETE /api/cost-items/{} - Deleting cost item", id);
        costItemService.deleteCostItem(id);
        return ResponseBuilder.success(null, "Cost item deleted successfully", HttpStatus.OK);
    }

    // Bulk endpoints

    @PostMapping(value = "/bulk-upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter bulkUploadCostItems(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        log.info("POST /api/cost-items/bulk-upload - Starting bulk upload");
        return bulkUploadHelper.bulkUpload(file, costItemService);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCostItems() throws java.io.IOException {
        log.info("GET /api/cost-items/export - Exporting all cost items to Excel");
        return bulkUploadHelper.export(costItemService);
    }

    @GetMapping("/download-template")
    public ResponseEntity<byte[]> downloadTemplate() throws java.io.IOException {
        log.info("GET /api/cost-items/download-template - Downloading bulk upload template");
        return bulkUploadHelper.downloadTemplate(costItemService);
    }

    @PostMapping("/export-errors")
    public ResponseEntity<byte[]> exportBulkUploadErrors(
            @RequestBody com.eps.module.common.bulk.dto.BulkUploadProgressDto progressData) throws java.io.IOException {
        log.info("POST /api/cost-items/export-errors - Exporting bulk upload errors");
        return bulkUploadHelper.exportErrors(progressData, costItemService);
    }

    /**
     * Map DTO field names to entity field names for sorting
     * This handles nested relationships and prevents PropertyReferenceException
     */
    private String mapSortFieldToEntity(String dtoField) {
        return switch (dtoField) {
            case "costTypeName" -> "costType.typeName";
            case "costCategoryName" -> "costType.costCategory.categoryName";
            default -> dtoField;
        };
    }
}
