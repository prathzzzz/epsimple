package com.eps.module.api.epsone.city.service;

import com.eps.module.api.epsone.city.constant.CityErrorMessages;
import com.eps.module.api.epsone.city.dto.CityBulkUploadDto;
import com.eps.module.api.epsone.city.dto.CityRequestDto;
import com.eps.module.api.epsone.city.dto.CityResponseDto;
import com.eps.module.api.epsone.city.mapper.CityMapper;
import com.eps.module.api.epsone.city.processor.CityBulkUploadProcessor;
import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.state.repository.StateRepository;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.api.epsone.city.dto.CityErrorReportDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.location.City;
import com.eps.module.location.Location;
import com.eps.module.location.State;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl extends BaseBulkUploadService<CityBulkUploadDto, City> implements CityService {

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final LocationRepository locationRepository;
    private final CityMapper cityMapper;
    private final CityBulkUploadProcessor cityBulkUploadProcessor;

    // Bulk upload implementation methods
    @Override
    protected BulkUploadProcessor<CityBulkUploadDto, City> getProcessor() {
        return cityBulkUploadProcessor;
    }

    @Override
    public Class<CityBulkUploadDto> getBulkUploadDtoClass() {
        return CityBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "City";
    }

    @Override
    public List<City> getAllEntitiesForExport() {
        return cityRepository.findAllCitiesList();
    }

    @Override
    public Function<City, CityBulkUploadDto> getEntityToDtoMapper() {
        return city -> CityBulkUploadDto.builder()
                .cityName(city.getCityName())
                .cityCode(city.getCityCode())
                .stateCode(city.getState() != null ? city.getState().getStateCode() : null)
                .build();
    }

    @Override
    protected CityErrorReportDto buildErrorReportDto(BulkUploadErrorDto error) {
        return CityErrorReportDto.builder()
                .rowNumber(error.getRowNumber())
                .errorType(error.getErrorType())
                .errorMessage(error.getErrorMessage())
                .cityName(error.getRowData() != null ? (String) error.getRowData().get("cityName") : null)
                .cityCode(error.getRowData() != null ? (String) error.getRowData().get("cityCode") : null)
                .stateCode(error.getRowData() != null ? (String) error.getRowData().get("stateCode") : null)
                .build();
    }

    @Override
    public Class<CityErrorReportDto> getErrorReportDtoClass() {
        return CityErrorReportDto.class;
    }

    @Override
    @Transactional
    public CityResponseDto createCity(CityRequestDto requestDto) {
        log.info("Creating city with name: {}", requestDto.getCityName());

        // Validate state exists
        State state = stateRepository.findById(requestDto.getStateId())
                .orElseThrow(() -> new ResourceNotFoundException(CityErrorMessages.STATE_NOT_FOUND_ID + requestDto.getStateId()));

        City city = cityMapper.toEntity(requestDto);
        city.setState(state);

        City savedCity = cityRepository.save(city);
        log.info("City created successfully with ID: {}", savedCity.getId());

        return cityMapper.toResponseDto(savedCity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CityResponseDto> getAllCities(Pageable pageable) {
        log.info("Fetching all cities with pagination");
        Page<City> cities = cityRepository.findAllWithState(pageable);
        return cities.map(cityMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CityResponseDto> searchCities(String searchTerm, Pageable pageable) {
        log.info("Searching cities with term: {}", searchTerm);
        Page<City> cities = cityRepository.searchCities(searchTerm, pageable);
        return cities.map(cityMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CityResponseDto> getCitiesByState(Long stateId, Pageable pageable) {
        log.info("Fetching cities for state ID: {}", stateId);
        
        // Validate state exists
        stateRepository.findById(stateId)
                .orElseThrow(() -> new ResourceNotFoundException(CityErrorMessages.STATE_NOT_FOUND_ID + stateId));

        Page<City> cities = cityRepository.findByStateId(stateId, pageable);
        return cities.map(cityMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityResponseDto> getCityList() {
        log.info("Fetching all cities as list");
        List<City> cities = cityRepository.findAllCitiesList();
        return cities.stream()
                .map(cityMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CityResponseDto getCityById(Long id) {
        log.info("Fetching city with ID: {}", id);
        City city = cityRepository.findByIdWithState(id)
                .orElseThrow(() -> new ResourceNotFoundException(CityErrorMessages.CITY_NOT_FOUND_ID + id));
        return cityMapper.toResponseDto(city);
    }

    @Override
    @Transactional
    public CityResponseDto updateCity(Long id, CityRequestDto requestDto) {
        log.info("Updating city with ID: {}", id);

        City city = cityRepository.findByIdWithState(id)
                .orElseThrow(() -> new ResourceNotFoundException(CityErrorMessages.CITY_NOT_FOUND_ID + id));

        // Validate state exists
        State state = stateRepository.findById(requestDto.getStateId())
                .orElseThrow(() -> new ResourceNotFoundException(CityErrorMessages.STATE_NOT_FOUND_ID + requestDto.getStateId()));

        cityMapper.updateEntityFromDto(requestDto, city);
        city.setState(state);

        City updatedCity = cityRepository.save(city);
        log.info("City updated successfully with ID: {}", updatedCity.getId());

        return cityMapper.toResponseDto(updatedCity);
    }

    @Override
    @Transactional
    public void deleteCity(Long id) {
        log.info("Deleting city with ID: {}", id);

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CityErrorMessages.CITY_NOT_FOUND_ID + id));

        // Check if this city is being used by any locations
        log.debug("Checking for dependent locations for city ID: {}", id);
        Page<Location> dependentLocations = locationRepository.findByCityId(id, PageRequest.of(0, 6));
        log.debug("Found {} dependent locations", dependentLocations.getTotalElements());
        
        if (!dependentLocations.isEmpty()) {
            long totalCount = dependentLocations.getTotalElements();
            List<String> locationNames = dependentLocations.getContent().stream()
                    .limit(5)
                    .map(Location::getLocationName)
                    .collect(Collectors.toList());
            
            String locationNamesList = String.join(", ", locationNames);
            String errorMessage = String.format(
                    CityErrorMessages.CANNOT_DELETE_CITY_LOCATIONS,
                    city.getCityName(),
                    totalCount,
                    totalCount > 1 ? "s" : "",
                    locationNamesList,
                    totalCount > 5 ? " and " + (totalCount - 5) + " more" : ""
            );
            
            log.warn("Attempted to delete city '{}' which is referenced by {} locations", city.getCityName(), totalCount);
            throw new ConflictException(errorMessage);
        }

        cityRepository.delete(city);
        log.info("City deleted successfully with ID: {}", id);
    }
}
