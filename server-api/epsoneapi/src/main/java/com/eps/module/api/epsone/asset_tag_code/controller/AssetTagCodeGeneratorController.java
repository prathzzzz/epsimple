package com.eps.module.api.epsone.asset_tag_code.controller;

import com.eps.module.api.epsone.asset_tag_code.dto.AssetTagCodeGeneratorRequestDto;
import com.eps.module.api.epsone.asset_tag_code.dto.AssetTagCodeGeneratorResponseDto;
import com.eps.module.api.epsone.asset_tag_code.dto.GeneratedAssetTagDto;
import com.eps.module.api.epsone.asset_tag_code.service.AssetTagCodeGeneratorService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset-tag-generators")
@RequiredArgsConstructor
@Slf4j
public class AssetTagCodeGeneratorController {

    private final AssetTagCodeGeneratorService generatorService;

    @PostMapping
    public ResponseEntity<ApiResponse<AssetTagCodeGeneratorResponseDto>> createGenerator(
        @Valid @RequestBody AssetTagCodeGeneratorRequestDto dto
    ) {
        log.info("POST /api/asset-tag-generators - Creating asset tag generator");
        AssetTagCodeGeneratorResponseDto response = generatorService.createGenerator(dto);
        return ResponseBuilder.success(response, "Asset tag generator created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetTagCodeGeneratorResponseDto>>> getAllGenerators(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /api/asset-tag-generators - Fetching generators page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<AssetTagCodeGeneratorResponseDto> generators = generatorService.getAllGenerators(pageable);
        return ResponseBuilder.success(generators, "Asset tag generators fetched successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AssetTagCodeGeneratorResponseDto>>> searchGenerators(
        @RequestParam String searchTerm,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /api/asset-tag-generators/search - Searching with term: {}", searchTerm);
        Pageable pageable = PageRequest.of(page, size);
        Page<AssetTagCodeGeneratorResponseDto> generators = generatorService.searchGenerators(searchTerm, pageable);
        return ResponseBuilder.success(generators, "Search completed successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<AssetTagCodeGeneratorResponseDto>>> getAllGeneratorsAsList() {
        log.info("GET /api/asset-tag-generators/list - Fetching all generators as list");
        List<AssetTagCodeGeneratorResponseDto> generators = generatorService.getAllGeneratorsAsList();
        return ResponseBuilder.success(generators, "Asset tag generators list fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetTagCodeGeneratorResponseDto>> getGeneratorById(
        @PathVariable Long id
    ) {
        log.info("GET /api/asset-tag-generators/{} - Fetching generator", id);
        AssetTagCodeGeneratorResponseDto generator = generatorService.getGeneratorById(id);
        return ResponseBuilder.success(generator, "Asset tag generator fetched successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetTagCodeGeneratorResponseDto>> updateGenerator(
        @PathVariable Long id,
        @Valid @RequestBody AssetTagCodeGeneratorRequestDto dto
    ) {
        log.info("PUT /api/asset-tag-generators/{} - Updating generator", id);
        AssetTagCodeGeneratorResponseDto updated = generatorService.updateGenerator(id, dto);
        return ResponseBuilder.success(updated, "Asset tag generator updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGenerator(@PathVariable Long id) {
        log.info("DELETE /api/asset-tag-generators/{} - Deleting generator", id);
        generatorService.deleteGenerator(id);
        return ResponseBuilder.success(null, "Asset tag generator deleted successfully");
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<GeneratedAssetTagDto>> generateAssetTag(
        @RequestBody AssetTagCodeGeneratorRequestDto dto
    ) {
        log.info("POST /api/asset-tag-generators/generate - Generating asset tag");
        GeneratedAssetTagDto generated = generatorService.generateAssetTag(
            dto.getAssetCategoryId(),
            dto.getVendorId(),
            dto.getBankId()
        );
        return ResponseBuilder.success(generated, "Asset tag generated successfully");
    }

    @GetMapping("/preview")
    public ResponseEntity<ApiResponse<GeneratedAssetTagDto>> previewAssetTag(
        @RequestParam Long assetCategoryId,
        @RequestParam Long vendorId,
        @RequestParam Long bankId
    ) {
        log.info("GET /api/asset-tag-generators/preview - Previewing asset tag");
        GeneratedAssetTagDto preview = generatorService.previewAssetTag(
            assetCategoryId,
            vendorId,
            bankId
        );
        return ResponseBuilder.success(preview, "Asset tag preview generated successfully");
    }
}
