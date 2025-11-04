package com.eps.module.api.epsone.data_center.mapper;

import com.eps.module.api.epsone.data_center.dto.DatacenterRequestDto;
import com.eps.module.api.epsone.data_center.dto.DatacenterResponseDto;
import com.eps.module.location.Location;
import com.eps.module.warehouse.Datacenter;
import org.springframework.stereotype.Component;

@Component
public class DatacenterMapper {

    public Datacenter toEntity(DatacenterRequestDto dto, Location location) {
        return Datacenter.builder()
                .datacenterName(dto.getDatacenterName())
                .datacenterCode(dto.getDatacenterCode())
                .datacenterType(dto.getDatacenterType())
                .location(location)
                .build();
    }

    public void updateEntity(Datacenter datacenter, DatacenterRequestDto dto, Location location) {
        datacenter.setDatacenterName(dto.getDatacenterName());
        datacenter.setDatacenterCode(dto.getDatacenterCode());
        datacenter.setDatacenterType(dto.getDatacenterType());
        datacenter.setLocation(location);
    }

    public DatacenterResponseDto toDto(Datacenter datacenter) {
        return DatacenterResponseDto.builder()
                .id(datacenter.getId())
                .datacenterName(datacenter.getDatacenterName())
                .datacenterCode(datacenter.getDatacenterCode())
                .datacenterType(datacenter.getDatacenterType())
                .locationId(datacenter.getLocation().getId())
                .locationName(datacenter.getLocation().getLocationName())
                .cityName(datacenter.getLocation().getCity().getCityName())
                .stateName(datacenter.getLocation().getCity().getState().getStateName())
                .createdAt(datacenter.getCreatedAt())
                .updatedAt(datacenter.getUpdatedAt())
                .build();
    }
}
