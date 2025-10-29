package com.eps.module.api.epsone.sitecode.controller;

import com.eps.module.api.epsone.sitecode.dto.GeneratedSiteCodeDto;
import com.eps.module.api.epsone.sitecode.dto.SiteCodeGeneratorRequestDto;
import com.eps.module.api.epsone.sitecode.dto.SiteCodeGeneratorResponseDto;
import com.eps.module.api.epsone.sitecode.service.SiteCodeGeneratorService;
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
@RequestMapping("/api/site-code-generators")
@RequiredArgsConstructor
public class SiteCodeGeneratorController {

    private final SiteCodeGeneratorService siteCodeGeneratorService;

    @PostMapping
    public ResponseEntity<ApiResponse<SiteCodeGeneratorResponseDto>> createSiteCodeGenerator(
            @Valid @RequestBody SiteCodeGeneratorRequestDto requestDto) {
        log.info("REST request to create site code generator: {}", requestDto);
        SiteCodeGeneratorResponseDto result = siteCodeGeneratorService.createSiteCodeGenerator(requestDto);
        return ResponseBuilder.success(result, "Site code generator created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SiteCodeGeneratorResponseDto>>> getAllSiteCodeGenerators(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("REST request to get all site code generators - page: {}, size: {}, sortBy: {}, direction: {}",
                page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SiteCodeGeneratorResponseDto> result = siteCodeGeneratorService.getAllSiteCodeGenerators(pageable);
        return ResponseBuilder.success(result, "Site code generators retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SiteCodeGeneratorResponseDto>>> searchSiteCodeGenerators(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        log.info("REST request to search site code generators - searchTerm: {}, page: {}, size: {}",
                searchTerm, page, size);

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SiteCodeGeneratorResponseDto> result = siteCodeGeneratorService.searchSiteCodeGenerators(searchTerm, pageable);
        return ResponseBuilder.success(result, "Site code generators searched successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<SiteCodeGeneratorResponseDto>>> getListSiteCodeGenerators() {
        log.info("REST request to get list of all site code generators");
        List<SiteCodeGeneratorResponseDto> result = siteCodeGeneratorService.getListSiteCodeGenerators();
        return ResponseBuilder.success(result, "Site code generators list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteCodeGeneratorResponseDto>> getSiteCodeGeneratorById(@PathVariable Long id) {
        log.info("REST request to get site code generator by id: {}", id);
        SiteCodeGeneratorResponseDto result = siteCodeGeneratorService.getSiteCodeGeneratorById(id);
        return ResponseBuilder.success(result, "Site code generator retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteCodeGeneratorResponseDto>> updateSiteCodeGenerator(
            @PathVariable Long id,
            @Valid @RequestBody SiteCodeGeneratorRequestDto requestDto) {
        log.info("REST request to update site code generator with id: {}", id);
        SiteCodeGeneratorResponseDto result = siteCodeGeneratorService.updateSiteCodeGenerator(id, requestDto);
        return ResponseBuilder.success(result, "Site code generator updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSiteCodeGenerator(@PathVariable Long id) {
        log.info("REST request to delete site code generator with id: {}", id);
        siteCodeGeneratorService.deleteSiteCodeGenerator(id);
        return ResponseBuilder.success(null, "Site code generator deleted successfully");
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<GeneratedSiteCodeDto>> generateSiteCode(
            @RequestParam Long projectId,
            @RequestParam Long stateId) {
        log.info("REST request to generate site code for project: {}, state: {}",
                projectId, stateId);
        GeneratedSiteCodeDto result = siteCodeGeneratorService.generateSiteCode(projectId, stateId);
        return ResponseBuilder.success(result, "Site code generated successfully");
    }

    @GetMapping("/preview")
    public ResponseEntity<ApiResponse<GeneratedSiteCodeDto>> previewSiteCode(
            @RequestParam Long projectId,
            @RequestParam Long stateId) {
        log.info("REST request to preview site code for project: {}, state: {}",
                projectId, stateId);
        GeneratedSiteCodeDto result = siteCodeGeneratorService.previewSiteCode(projectId, stateId);
        return ResponseBuilder.success(result, "Site code previewed successfully");
    }
}
