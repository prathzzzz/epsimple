package com.eps.module.api.epsone.data_center.processor;

import com.eps.module.api.epsone.data_center.dto.DatacenterBulkUploadDto;
import com.eps.module.api.epsone.data_center.repository.DatacenterRepository;
import com.eps.module.api.epsone.data_center.validator.DatacenterBulkUploadValidator;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.location.Location;
import com.eps.module.warehouse.Datacenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatacenterBulkUploadProcessor extends BulkUploadProcessor<DatacenterBulkUploadDto, Datacenter> {

    private final DatacenterRepository datacenterRepository;
    private final LocationRepository locationRepository;
    private final DatacenterBulkUploadValidator validator;

    @Override
    protected BulkRowValidator<DatacenterBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected Datacenter convertToEntity(DatacenterBulkUploadDto dto) {
        // Find location by name with eager fetching to avoid LazyInitializationException
        Location location = locationRepository.findByLocationNameWithCityAndState(dto.getLocationName())
                .orElseThrow(() -> new IllegalStateException("Location not found: " + dto.getLocationName()));

        return Datacenter.builder()
                .datacenterName(dto.getDatacenterName())
                .datacenterCode(dto.getDatacenterCode())
                .datacenterType(dto.getDatacenterType())
                .location(location)
                .build();
    }

    @Override
    protected void saveEntity(Datacenter entity) {
        datacenterRepository.save(entity);
    }

    @Override
    protected boolean isEmptyRow(DatacenterBulkUploadDto dto) {
        return (dto.getDatacenterName() == null || dto.getDatacenterName().trim().isEmpty()) &&
               (dto.getLocationName() == null || dto.getLocationName().trim().isEmpty());
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(DatacenterBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("datacenterName", dto.getDatacenterName());
        rowData.put("datacenterCode", dto.getDatacenterCode());
        rowData.put("datacenterType", dto.getDatacenterType());
        rowData.put("locationName", dto.getLocationName());

        // Lookup location details for error report (use eager fetch to avoid LazyInitializationException)
        if (dto.getLocationName() != null && !dto.getLocationName().trim().isEmpty()) {
            locationRepository.findByLocationNameWithCityAndState(dto.getLocationName()).ifPresent(location -> {
                rowData.put("cityName", location.getCity().getCityName());
                rowData.put("stateName", location.getCity().getState().getStateName());
            });
        }

        return rowData;
    }
}
