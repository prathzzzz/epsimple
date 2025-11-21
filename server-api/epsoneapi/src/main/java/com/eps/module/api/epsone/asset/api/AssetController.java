package com.eps.module.api.epsone.asset.api;

import com.eps.module.api.epsone.asset.dto.AssetRequestDto;
import com.eps.module.api.epsone.asset.dto.AssetResponseDto;
import com.eps.module.api.epsone.asset.service.AssetService;
import com.eps.module.auth.rbac.annotation.RequirePermission;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final BulkUploadControllerHelper bulkUploadHelper;

    // ========== Bulk Upload Endpoints ==========

    @RequirePermission("ASSET:BULK_UPLOAD")
    @PostMapping("/bulk/upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/assets/bulk/upload - Starting bulk upload with file: {}", file.getOriginalFilename());
        return bulkUploadHelper.bulkUpload(file, assetService);
    }

    @RequirePermission("ASSET:READ")
    @GetMapping("/bulk/export-template")
    public ResponseEntity<byte[]> exportTemplate() throws IOException {
        log.info("GET /api/assets/bulk/export-template - Exporting template");
        return bulkUploadHelper.downloadTemplate(assetService);
    }

    @RequirePermission("ASSET:READ")
    @PostMapping("/bulk/export-error-report")
    public ResponseEntity<byte[]> exportErrorReport(@RequestBody BulkUploadProgressDto progressData) throws IOException {
        log.info("POST /api/assets/bulk/export-error-report - Exporting error report");
        return bulkUploadHelper.exportErrors(progressData, assetService);
    }

    // ========== Existing CRUD Endpoints ==========

    @RequirePermission("ASSET:CREATE")
    @PostMapping
    public ResponseEntity<ApiResponse<AssetResponseDto>> createAsset(
            @RequestBody AssetRequestDto requestDto) {
        AssetResponseDto response = assetService.createAsset(requestDto);
        return ResponseBuilder.success(response, "Asset created successfully");
    }

    @RequirePermission("ASSET:READ")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetResponseDto>>> getAllAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<AssetResponseDto> assets = assetService.getAllAssets(pageable);
        return ResponseBuilder.success(assets, "Assets retrieved successfully");
    }

    @RequirePermission("ASSET:READ")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AssetResponseDto>>> searchAssets(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<AssetResponseDto> assets = assetService.searchAssets(searchTerm, pageable);
        return ResponseBuilder.success(assets, "Assets search completed");
    }

    @RequirePermission("ASSET:READ")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<AssetResponseDto>>> listAssets() {
        List<AssetResponseDto> assets = assetService.listAssets();
        return ResponseBuilder.success(assets, "Assets list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @RequirePermission("ASSET:EXPORT")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() throws IOException {
        log.info("GET /api/assets/export - Exporting all assets");
        return bulkUploadHelper.export(assetService);
    }

    // ========== CRUD Endpoints ==========

    @RequirePermission("ASSET:READ")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponseDto>> getAssetById(@PathVariable Long id) {
        AssetResponseDto asset = assetService.getAssetById(id);
        return ResponseBuilder.success(asset, "Asset retrieved successfully");
    }

    @RequirePermission("ASSET:UPDATE")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponseDto>> updateAsset(
            @PathVariable Long id,
            @RequestBody AssetRequestDto requestDto) {
        AssetResponseDto response = assetService.updateAsset(id, requestDto);
        return ResponseBuilder.success(response, "Asset updated successfully");
    }

    @RequirePermission("ASSET:DELETE")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseBuilder.success(null, "Asset deleted successfully");
    }
}
