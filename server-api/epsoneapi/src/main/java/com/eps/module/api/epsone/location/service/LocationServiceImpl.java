package com.eps.module.api.epsone.location.service;

import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.api.epsone.location.dto.LocationBulkUploadDto;
import com.eps.module.api.epsone.location.dto.LocationRequestDto;
import com.eps.module.api.epsone.location.dto.LocationResponseDto;
import com.eps.module.api.epsone.location.mapper.LocationMapper;
import com.eps.module.api.epsone.location.processor.LocationBulkUploadProcessor;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.api.epsone.data_center.repository.DatacenterRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.dto.LocationErrorReportDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.location.City;
import com.eps.module.location.Location;
import com.eps.module.warehouse.Warehouse;
import com.eps.module.warehouse.Datacenter;
import com.eps.module.site.Site;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl extends BaseBulkUploadService<LocationBulkUploadDto, Location> implements LocationService {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;
    private final WarehouseRepository warehouseRepository;
    private final DatacenterRepository datacenterRepository;
    private final SiteRepository siteRepository;
    private final LocationMapper locationMapper;
    private final LocationBulkUploadProcessor bulkUploadProcessor;

    @Override
    public LocationResponseDto createLocation(LocationRequestDto locationRequestDto) {
        log.info("Creating new location: {}", locationRequestDto.getLocationName());

        // Validate city exists
        City city = cityRepository.findById(locationRequestDto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "City not found with ID: " + locationRequestDto.getCityId()));

        Location location = locationMapper.toEntity(locationRequestDto);
        location.setCity(city);
        
        Location savedLocation = locationRepository.save(location);
        log.info("Location created successfully with ID: {}", savedLocation.getId());
        
        return locationMapper.toResponseDto(savedLocation);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponseDto getLocationById(Long id) {
        log.info("Fetching location with ID: {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));
        return locationMapper.toResponseDto(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocationResponseDto> getAllLocations(Pageable pageable) {
        log.info("Fetching all locations with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<Location> locations = locationRepository.findAll(pageable);
        return locations.map(locationMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocationResponseDto> searchLocations(String searchTerm, Pageable pageable) {
        log.info("Searching locations with keyword: {}", searchTerm);
        Page<Location> locations = locationRepository.searchLocations(searchTerm, pageable);
        return locations.map(locationMapper::toResponseDto);
    }

    @Override
    public LocationResponseDto updateLocation(Long id, LocationRequestDto locationRequestDto) {
        log.info("Updating location with ID: {}", id);

        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));

        // Validate city exists if cityId is being updated
        if (!existingLocation.getCity().getId().equals(locationRequestDto.getCityId())) {
            City city = cityRepository.findById(locationRequestDto.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "City not found with ID: " + locationRequestDto.getCityId()));
            existingLocation.setCity(city);
        }

        locationMapper.updateEntityFromDto(locationRequestDto, existingLocation);
        Location updatedLocation = locationRepository.save(existingLocation);
        
        log.info("Location updated successfully with ID: {}", id);
        return locationMapper.toResponseDto(updatedLocation);
    }

    @Override
    public void deleteLocation(Long id) {
        log.info("Deleting location with ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));

        // Check if this location is being used by any sites
        log.debug("Checking for dependent sites for location ID: {}", id);
        Page<Site> dependentSites = siteRepository.findByLocationId(id, PageRequest.of(0, 6));
        log.debug("Found {} dependent sites", dependentSites.getTotalElements());
        
        if (!dependentSites.isEmpty()) {
            long totalCount = dependentSites.getTotalElements();
            List<String> siteNames = dependentSites.getContent().stream()
                    .limit(5)
                    .map(Site::getSiteCode)
                    .collect(Collectors.toList());
            
            String siteNamesList = String.join(", ", siteNames);
            String errorMessage = String.format(
                    "Cannot delete '%s' location because it is being used by %d site%s: %s%s. Please delete or reassign these sites first.",
                    location.getLocationName(),
                    totalCount,
                    totalCount > 1 ? "s" : "",
                    siteNamesList,
                    totalCount > 5 ? " and " + (totalCount - 5) + " more" : ""
            );
            
            log.warn("Attempted to delete location '{}' which is referenced by {} sites", location.getLocationName(), totalCount);
            throw new IllegalStateException(errorMessage);
        }

        // Check if this location is being used by any warehouses
        log.debug("Checking for dependent warehouses for location ID: {}", id);
        Page<Warehouse> dependentWarehouses = warehouseRepository.findByLocationId(id, PageRequest.of(0, 6));
        log.debug("Found {} dependent warehouses", dependentWarehouses.getTotalElements());
        
        if (!dependentWarehouses.isEmpty()) {
            long totalCount = dependentWarehouses.getTotalElements();
            List<String> warehouseNames = dependentWarehouses.getContent().stream()
                    .limit(5)
                    .map(Warehouse::getWarehouseName)
                    .collect(Collectors.toList());
            
            String warehouseNamesList = String.join(", ", warehouseNames);
            String errorMessage = String.format(
                    "Cannot delete '%s' location because it is being used by %d warehouse%s: %s%s. Please delete or reassign these warehouses first.",
                    location.getLocationName(),
                    totalCount,
                    totalCount > 1 ? "s" : "",
                    warehouseNamesList,
                    totalCount > 5 ? " and " + (totalCount - 5) + " more" : ""
            );
            
            log.warn("Attempted to delete location '{}' which is referenced by {} warehouses", location.getLocationName(), totalCount);
            throw new IllegalStateException(errorMessage);
        }

        // Check if this location is being used by any datacenters
        log.debug("Checking for dependent datacenters for location ID: {}", id);
        Page<Datacenter> dependentDatacenters = datacenterRepository.findByLocationId(id, PageRequest.of(0, 6));
        log.debug("Found {} dependent datacenters", dependentDatacenters.getTotalElements());
        
        if (!dependentDatacenters.isEmpty()) {
            long totalCount = dependentDatacenters.getTotalElements();
            List<String> datacenterNames = dependentDatacenters.getContent().stream()
                    .limit(5)
                    .map(Datacenter::getDatacenterName)
                    .collect(Collectors.toList());
            
            String datacenterNamesList = String.join(", ", datacenterNames);
            String errorMessage = String.format(
                    "Cannot delete '%s' location because it is being used by %d datacenter%s: %s%s. Please delete or reassign these datacenters first.",
                    location.getLocationName(),
                    totalCount,
                    totalCount > 1 ? "s" : "",
                    datacenterNamesList,
                    totalCount > 5 ? " and " + (totalCount - 5) + " more" : ""
            );
            
            log.warn("Attempted to delete location '{}' which is referenced by {} datacenters", location.getLocationName(), totalCount);
            throw new IllegalStateException(errorMessage);
        }

        locationRepository.delete(location);
        log.info("Location deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponseDto> getAllLocationsList() {
        log.info("Fetching all locations as list");
        List<Location> locations = locationRepository.findAllList();
        return locations.stream()
                .map(locationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ========== Bulk Upload Implementation ==========

    @Override
    protected BulkUploadProcessor<LocationBulkUploadDto, Location> getProcessor() {
        return bulkUploadProcessor;
    }

    @Override
    public Class<LocationBulkUploadDto> getBulkUploadDtoClass() {
        return LocationBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Location";
    }

    @Override
    public List<Location> getAllEntitiesForExport() {
        return locationRepository.findAllWithCityAndState();
    }

    @Override
    public Function<Location, LocationBulkUploadDto> getEntityToDtoMapper() {
        return location -> LocationBulkUploadDto.builder()
                .locationName(location.getLocationName())
                .address(location.getAddress())
                .district(location.getDistrict())
                .cityName(location.getCity().getCityName())
                .cityCode(location.getCity().getCityCode())
                .stateName(location.getCity().getState().getStateName())
                .stateCode(location.getCity().getState().getStateCode())
                .pincode(location.getPincode())
                .region(location.getRegion())
                .zone(location.getZone())
                .longitude(location.getLongitude() != null ? location.getLongitude().toString() : null)
                .latitude(location.getLatitude() != null ? location.getLatitude().toString() : null)
                .build();
    }

    @Override
    protected LocationErrorReportDto buildErrorReportDto(BulkUploadErrorDto error) {
        return LocationErrorReportDto.builder()
                .rowNumber(error.getRowNumber())
                .errorType(error.getErrorType())
                .errorMessage(error.getErrorMessage())
                .locationName(error.getRowData() != null ? (String) error.getRowData().get("locationName") : null)
                .address(error.getRowData() != null ? (String) error.getRowData().get("address") : null)
                .district(error.getRowData() != null ? (String) error.getRowData().get("district") : null)
                .cityName(error.getRowData() != null ? (String) error.getRowData().get("cityName") : null)
                .cityCode(error.getRowData() != null ? (String) error.getRowData().get("cityCode") : null)
                .stateName(error.getRowData() != null ? (String) error.getRowData().get("stateName") : null)
                .stateCode(error.getRowData() != null ? (String) error.getRowData().get("stateCode") : null)
                .pincode(error.getRowData() != null ? (String) error.getRowData().get("pincode") : null)
                .region(error.getRowData() != null ? (String) error.getRowData().get("region") : null)
                .zone(error.getRowData() != null ? (String) error.getRowData().get("zone") : null)
                .longitude(error.getRowData() != null ? (String) error.getRowData().get("longitude") : null)
                .latitude(error.getRowData() != null ? (String) error.getRowData().get("latitude") : null)
                .build();
    }

    @Override
    public Class<LocationErrorReportDto> getErrorReportDtoClass() {
        return LocationErrorReportDto.class;
    }
}
