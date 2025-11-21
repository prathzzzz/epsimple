package com.eps.module.api.epsone.activity.controller;

import com.eps.module.api.epsone.activity.dto.ActivityRequestDto;
import com.eps.module.api.epsone.activity.dto.ActivityResponseDto;
import com.eps.module.api.epsone.activity.service.ActivityService;
import com.eps.module.auth.rbac.annotation.RequireAdmin;
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
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;
    private final BulkUploadControllerHelper bulkUploadControllerHelper;

    // ========== Bulk Upload Endpoints ==========

    @PostMapping(value = "/bulk-upload", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @RequireAdmin
    public SseEmitter bulkUploadActivities(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("POST /api/activity/bulk-upload - Starting bulk upload");
        return bulkUploadControllerHelper.bulkUpload(file, activityService);
    }

    @GetMapping("/bulk-upload/template")
    @RequireAdmin
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        log.info("GET /api/activity/bulk-upload/template - Downloading template");
        return bulkUploadControllerHelper.downloadTemplate(activityService);
    }

    @PostMapping("/bulk-upload/errors")
    @RequireAdmin
    public ResponseEntity<byte[]> downloadErrorReport(@RequestBody BulkUploadProgressDto progressData) throws Exception {
        log.info("POST /api/activity/bulk-upload/errors - Downloading error report");
        return bulkUploadControllerHelper.exportErrors(progressData, activityService);
    }

    // ========== CRUD Endpoints ==========

    @PostMapping
    @RequireAdmin
    public ResponseEntity<ApiResponse<ActivityResponseDto>> createActivity(
            @Valid @RequestBody ActivityRequestDto activityRequestDto) {
        ActivityResponseDto response = activityService.createActivity(activityRequestDto);
        return ResponseBuilder.success(response, "Activity created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ActivityResponseDto>>> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ActivityResponseDto> activities = activityService.getAllActivities(pageable);

        return ResponseBuilder.success(activities, "Activities retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ActivityResponseDto>>> searchActivities(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ActivityResponseDto> activities = activityService.searchActivities(searchTerm, pageable);

        return ResponseBuilder.success(activities, "Activities search completed");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ActivityResponseDto>>> getActivityList() {
        List<ActivityResponseDto> activities = activityService.getActivityList();
        return ResponseBuilder.success(activities, "Activity list retrieved successfully");
    }

    // ========== Export Endpoint (must be before /{id}) ==========

    @GetMapping("/export")
    @RequireAdmin
    public ResponseEntity<byte[]> exportData() throws Exception {
        log.info("GET /api/activity/export - Exporting all activities");
        return bulkUploadControllerHelper.export(activityService);
    }

    // ========== CRUD Endpoints ==========

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> getActivityById(@PathVariable Long id) {
        ActivityResponseDto activity = activityService.getActivityById(id);
        return ResponseBuilder.success(activity, "Activity retrieved successfully");
    }

    @PutMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<ActivityResponseDto>> updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody ActivityRequestDto activityRequestDto) {
        ActivityResponseDto response = activityService.updateActivity(id, activityRequestDto);
        return ResponseBuilder.success(response, "Activity updated successfully");
    }

    @DeleteMapping("/{id}")
    @RequireAdmin
    public ResponseEntity<ApiResponse<Void>> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseBuilder.success(null, "Activity deleted successfully");
    }
}
