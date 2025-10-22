package com.eps.module.api.epsone.activities.controller;

import com.eps.module.api.epsone.activities.dto.ActivitiesRequestDto;
import com.eps.module.api.epsone.activities.dto.ActivitiesResponseDto;
import com.eps.module.api.epsone.activities.service.ActivitiesService;
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
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivitiesController {

    private final ActivitiesService activitiesService;

    @PostMapping
    public ResponseEntity<ApiResponse<ActivitiesResponseDto>> createActivities(
            @Valid @RequestBody ActivitiesRequestDto activitiesRequestDto) {
        ActivitiesResponseDto response = activitiesService.createActivities(activitiesRequestDto);
        return ResponseBuilder.success(response, "Activities created successfully");
    }

    @GetMapping
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
    public ResponseEntity<ApiResponse<List<ActivitiesResponseDto>>> getActivitiesList() {
        List<ActivitiesResponseDto> activities = activitiesService.getActivitiesList();
        return ResponseBuilder.success(activities, "Activities list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivitiesResponseDto>> getActivitiesById(@PathVariable Long id) {
        ActivitiesResponseDto activities = activitiesService.getActivitiesById(id);
        return ResponseBuilder.success(activities, "Activities retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivitiesResponseDto>> updateActivities(
            @PathVariable Long id,
            @Valid @RequestBody ActivitiesRequestDto activitiesRequestDto) {
        ActivitiesResponseDto response = activitiesService.updateActivities(id, activitiesRequestDto);
        return ResponseBuilder.success(response, "Activities updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteActivities(@PathVariable Long id) {
        activitiesService.deleteActivities(id);
        return ResponseBuilder.success(null, "Activities deleted successfully");
    }
}
