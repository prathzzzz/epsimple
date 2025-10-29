package com.eps.module.api.epsone.activitywork.controller;

import com.eps.module.api.epsone.activitywork.dto.ActivityWorkRequestDto;
import com.eps.module.api.epsone.activitywork.dto.ActivityWorkResponseDto;
import com.eps.module.api.epsone.activitywork.service.ActivityWorkService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/activity-works")
@RequiredArgsConstructor
public class ActivityWorkController {

    private final ActivityWorkService activityWorkService;

    @PostMapping
    public ResponseEntity<ApiResponse<ActivityWorkResponseDto>> createActivityWork(
            @Valid @RequestBody ActivityWorkRequestDto requestDto) {
        log.info("POST /api/activity-works - Creating activity work");
        ActivityWorkResponseDto responseDto = activityWorkService.createActivityWork(requestDto);
        return ResponseBuilder.success(responseDto, "Activity work created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ActivityWorkResponseDto>>> getAllActivityWorks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/activity-works - Fetching all activity works");
        Page<ActivityWorkResponseDto> activityWorks = activityWorkService.getAllActivityWorks(page, size, sortBy, sortOrder);
        return ResponseBuilder.success(activityWorks, "Activity works fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ActivityWorkResponseDto>>> searchActivityWorks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/activity-works/search - Searching activity works with keyword: {}", keyword);
        Page<ActivityWorkResponseDto> activityWorks = activityWorkService.searchActivityWorks(keyword, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(activityWorks, "Activity works search completed", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ActivityWorkResponseDto>>> getAllActivityWorksList() {
        log.info("GET /api/activity-works/list - Fetching all activity works as list");
        List<ActivityWorkResponseDto> activityWorks = activityWorkService.getAllActivityWorksList();
        return ResponseBuilder.success(activityWorks, "Activity works list fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityWorkResponseDto>> getActivityWorkById(@PathVariable Long id) {
        log.info("GET /api/activity-works/{} - Fetching activity work", id);
        ActivityWorkResponseDto responseDto = activityWorkService.getActivityWorkById(id);
        return ResponseBuilder.success(responseDto, "Activity work fetched successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityWorkResponseDto>> updateActivityWork(
            @PathVariable Long id,
            @Valid @RequestBody ActivityWorkRequestDto requestDto) {
        log.info("PUT /api/activity-works/{} - Updating activity work", id);
        ActivityWorkResponseDto responseDto = activityWorkService.updateActivityWork(id, requestDto);
        return ResponseBuilder.success(responseDto, "Activity work updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteActivityWork(@PathVariable Long id) {
        log.info("DELETE /api/activity-works/{} - Deleting activity work", id);
        activityWorkService.deleteActivityWork(id);
        return ResponseBuilder.success(null, "Activity work deleted successfully", HttpStatus.OK);
    }
}
