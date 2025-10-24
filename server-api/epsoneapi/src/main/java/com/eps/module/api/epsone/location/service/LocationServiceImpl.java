package com.eps.module.api.epsone.location.service;

import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.api.epsone.location.dto.LocationRequestDto;
import com.eps.module.api.epsone.location.dto.LocationResponseDto;
import com.eps.module.api.epsone.location.mapper.LocationMapper;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.location.City;
import com.eps.module.location.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;
    private final LocationMapper locationMapper;

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
}
