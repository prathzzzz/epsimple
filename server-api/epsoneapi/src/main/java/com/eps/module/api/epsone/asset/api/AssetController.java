package com.eps.module.api.epsone.asset.api;

import com.eps.module.api.epsone.asset.dto.AssetFinancialDetailsDto;
import com.eps.module.api.epsone.asset.dto.AssetFinancialExportRequestDto;
import com.eps.module.api.epsone.asset.dto.AssetFinancialExportRowDto;
import com.eps.module.api.epsone.asset.dto.AssetRequestDto;
import com.eps.module.api.epsone.asset.dto.AssetResponseDto;
import com.eps.module.api.epsone.asset.service.AssetService;
import com.eps.module.auth.rbac.annotation.RequirePermission;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.bulk.excel.ExcelExportUtil;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final BulkUploadControllerHelper bulkUploadHelper;
    private final ExcelExportUtil excelExportUtil;

    // ========== Bulk Upload Endpoints ==========

    @RequirePermission("ASSET:BULK_UPLOAD")
    @PostMapping("/bulk/upload")
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("POST /api/assets/bulk/upload - Starting bulk upload with file: {}", file.getOriginalFilename());
        return bulkUploadHelper.bulkUpload(file, assetService);
    }

    @GetMapping("/bulk/export-template")
    public ResponseEntity<byte[]> exportTemplate() throws IOException {
        log.info("GET /api/assets/bulk/export-template - Exporting template");
        return bulkUploadHelper.downloadTemplate(assetService);
    }

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

    // ========== Financial Export Endpoint ==========

    @RequirePermission("ASSET:FINANCIAL_EXPORT")
    @PostMapping("/financial-export")
    public ResponseEntity<byte[]> exportFinancialData(@RequestBody AssetFinancialExportRequestDto requestDto) throws IOException {
        log.info("POST /api/assets/financial-export - Exporting financial data with filters: managedProjectId={}, assetCategoryId={}, wdvToDate={}, customFromDate={}, customToDate={}",
                requestDto.getManagedProjectId(), requestDto.getAssetCategoryId(), requestDto.getWdvToDate(), requestDto.getCustomFromDate(), requestDto.getCustomToDate());
        
        List<AssetFinancialExportRowDto> data = assetService.getFinancialExportData(requestDto);
        
        byte[] excelData = excelExportUtil.exportToExcel(data, AssetFinancialExportRowDto.class, "Asset Financial Report");
        
        String filename = "asset_financial_report_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", filename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // ========== CRUD Endpoints ==========

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

    @RequirePermission("ASSET:FINANCIAL_VIEW")
    @GetMapping("/{id}/financial-details")
    public ResponseEntity<ApiResponse<AssetFinancialDetailsDto>> getAssetFinancialDetails(@PathVariable Long id) {
        log.info("GET /api/assets/{}/financial-details - Fetching financial details", id);
        AssetFinancialDetailsDto financialDetails = assetService.getAssetFinancialDetails(id);
        return ResponseBuilder.success(financialDetails, "Asset financial details retrieved successfully");
    }

    @RequirePermission("ASSET:SCRAP")
    @PostMapping("/{id}/scrap")
    public ResponseEntity<ApiResponse<Void>> scrapAsset(@PathVariable Long id) {
        log.info("POST /api/assets/{}/scrap - Scrapping asset", id);
        assetService.scrapAsset(id);
        return ResponseBuilder.success(null, "Asset scrapped successfully");
    }

    @RequirePermission("ASSET:SCRAP")
    @PostMapping("/{id}/unscrap")
    public ResponseEntity<ApiResponse<Void>> unscrapAsset(@PathVariable Long id) {
        log.info("POST /api/assets/{}/unscrap - Unscrapping asset", id);
        assetService.unscrapAsset(id);
        return ResponseBuilder.success(null, "Asset unscraped successfully");
    }
}
