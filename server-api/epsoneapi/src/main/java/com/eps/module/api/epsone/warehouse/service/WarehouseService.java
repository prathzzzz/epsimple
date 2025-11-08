package com.eps.module.api.epsone.warehouse.service;

import com.eps.module.api.epsone.warehouse.dto.WarehouseBulkUploadDto;
import com.eps.module.api.epsone.warehouse.dto.WarehouseRequestDto;
import com.eps.module.api.epsone.warehouse.dto.WarehouseResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.warehouse.Warehouse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WarehouseService extends BulkUploadService<WarehouseBulkUploadDto, Warehouse> {

    WarehouseResponseDto createWarehouse(WarehouseRequestDto requestDto);

    Page<WarehouseResponseDto> getAllWarehouses(int page, int size, String sortBy, String sortOrder);

    Page<WarehouseResponseDto> searchWarehouses(String searchTerm, int page, int size, String sortBy, String sortOrder);

    List<WarehouseResponseDto> getAllWarehousesList();

    WarehouseResponseDto getWarehouseById(Long id);

    WarehouseResponseDto updateWarehouse(Long id, WarehouseRequestDto requestDto);

    void deleteWarehouse(Long id);
}
