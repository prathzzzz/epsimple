package com.eps.module.api.epsone.sitetype.controller;

import com.eps.module.api.epsone.sitetype.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.sitetype.dto.SiteTypeResponseDto;
import com.eps.module.api.epsone.sitetype.service.SiteTypeService;
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
@RequestMapping("/api/site-types")
@RequiredArgsConstructor
public class SiteTypeController {

    private final SiteTypeService siteTypeService;

    @PostMapping
    public ResponseEntity<SiteTypeResponseDto> createSiteType(@Valid @RequestBody SiteTypeRequestDto requestDto) {
        SiteTypeResponseDto response = siteTypeService.createSiteType(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<SiteTypeResponseDto>> getAllSiteTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteTypeResponseDto> siteTypes = siteTypeService.getAllSiteTypes(pageable);
        return ResponseEntity.ok(siteTypes);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SiteTypeResponseDto>> searchSiteTypes(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<SiteTypeResponseDto> siteTypes = siteTypeService.searchSiteTypes(searchTerm, pageable);
        return ResponseEntity.ok(siteTypes);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SiteTypeResponseDto>> getSiteTypeList() {
        List<SiteTypeResponseDto> siteTypes = siteTypeService.getSiteTypeList();
        return ResponseEntity.ok(siteTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteTypeResponseDto> getSiteTypeById(@PathVariable Long id) {
        SiteTypeResponseDto siteType = siteTypeService.getSiteTypeById(id);
        return ResponseEntity.ok(siteType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiteTypeResponseDto> updateSiteType(
            @PathVariable Long id,
            @Valid @RequestBody SiteTypeRequestDto requestDto) {
        SiteTypeResponseDto updatedSiteType = siteTypeService.updateSiteType(id, requestDto);
        return ResponseEntity.ok(updatedSiteType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiteType(@PathVariable Long id) {
        siteTypeService.deleteSiteType(id);
        return ResponseEntity.noContent().build();
    }
}
