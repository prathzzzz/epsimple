package com.eps.module.api.epsone.site_type.controller;

import com.eps.module.api.epsone.site_type.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeResponseDto;
import com.eps.module.api.epsone.site_type.service.SiteTypeService;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/site-types")
@RequiredArgsConstructor
public class SiteTypeController {

    private final SiteTypeService siteTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<SiteTypeResponseDto>> createSiteType(@Valid @RequestBody SiteTypeRequestDto requestDto) {
        log.info("POST /api/site-types - Creating new site type");
        SiteTypeResponseDto response = siteTypeService.createSiteType(requestDto);
        return ResponseBuilder.success(response, "Site type created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SiteTypeResponseDto>>> getAllSiteTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/site-types - Fetching all site types with pagination");
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteTypeResponseDto> siteTypes = siteTypeService.getAllSiteTypes(pageable);
        return ResponseBuilder.success(siteTypes, "Site types retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SiteTypeResponseDto>>> searchSiteTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/site-types/search - Searching site types with term: {}", searchTerm);
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteTypeResponseDto> siteTypes = siteTypeService.searchSiteTypes(searchTerm, pageable);
        return ResponseBuilder.success(siteTypes, "Site types search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<SiteTypeResponseDto>>> getSiteTypeList() {
        log.info("GET /api/site-types/list - Fetching all site types as list");
        List<SiteTypeResponseDto> siteTypes = siteTypeService.getSiteTypeList();
        return ResponseBuilder.success(siteTypes, "Site types list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteTypeResponseDto>> getSiteTypeById(@PathVariable Long id) {
        log.info("GET /api/site-types/{} - Fetching site type by ID", id);
        SiteTypeResponseDto siteType = siteTypeService.getSiteTypeById(id);
        return ResponseBuilder.success(siteType, "Site type retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteTypeResponseDto>> updateSiteType(
            @PathVariable Long id,
            @Valid @RequestBody SiteTypeRequestDto requestDto) {
        log.info("PUT /api/site-types/{} - Updating site type", id);
        SiteTypeResponseDto updatedSiteType = siteTypeService.updateSiteType(id, requestDto);
        return ResponseBuilder.success(updatedSiteType, "Site type updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSiteType(@PathVariable Long id) {
        log.info("DELETE /api/site-types/{} - Deleting site type", id);
        siteTypeService.deleteSiteType(id);
        return ResponseBuilder.success(null, "Site type deleted successfully");
    }
}
