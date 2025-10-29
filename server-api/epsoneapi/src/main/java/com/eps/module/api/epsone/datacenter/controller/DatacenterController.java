package com.eps.module.api.epsone.datacenter.controller;

import com.eps.module.api.epsone.datacenter.dto.DatacenterRequestDto;
import com.eps.module.api.epsone.datacenter.dto.DatacenterResponseDto;
import com.eps.module.api.epsone.datacenter.service.DatacenterService;
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
@RequestMapping("/api/datacenters")
@RequiredArgsConstructor
public class DatacenterController {

    private final DatacenterService datacenterService;

    @PostMapping
    public ResponseEntity<ApiResponse<DatacenterResponseDto>> createDatacenter(
            @Valid @RequestBody DatacenterRequestDto requestDto) {
        log.info("POST /api/datacenters - Creating datacenter: {}", requestDto.getDatacenterName());
        DatacenterResponseDto responseDto = datacenterService.createDatacenter(requestDto);
        return ResponseBuilder.success(responseDto, "Datacenter created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DatacenterResponseDto>>> getAllDatacenters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datacenterName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/datacenters - Fetching all datacenters");
        Page<DatacenterResponseDto> datacenters = datacenterService.getAllDatacenters(page, size, sortBy, sortOrder);
        return ResponseBuilder.success(datacenters, "Datacenters fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<DatacenterResponseDto>>> searchDatacenters(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datacenterName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        log.info("GET /api/datacenters/search - Searching datacenters with keyword: {}", keyword);
        Page<DatacenterResponseDto> datacenters = datacenterService.searchDatacenters(keyword, page, size, sortBy, sortOrder);
        return ResponseBuilder.success(datacenters, "Datacenters search completed", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<DatacenterResponseDto>>> getAllDatacentersList() {
        log.info("GET /api/datacenters/list - Fetching all datacenters as list");
        List<DatacenterResponseDto> datacenters = datacenterService.getAllDatacentersList();
        return ResponseBuilder.success(datacenters, "Datacenters list fetched successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DatacenterResponseDto>> getDatacenterById(@PathVariable Long id) {
        log.info("GET /api/datacenters/{} - Fetching datacenter", id);
        DatacenterResponseDto responseDto = datacenterService.getDatacenterById(id);
        return ResponseBuilder.success(responseDto, "Datacenter fetched successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DatacenterResponseDto>> updateDatacenter(
            @PathVariable Long id,
            @Valid @RequestBody DatacenterRequestDto requestDto) {
        log.info("PUT /api/datacenters/{} - Updating datacenter", id);
        DatacenterResponseDto responseDto = datacenterService.updateDatacenter(id, requestDto);
        return ResponseBuilder.success(responseDto, "Datacenter updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDatacenter(@PathVariable Long id) {
        log.info("DELETE /api/datacenters/{} - Deleting datacenter", id);
        datacenterService.deleteDatacenter(id);
        return ResponseBuilder.success(null, "Datacenter deleted successfully", HttpStatus.OK);
    }
}
