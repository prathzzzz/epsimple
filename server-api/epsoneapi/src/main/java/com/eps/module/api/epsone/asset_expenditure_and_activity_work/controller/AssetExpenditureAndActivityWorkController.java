package com.eps.module.api.epsone.asset_expenditure_and_activity_work.controller;

import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkRequestDto;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkResponseDto;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.service.AssetExpenditureAndActivityWorkService;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/asset-expenditure-and-activity-works")
@RequiredArgsConstructor
public class AssetExpenditureAndActivityWorkController {

    private final AssetExpenditureAndActivityWorkService service;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    // ========== Bulk Upload Endpoints ==========

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter bulkUpload(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("POST /api/asset-expenditure-and-activity-works/bulk-upload - Starting bulk upload");
        return bulkUploadControllerHelper.bulkUpload(file, service);
    }

    @GetMapping("/bulk-upload/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        log.info("GET /api/asset-expenditure-and-activity-works/bulk-upload/template - Downloading template");
        return bulkUploadControllerHelper.downloadTemplate(service);
    }

    @GetMapping("/bulk-upload/export")
    public ResponseEntity<byte[]> exportData() throws Exception {
        log.info("GET /api/asset-expenditure-and-activity-works/bulk-upload/export - Exporting all data");
        return bulkUploadControllerHelper.export(service);
    }

    @PostMapping("/bulk-upload/errors")
    public ResponseEntity<byte[]> exportErrors(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        log.info("POST /api/asset-expenditure-and-activity-works/bulk-upload/errors - Downloading error report");
        return bulkUploadControllerHelper.exportErrors(progressData, service);
    }

    // ========== CRUD Endpoints ==========

    @PostMapping
    public ResponseEntity<ApiResponse<AssetExpenditureAndActivityWorkResponseDto>> createAssetExpenditureAndActivityWork(
            @Valid @RequestBody AssetExpenditureAndActivityWorkRequestDto requestDto) {
        
        log.info("POST /api/asset-expenditure-and-activity-works - Creating asset expenditure and activity work");
        AssetExpenditureAndActivityWorkResponseDto responseDto = service.createAssetExpenditureAndActivityWork(requestDto);
        return ResponseBuilder.success(responseDto, "Asset expenditure and activity work created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AssetExpenditureAndActivityWorkResponseDto>>> getAllAssetExpenditureAndActivityWorks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        log.info("GET /api/asset-expenditure-and-activity-works - Fetching all expenditures");
        Page<AssetExpenditureAndActivityWorkResponseDto> expenditures = 
                service.getAllAssetExpenditureAndActivityWorks(page, size, sortBy, sortOrder);
        return ResponseBuilder.success(expenditures, "Asset expenditure and activity works fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<ApiResponse<Page<AssetExpenditureAndActivityWorkResponseDto>>> getExpendituresByAssetId(
            @PathVariable Long assetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        log.info("GET /api/asset-expenditure-and-activity-works/asset/{} - Fetching expenditures for asset", assetId);
        Page<AssetExpenditureAndActivityWorkResponseDto> expenditures = 
                service.getExpendituresByAssetId(assetId, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(expenditures, "Asset expenditures fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/activity-work/{activityWorkId}")
    public ResponseEntity<ApiResponse<Page<AssetExpenditureAndActivityWorkResponseDto>>> getExpendituresByActivityWorkId(
            @PathVariable Long activityWorkId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        log.info("GET /api/asset-expenditure-and-activity-works/activity-work/{} - Fetching expenditures for activity work", activityWorkId);
        Page<AssetExpenditureAndActivityWorkResponseDto> expenditures = 
                service.getExpendituresByActivityWorkId(activityWorkId, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(expenditures, "Activity work expenditures fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AssetExpenditureAndActivityWorkResponseDto>>> searchExpenditures(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        
        log.info("GET /api/asset-expenditure-and-activity-works/search - Searching expenditures with keyword: {}", keyword);
        Page<AssetExpenditureAndActivityWorkResponseDto> expenditures = 
                service.searchExpenditures(keyword, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(expenditures, "Asset expenditure and activity works search completed", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetExpenditureAndActivityWorkResponseDto>> getExpenditureById(
            @PathVariable Long id) {
        
        log.info("GET /api/asset-expenditure-and-activity-works/{} - Fetching expenditure", id);
        AssetExpenditureAndActivityWorkResponseDto responseDto = service.getExpenditureById(id);
        return ResponseBuilder.success(responseDto, "Asset expenditure and activity work fetched successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetExpenditureAndActivityWorkResponseDto>> updateAssetExpenditureAndActivityWork(
            @PathVariable Long id,
            @Valid @RequestBody AssetExpenditureAndActivityWorkRequestDto requestDto) {
        
        log.info("PUT /api/asset-expenditure-and-activity-works/{} - Updating expenditure", id);
        AssetExpenditureAndActivityWorkResponseDto responseDto = service.updateAssetExpenditureAndActivityWork(id, requestDto);
        return ResponseBuilder.success(responseDto, "Asset expenditure and activity work updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAssetExpenditureAndActivityWork(
            @PathVariable Long id) {
        
        log.info("DELETE /api/asset-expenditure-and-activity-works/{} - Deleting expenditure", id);
        service.deleteAssetExpenditureAndActivityWork(id);
        return ResponseBuilder.success(null, "Asset expenditure and activity work deleted successfully", HttpStatus.OK);
    }
}
