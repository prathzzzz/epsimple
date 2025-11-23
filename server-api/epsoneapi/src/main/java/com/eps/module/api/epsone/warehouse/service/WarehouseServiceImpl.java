package com.eps.module.api.epsone.warehouse.service;

import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.warehouse.bulk.WarehouseBulkUploadProcessor;
import com.eps.module.api.epsone.warehouse.constant.WarehouseErrorMessages;
import com.eps.module.api.epsone.warehouse.dto.WarehouseBulkUploadDto;
import com.eps.module.api.epsone.warehouse.dto.WarehouseErrorReportDto;
import com.eps.module.api.epsone.warehouse.dto.WarehouseRequestDto;
import com.eps.module.api.epsone.warehouse.dto.WarehouseResponseDto;
import com.eps.module.api.epsone.warehouse.mapper.WarehouseMapper;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.api.epsone.asset_placement.repository.AssetsOnWarehouseRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ConflictException;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl extends BaseBulkUploadService<WarehouseBulkUploadDto, Warehouse> 
        implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final LocationRepository locationRepository;
    private final WarehouseMapper warehouseMapper;
    private final AssetsOnWarehouseRepository assetsOnWarehouseRepository;
    private final WarehouseBulkUploadProcessor warehouseBulkUploadProcessor;

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<WarehouseBulkUploadDto, Warehouse> getProcessor() {
        return warehouseBulkUploadProcessor;
    }

    @Override
    public Class<WarehouseBulkUploadDto> getBulkUploadDtoClass() {
        return WarehouseBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Warehouse";
    }

    @Override
    public List<Warehouse> getAllEntitiesForExport() {
        return warehouseRepository.findAllWithLocationAndCityAndState();
    }

    @Override
    public Function<Warehouse, WarehouseBulkUploadDto> getEntityToDtoMapper() {
        return warehouse -> WarehouseBulkUploadDto.builder()
                .warehouseName(warehouse.getWarehouseName())
                .warehouseCode(warehouse.getWarehouseCode())
                .warehouseType(warehouse.getWarehouseType())
                .locationName(warehouse.getLocation().getLocationName())
                .locationAddress(warehouse.getLocation().getAddress())
                .cityName(warehouse.getLocation().getCity().getCityName())
                .stateName(warehouse.getLocation().getCity().getState().getStateName())
                .build();
    }

    @Override
    protected WarehouseErrorReportDto buildErrorReportDto(BulkUploadErrorDto error) {
        return WarehouseErrorReportDto.builder()
                .rowNumber(error.getRowNumber())
                .errorType(error.getErrorType())
                .errorMessage(error.getErrorMessage())
                .warehouseName(error.getRowData() != null ? (String) error.getRowData().get("warehouseName") : null)
                .warehouseCode(error.getRowData() != null ? (String) error.getRowData().get("warehouseCode") : null)
                .warehouseType(error.getRowData() != null ? (String) error.getRowData().get("warehouseType") : null)
                .locationName(error.getRowData() != null ? (String) error.getRowData().get("locationName") : null)
                .locationAddress(error.getRowData() != null ? (String) error.getRowData().get("locationAddress") : null)
                .cityName(error.getRowData() != null ? (String) error.getRowData().get("cityName") : null)
                .stateName(error.getRowData() != null ? (String) error.getRowData().get("stateName") : null)
                .build();
    }

    @Override
    public Class<WarehouseErrorReportDto> getErrorReportDtoClass() {
        return WarehouseErrorReportDto.class;
    }

    // ========== Existing CRUD Methods ==========

    @Override
    @Transactional
    public WarehouseResponseDto createWarehouse(WarehouseRequestDto requestDto) {
        log.info("Creating new warehouse: {}", requestDto.getWarehouseName());
        
        // Validate location exists
        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        WarehouseErrorMessages.LOCATION_NOT_FOUND_ID + requestDto.getLocationId()));

        // Validate unique warehouse code if provided
        if (requestDto.getWarehouseCode() != null && !requestDto.getWarehouseCode().isEmpty() &&
            warehouseRepository.existsByWarehouseCode(requestDto.getWarehouseCode())) {
            throw new ConflictException(String.format(
                    WarehouseErrorMessages.WAREHOUSE_CODE_EXISTS, requestDto.getWarehouseCode()));
        }

        Warehouse warehouse = warehouseMapper.toEntity(requestDto, location);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        // Fetch with details for response
        Warehouse warehouseWithDetails = warehouseRepository.findByIdWithDetails(savedWarehouse.getId())
                .orElseThrow(() -> new ResourceNotFoundException(WarehouseErrorMessages.WAREHOUSE_NOT_FOUND_AFTER_SAVE));

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
                .orElseThrow(() -> new ResourceNotFoundException(WarehouseErrorMessages.WAREHOUSE_NOT_FOUND_ID + id));
        return warehouseMapper.toDto(warehouse);
    }

    @Override
    @Transactional
    public WarehouseResponseDto updateWarehouse(Long id, WarehouseRequestDto requestDto) {
        log.info("Updating warehouse with ID: {}", id);
        
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WarehouseErrorMessages.WAREHOUSE_NOT_FOUND_ID + id));

        // Validate location exists
        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        WarehouseErrorMessages.LOCATION_NOT_FOUND_ID + requestDto.getLocationId()));

        // Validate unique warehouse code if provided and changed
        if (requestDto.getWarehouseCode() != null && !requestDto.getWarehouseCode().isEmpty() &&
            warehouseRepository.existsByWarehouseCodeAndIdNot(requestDto.getWarehouseCode(), id)) {
            throw new ConflictException(String.format(
                    WarehouseErrorMessages.WAREHOUSE_CODE_EXISTS, requestDto.getWarehouseCode()));
        }

        warehouseMapper.updateEntity(warehouse, requestDto, location);
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);

        // Fetch with details for response
        Warehouse warehouseWithDetails = warehouseRepository.findByIdWithDetails(updatedWarehouse.getId())
                .orElseThrow(() -> new ResourceNotFoundException(WarehouseErrorMessages.WAREHOUSE_NOT_FOUND_AFTER_UPDATE));

        log.info("Warehouse updated successfully with ID: {}", id);
        return warehouseMapper.toDto(warehouseWithDetails);
    }

    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        log.info("Deleting warehouse with ID: {}", id);
        
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(WarehouseErrorMessages.WAREHOUSE_NOT_FOUND_ID + id));

        // Check if warehouse has assets
        long assetCount = assetsOnWarehouseRepository.countByWarehouseId(id);
        if (assetCount > 0) {
            throw new ConflictException(String.format(
                WarehouseErrorMessages.CANNOT_DELETE_WAREHOUSE_ASSETS,
                warehouse.getWarehouseName(), assetCount, assetCount > 1 ? "s" : ""
            ));
        }

        warehouseRepository.delete(warehouse);
        log.info("Warehouse deleted successfully with ID: {}", id);
    }
}
