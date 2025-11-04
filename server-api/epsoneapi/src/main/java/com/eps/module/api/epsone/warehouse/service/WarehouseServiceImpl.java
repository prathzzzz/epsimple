package com.eps.module.api.epsone.warehouse.service;

import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.warehouse.dto.WarehouseRequestDto;
import com.eps.module.api.epsone.warehouse.dto.WarehouseResponseDto;
import com.eps.module.api.epsone.warehouse.mapper.WarehouseMapper;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.location.Location;
import com.eps.module.warehouse.Warehouse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private static final String WAREHOUSE_NOT_FOUND_WITH_ID_MSG = "Warehouse not found with id: ";

    private final WarehouseRepository warehouseRepository;
    private final LocationRepository locationRepository;
    private final WarehouseMapper warehouseMapper;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;

    @Override
    @Transactional
    public WarehouseResponseDto createWarehouse(WarehouseRequestDto requestDto) {
        log.info("Creating new warehouse: {}", requestDto.getWarehouseName());
        
        // Validate location exists
        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Location not found with id: " + requestDto.getLocationId()));

        // Validate unique warehouse code if provided
        if (requestDto.getWarehouseCode() != null && !requestDto.getWarehouseCode().isEmpty() &&
            warehouseRepository.existsByWarehouseCode(requestDto.getWarehouseCode())) {
            throw new IllegalArgumentException(
                    "Warehouse code '" + requestDto.getWarehouseCode() + "' already exists");
        }

        Warehouse warehouse = warehouseMapper.toEntity(requestDto, location);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        // Fetch with details for response
        Warehouse warehouseWithDetails = warehouseRepository.findByIdWithDetails(savedWarehouse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found after save"));

        log.info("Warehouse created successfully with ID: {}", savedWarehouse.getId());
        return warehouseMapper.toDto(warehouseWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseResponseDto> getAllWarehouses(int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching all warehouses with pagination: page={}, size={}", page, size);
        Sort sort = sortOrder.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Warehouse> warehousePage = warehouseRepository.findAllWithDetails(pageable);
        return warehousePage.map(warehouseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseResponseDto> searchWarehouses(String searchTerm, int page, int size, String sortBy, String sortOrder) {
        log.info("Searching warehouses with keyword: {}", searchTerm);
        Sort sort = sortOrder.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Warehouse> warehousePage = warehouseRepository.searchWarehouses(searchTerm, pageable);
        return warehousePage.map(warehouseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseResponseDto> getAllWarehousesList() {
        log.info("Fetching all warehouses as list");
        List<Warehouse> warehouses = warehouseRepository.findAll(Sort.by("warehouseName").ascending());
        return warehouses.stream()
                .map(warehouseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponseDto getWarehouseById(Long id) {
        log.info("Fetching warehouse with ID: {}", id);
        Warehouse warehouse = warehouseRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(WAREHOUSE_NOT_FOUND_WITH_ID_MSG + id));
        return warehouseMapper.toDto(warehouse);
    }

    @Override
    @Transactional
    public WarehouseResponseDto updateWarehouse(Long id, WarehouseRequestDto requestDto) {
        log.info("Updating warehouse with ID: {}", id);
        
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WAREHOUSE_NOT_FOUND_WITH_ID_MSG + id));

        // Validate location exists
        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Location not found with id: " + requestDto.getLocationId()));

        // Validate unique warehouse code if provided and changed
        if (requestDto.getWarehouseCode() != null && !requestDto.getWarehouseCode().isEmpty() &&
            warehouseRepository.existsByWarehouseCodeAndIdNot(requestDto.getWarehouseCode(), id)) {
            throw new IllegalArgumentException(
                    "Warehouse code '" + requestDto.getWarehouseCode() + "' already exists");
        }

        warehouseMapper.updateEntity(warehouse, requestDto, location);
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);

        // Fetch with details for response
        Warehouse warehouseWithDetails = warehouseRepository.findByIdWithDetails(updatedWarehouse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found after update"));

        log.info("Warehouse updated successfully with ID: {}", id);
        return warehouseMapper.toDto(warehouseWithDetails);
    }

    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        log.info("Deleting warehouse with ID: {}", id);
        
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WAREHOUSE_NOT_FOUND_WITH_ID_MSG + id));

        // Check if warehouse has assets
        long assetCount = assetsOnWarehouseRepository.countByWarehouseId(id);
        if (assetCount > 0) {
            throw new IllegalStateException(String.format(
                "Cannot delete warehouse '%s' because it has %d asset%s. Please remove the assets from this warehouse first.",
                warehouse.getWarehouseName(), assetCount, assetCount > 1 ? "s" : ""
            ));
        }

        warehouseRepository.delete(warehouse);
        log.info("Warehouse deleted successfully with ID: {}", id);
    }
}
