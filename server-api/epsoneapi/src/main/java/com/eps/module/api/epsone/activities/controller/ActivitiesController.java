package com.eps.module.api.epsone.activities.controller;

import com.eps.module.api.epsone.activities.dto.ActivitiesRequestDto;
import com.eps.module.api.epsone.activities.dto.ActivitiesResponseDto;
import com.eps.module.api.epsone.activities.service.ActivitiesService;
import com.eps.module.auth.rbac.annotation.RequirePermission;
import com.eps.module.common.bulk.controller.BulkUploadControllerHelper;
import com.eps.module.common.bulk.dto.BulkUploadProgressDto;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivitiesController {

    private final ActivitiesService activitiesService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    // ========== Bulk Upload Endpoints ==========

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @RequirePermission("ACTIVITY_LIST:BULK_UPLOAD")
    public SseEmitter bulkUploadActivities(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("POST /api/activities/bulk-upload - Starting bulk upload");
        return bulkUploadControllerHelper.bulkUpload(file, activitiesService);
    }

    @GetMapping("/bulk-upload/template")
    @RequirePermission("ACTIVITY_LIST:READ")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        log.info("GET /api/activities/bulk-upload/template - Downloading template");
        return bulkUploadControllerHelper.downloadTemplate(activitiesService);
    }

    @GetMapping("/bulk-upload/export")
    @RequirePermission("ACTIVITY_LIST:EXPORT")
    public ResponseEntity<byte[]> exportData() throws Exception {
        log.info("GET /api/activities/bulk-upload/export - Exporting data");
        return bulkUploadControllerHelper.export(activitiesService);
    }

    @PostMapping("/bulk-upload/errors")
    @RequirePermission("ACTIVITY_LIST:READ")
    public ResponseEntity<byte[]> downloadErrorReport(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        log.info("POST /api/activities/bulk-upload/errors - Downloading error report");
        return bulkUploadControllerHelper.exportErrors(progressData, activitiesService);
    }

    // ========== CRUD Endpoints ==========

    @PostMapping
    @RequirePermission("ACTIVITY_LIST:CREATE")
    public ResponseEntity<ApiResponse<ActivitiesResponseDto>> createActivities(
            @Valid @RequestBody ActivitiesRequestDto activitiesRequestDto) {
        ActivitiesResponseDto response = activitiesService.createActivities(activitiesRequestDto);
        return ResponseBuilder.success(response, "Activities created successfully");
    }

    @GetMapping
    @RequirePermission("ACTIVITY_LIST:READ")
    public ResponseEntity<ApiResponse<Page<ActivitiesResponseDto>>> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ActivitiesResponseDto> activities = activitiesService.getAllActivities(pageable);

        return ResponseBuilder.success(activities, "Activities retrieved successfully");
    }

    @GetMapping("/search")
    @RequirePermission("ACTIVITY_LIST:READ")
    public ResponseEntity<ApiResponse<Page<ActivitiesResponseDto>>> searchActivities(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ActivitiesResponseDto> activities = activitiesService.searchActivities(searchTerm, pageable);

        return ResponseBuilder.success(activities, "Activities search completed");
    }

    @GetMapping("/list")
    @RequirePermission("ACTIVITY_LIST:READ")
    public ResponseEntity<ApiResponse<List<ActivitiesResponseDto>>> getActivitiesList() {
        List<ActivitiesResponseDto> activities = activitiesService.getActivitiesList();
        return ResponseBuilder.success(activities, "Activities list retrieved successfully");
    }

    @GetMapping("/{id}")
    @RequirePermission("ACTIVITY_LIST:READ")
    public ResponseEntity<ApiResponse<ActivitiesResponseDto>> getActivitiesById(@PathVariable Long id) {
        ActivitiesResponseDto activities = activitiesService.getActivitiesById(id);
        return ResponseBuilder.success(activities, "Activities retrieved successfully");
    }

    @PutMapping("/{id}")
    @RequirePermission("ACTIVITY_LIST:UPDATE")
    public ResponseEntity<ApiResponse<ActivitiesResponseDto>> updateActivities(
            @PathVariable Long id,
            @Valid @RequestBody ActivitiesRequestDto activitiesRequestDto) {
        ActivitiesResponseDto response = activitiesService.updateActivities(id, activitiesRequestDto);
        return ResponseBuilder.success(response, "Activities updated successfully");
    }

    @DeleteMapping("/{id}")
    @RequirePermission("ACTIVITY_LIST:DELETE")
    public ResponseEntity<ApiResponse<Void>> deleteActivities(@PathVariable Long id) {
        activitiesService.deleteActivities(id);
        return ResponseBuilder.success(null, "Activities deleted successfully");
    }
}
