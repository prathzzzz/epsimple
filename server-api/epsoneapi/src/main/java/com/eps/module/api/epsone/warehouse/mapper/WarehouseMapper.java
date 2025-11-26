package com.eps.module.api.epsone.warehouse.mapper;

import com.eps.module.api.epsone.warehouse.dto.WarehouseRequestDto;
import com.eps.module.api.epsone.warehouse.dto.WarehouseResponseDto;
import com.eps.module.auth.audit.AuditUserResolver;
import com.eps.module.location.Location;
import com.eps.module.warehouse.Warehouse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarehouseMapper {

    private final AuditUserResolver auditUserResolver;

    public Warehouse toEntity(WarehouseRequestDto dto, Location location) {
        return Warehouse.builder()
                .warehouseName(dto.getWarehouseName())
                .warehouseCode(dto.getWarehouseCode())
                .warehouseType(dto.getWarehouseType())
                .location(location)
                .build();
    }

    public void updateEntity(Warehouse warehouse, WarehouseRequestDto dto, Location location) {
        warehouse.setWarehouseName(dto.getWarehouseName());
        warehouse.setWarehouseCode(dto.getWarehouseCode());
        warehouse.setWarehouseType(dto.getWarehouseType());
        warehouse.setLocation(location);
    }

    public WarehouseResponseDto toDto(Warehouse warehouse) {
        return WarehouseResponseDto.builder()
                .id(warehouse.getId())
                .warehouseName(warehouse.getWarehouseName())
                .warehouseCode(warehouse.getWarehouseCode())
                .warehouseType(warehouse.getWarehouseType())
                .locationId(warehouse.getLocation().getId())
                .locationName(warehouse.getLocation().getLocationName())
                .cityName(warehouse.getLocation().getCity().getCityName())
                .stateName(warehouse.getLocation().getCity().getState().getStateName())
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .createdBy(auditUserResolver.resolveUserName(warehouse.getCreatedBy()))
                .updatedBy(auditUserResolver.resolveUserName(warehouse.getUpdatedBy()))
                .build();
    }
}
