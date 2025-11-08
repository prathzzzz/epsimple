package com.eps.module.api.epsone.location.service;

import com.eps.module.api.epsone.location.dto.LocationBulkUploadDto;
import com.eps.module.api.epsone.location.dto.LocationRequestDto;
import com.eps.module.api.epsone.location.dto.LocationResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.location.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService extends BulkUploadService<LocationBulkUploadDto, Location> {

    /**
     * Create a new location
     */
    LocationResponseDto createLocation(LocationRequestDto locationRequestDto);

    /**
     * Get location by ID
     */
    LocationResponseDto getLocationById(Long id);

    /**
     * Get all locations with pagination
     */
    Page<LocationResponseDto> getAllLocations(Pageable pageable);

    /**
     * Search locations by keyword
     */
    Page<LocationResponseDto> searchLocations(String searchTerm, Pageable pageable);

    /**
     * Update location
     */
    LocationResponseDto updateLocation(Long id, LocationRequestDto locationRequestDto);

    /**
     * Delete location
     */
    void deleteLocation(Long id);

    /**
     * Get all locations as list (no pagination) - for dropdowns
     */
    List<LocationResponseDto> getAllLocationsList();
}
