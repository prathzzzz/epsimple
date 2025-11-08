package com.eps.module.api.epsone.warehouse.bulk;

import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.warehouse.dto.WarehouseBulkUploadDto;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.location.Location;
import com.eps.module.warehouse.Warehouse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseBulkUploadProcessor extends BulkUploadProcessor<WarehouseBulkUploadDto, Warehouse> {

    private final WarehouseBulkUploadValidator validator;
    private final WarehouseRepository warehouseRepository;
    private final LocationRepository locationRepository;

    @Override
    protected BulkRowValidator<WarehouseBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected Warehouse convertToEntity(WarehouseBulkUploadDto dto) {
        // Look up location with city and state eagerly fetched
        Location location = locationRepository.findByLocationNameWithCityAndState(dto.getLocationName())
                .orElseThrow(() -> new IllegalArgumentException("Location not found: " + dto.getLocationName()));

        return Warehouse.builder()
                .warehouseName(dto.getWarehouseName())
                .warehouseCode(dto.getWarehouseCode())
                .warehouseType(dto.getWarehouseType())
                .location(location)
                .build();
    }

    @Override
    protected void saveEntity(Warehouse entity) {
        warehouseRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(WarehouseBulkUploadDto dto) {
        Map<String, Object> data = new HashMap<>();
        data.put("warehouseName", dto.getWarehouseName());
        data.put("warehouseCode", dto.getWarehouseCode());
        data.put("warehouseType", dto.getWarehouseType());
        data.put("locationName", dto.getLocationName());
        
        // Look up location with city and state to get additional data for error report
        if (dto.getLocationName() != null) {
            locationRepository.findByLocationNameWithCityAndState(dto.getLocationName()).ifPresent(location -> {
                data.put("locationAddress", location.getAddress());
                data.put("cityName", location.getCity().getCityName());
                data.put("stateName", location.getCity().getState().getStateName());
            });
        }
        
        return data;
    }

    @Override
    protected boolean isEmptyRow(WarehouseBulkUploadDto dto) {
        return (dto.getWarehouseName() == null || dto.getWarehouseName().trim().isEmpty()) &&
               (dto.getWarehouseCode() == null || dto.getWarehouseCode().trim().isEmpty()) &&
               (dto.getLocationName() == null || dto.getLocationName().trim().isEmpty());
    }
}
