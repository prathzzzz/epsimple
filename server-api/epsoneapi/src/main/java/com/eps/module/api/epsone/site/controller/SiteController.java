package com.eps.module.api.epsone.site.controller;

import com.eps.module.api.epsone.site.dto.SiteRequestDto;
import com.eps.module.api.epsone.site.dto.SiteResponseDto;
import com.eps.module.api.epsone.site.service.SiteService;
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
@RequestMapping("/api/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @PostMapping
    public ResponseEntity<ApiResponse<SiteResponseDto>> createSite(@Valid @RequestBody SiteRequestDto requestDto) {
        log.info("POST /api/sites - Creating new site");
        SiteResponseDto response = siteService.createSite(requestDto);
        return ResponseBuilder.success(response, "Site created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SiteResponseDto>>> getAllSites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("GET /api/sites - Fetching all sites with pagination");
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sanitizedSortBy = sanitizeSortBy(sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sanitizedSortBy));
        Page<SiteResponseDto> sites = siteService.getAllSites(pageable);
        return ResponseBuilder.success(sites, "Sites retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SiteResponseDto>>> searchSites(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("GET /api/sites/search - Searching sites with term: {}", searchTerm);
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sanitizedSortBy = sanitizeSortBy(sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sanitizedSortBy));
        Page<SiteResponseDto> sites = siteService.searchSites(searchTerm, pageable);
        return ResponseBuilder.success(sites, "Sites search completed successfully");
    }

    /**
     * Map client-facing sortBy values to real entity properties to avoid invalid paths in JPQL.
     * Extend this mapping if the frontend sends other friendly names.
     */
    private String sanitizeSortBy(String sortBy) {
        if (sortBy == null) return "id";
        switch (sortBy) {
            // frontend uses "siteName" as a friendly column name — map it to an actual field
            case "siteName":
                return "siteCode";
            // allow sorting by project name through nested property
            case "projectName":
                return "project.projectName";
            default:
                return sortBy;
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<SiteResponseDto>>> getSiteList() {
        log.info("GET /api/sites/list - Fetching all sites as list");
        List<SiteResponseDto> sites = siteService.getSiteList();
        return ResponseBuilder.success(sites, "Sites list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteResponseDto>> getSiteById(@PathVariable Long id) {
        log.info("GET /api/sites/{} - Fetching site by ID", id);
        SiteResponseDto site = siteService.getSiteById(id);
        return ResponseBuilder.success(site, "Site retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteResponseDto>> updateSite(
            @PathVariable Long id,
            @Valid @RequestBody SiteRequestDto requestDto) {
        log.info("PUT /api/sites/{} - Updating site", id);
        SiteResponseDto updatedSite = siteService.updateSite(id, requestDto);
        return ResponseBuilder.success(updatedSite, "Site updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSite(@PathVariable Long id) {
        log.info("DELETE /api/sites/{} - Deleting site", id);
        siteService.deleteSite(id);
        return ResponseBuilder.success(null, "Site deleted successfully");
    }
}
