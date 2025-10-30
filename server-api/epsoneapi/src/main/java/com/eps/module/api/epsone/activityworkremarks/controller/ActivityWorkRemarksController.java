package com.eps.module.api.epsone.activityworkremarks.controller;

import com.eps.module.api.epsone.activityworkremarks.dto.ActivityWorkRemarksRequestDto;
import com.eps.module.api.epsone.activityworkremarks.dto.ActivityWorkRemarksResponseDto;
import com.eps.module.api.epsone.activityworkremarks.service.ActivityWorkRemarksService;
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
@RequestMapping("/api/activity-work-remarks")
@RequiredArgsConstructor
public class ActivityWorkRemarksController {

    private final ActivityWorkRemarksService remarksService;

    @PostMapping
    public ResponseEntity<ApiResponse<ActivityWorkRemarksResponseDto>> createRemark(
            @Valid @RequestBody ActivityWorkRemarksRequestDto requestDto) {
        ActivityWorkRemarksResponseDto response = remarksService.createRemark(requestDto);
        return ResponseBuilder.success(response, "Remark created successfully");
    }

    @GetMapping("/activity-work/{activityWorkId}")
    public ResponseEntity<ApiResponse<Page<ActivityWorkRemarksResponseDto>>> getRemarksByActivityWorkId(
            @PathVariable Long activityWorkId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "commentedOn") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ActivityWorkRemarksResponseDto> remarks = remarksService.getRemarksByActivityWorkId(activityWorkId, pageable);
        return ResponseBuilder.success(remarks, "Remarks retrieved successfully");
    }

    @GetMapping("/activity-work/{activityWorkId}/list")
    public ResponseEntity<ApiResponse<List<ActivityWorkRemarksResponseDto>>> getAllRemarksByActivityWorkId(
            @PathVariable Long activityWorkId) {
        List<ActivityWorkRemarksResponseDto> remarks = remarksService.getAllRemarksByActivityWorkId(activityWorkId);
        return ResponseBuilder.success(remarks, "All remarks retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityWorkRemarksResponseDto>> getRemarkById(@PathVariable Long id) {
        ActivityWorkRemarksResponseDto remark = remarksService.getRemarkById(id);
        return ResponseBuilder.success(remark, "Remark retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityWorkRemarksResponseDto>> updateRemark(
            @PathVariable Long id,
            @Valid @RequestBody ActivityWorkRemarksRequestDto requestDto) {
        ActivityWorkRemarksResponseDto response = remarksService.updateRemark(id, requestDto);
        return ResponseBuilder.success(response, "Remark updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRemark(@PathVariable Long id) {
        remarksService.deleteRemark(id);
        return ResponseBuilder.success(null, "Remark deleted successfully");
    }

    @GetMapping("/activity-work/{activityWorkId}/count")
    public ResponseEntity<ApiResponse<Long>> countRemarksByActivityWorkId(@PathVariable Long activityWorkId) {
        long count = remarksService.countRemarksByActivityWorkId(activityWorkId);
        return ResponseBuilder.success(count, "Remark count retrieved successfully");
    }
}
