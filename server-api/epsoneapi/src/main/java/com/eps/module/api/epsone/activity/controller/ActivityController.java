package com.eps.module.api.epsone.activity.controller;

import com.eps.module.api.epsone.activity.dto.ActivityRequestDto;
import com.eps.module.api.epsone.activity.dto.ActivityResponseDto;
import com.eps.module.api.epsone.activity.service.ActivityService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> getActivityById(@PathVariable Long id) {
        ActivityResponseDto activity = activityService.getActivityById(id);
        return ResponseBuilder.success(activity, "Activity retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityResponseDto>> updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody ActivityRequestDto activityRequestDto) {
        ActivityResponseDto response = activityService.updateActivity(id, activityRequestDto);
        return ResponseBuilder.success(response, "Activity updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseBuilder.success(null, "Activity deleted successfully");
    }
}
