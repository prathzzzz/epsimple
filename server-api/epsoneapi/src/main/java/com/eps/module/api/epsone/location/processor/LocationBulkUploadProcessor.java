package com.eps.module.api.epsone.location.processor;

import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.api.epsone.location.dto.LocationBulkUploadDto;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.location.validator.LocationBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.location.City;
import com.eps.module.location.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationBulkUploadProcessor extends BulkUploadProcessor<LocationBulkUploadDto, Location> {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;
    private final LocationBulkUploadValidator validator;

    @Override
    protected BulkRowValidator<LocationBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected Location convertToEntity(LocationBulkUploadDto dto) {
        // Find city by name
        City city = cityRepository.findByCityName(dto.getCityName())
                .orElseThrow(() -> new IllegalStateException("City not found: " + dto.getCityName()));

        Location location = Location.builder()
                .locationName(dto.getLocationName())
                .address(dto.getAddress())
                .district(dto.getDistrict())
                .city(city)
                .pincode(dto.getPincode())
                .region(dto.getRegion())
                .zone(dto.getZone())
                .build();

        // Parse longitude if provided
        if (dto.getLongitude() != null && !dto.getLongitude().trim().isEmpty()) {
            try {
                location.setLongitude(new BigDecimal(dto.getLongitude()));
            } catch (NumberFormatException e) {
                log.warn("Invalid longitude format for location: {}", dto.getLocationName());
            }
        }

        // Parse latitude if provided
        if (dto.getLatitude() != null && !dto.getLatitude().trim().isEmpty()) {
            try {
                location.setLatitude(new BigDecimal(dto.getLatitude()));
            } catch (NumberFormatException e) {
                log.warn("Invalid latitude format for location: {}", dto.getLocationName());
            }
        }

        return location;
    }

    @Override
    protected void saveEntity(Location entity) {
        locationRepository.save(entity);
    }

    @Override
    protected boolean isEmptyRow(LocationBulkUploadDto dto) {
        return (dto.getLocationName() == null || dto.getLocationName().trim().isEmpty()) &&
               (dto.getCityName() == null || dto.getCityName().trim().isEmpty());
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(LocationBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("locationName", dto.getLocationName());
        rowData.put("address", dto.getAddress());
        rowData.put("district", dto.getDistrict());
        rowData.put("cityName", dto.getCityName());
        rowData.put("pincode", dto.getPincode());
        rowData.put("region", dto.getRegion());
        rowData.put("zone", dto.getZone());
        rowData.put("longitude", dto.getLongitude());
        rowData.put("latitude", dto.getLatitude());

        // Lookup city details for error report
        if (dto.getCityName() != null && !dto.getCityName().trim().isEmpty()) {
            cityRepository.findByCityName(dto.getCityName()).ifPresent(city -> {
                rowData.put("cityCode", city.getCityCode());
                rowData.put("stateName", city.getState().getStateName());
                rowData.put("stateCode", city.getState().getStateCode());
            });
        }

        return rowData;
    }
}
