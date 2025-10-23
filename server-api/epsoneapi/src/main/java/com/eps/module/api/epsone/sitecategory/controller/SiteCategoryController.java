package com.eps.module.api.epsone.sitecategory.controller;

import com.eps.module.api.epsone.sitecategory.dto.SiteCategoryRequestDto;
import com.eps.module.api.epsone.sitecategory.dto.SiteCategoryResponseDto;
import com.eps.module.api.epsone.sitecategory.service.SiteCategoryService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/site-categories")
@RequiredArgsConstructor
public class SiteCategoryController {

    private final SiteCategoryService siteCategoryService;

    @PostMapping
    public ResponseEntity<SiteCategoryResponseDto> createSiteCategory(@Valid @RequestBody SiteCategoryRequestDto requestDto) {
        SiteCategoryResponseDto response = siteCategoryService.createSiteCategory(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<SiteCategoryResponseDto>> getAllSiteCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteCategoryResponseDto> siteCategories = siteCategoryService.getAllSiteCategories(pageable);
        return ResponseEntity.ok(siteCategories);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SiteCategoryResponseDto>> searchSiteCategories(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteCategoryResponseDto> siteCategories = siteCategoryService.searchSiteCategories(searchTerm, pageable);
        return ResponseEntity.ok(siteCategories);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SiteCategoryResponseDto>> getSiteCategoryList() {
        List<SiteCategoryResponseDto> siteCategories = siteCategoryService.getSiteCategoryList();
        return ResponseEntity.ok(siteCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteCategoryResponseDto> getSiteCategoryById(@PathVariable Long id) {
        SiteCategoryResponseDto siteCategory = siteCategoryService.getSiteCategoryById(id);
        return ResponseEntity.ok(siteCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiteCategoryResponseDto> updateSiteCategory(
            @PathVariable Long id,
            @Valid @RequestBody SiteCategoryRequestDto requestDto) {
        SiteCategoryResponseDto updatedSiteCategory = siteCategoryService.updateSiteCategory(id, requestDto);
        return ResponseEntity.ok(updatedSiteCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSiteCategory(@PathVariable Long id) {
        siteCategoryService.deleteSiteCategory(id);
        return ResponseBuilder.success(null, "Site category deleted successfully");
    }
}
