package com.eps.module.api.epsone.warehouse.bulk;

import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.warehouse.dto.WarehouseBulkUploadDto;
import com.eps.module.api.epsone.warehouse.repository.WarehouseRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseBulkUploadValidator implements BulkRowValidator<WarehouseBulkUploadDto> {

    private final WarehouseRepository warehouseRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<BulkUploadErrorDto> validate(WarehouseBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Required field validations
        if (isBlank(rowData.getWarehouseName())) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Warehouse Name")
                    .errorMessage("Warehouse Name is required")
                    .rejectedValue(rowData.getWarehouseName())
                    .build());
        }

        if (isBlank(rowData.getLocationName())) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Location Name")
                    .errorMessage("Location Name is required")
                    .rejectedValue(rowData.getLocationName())
                    .build());
        }

        // Length validations
        if (!isBlank(rowData.getWarehouseName()) && rowData.getWarehouseName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Warehouse Name")
                    .errorMessage("Warehouse Name must not exceed 255 characters")
                    .rejectedValue(rowData.getWarehouseName())
                    .build());
        }

        if (!isBlank(rowData.getWarehouseCode()) && rowData.getWarehouseCode().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Warehouse Code")
                    .errorMessage("Warehouse Code must not exceed 50 characters")
                    .rejectedValue(rowData.getWarehouseCode())
                    .build());
        }

        if (!isBlank(rowData.getWarehouseType()) && rowData.getWarehouseType().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Warehouse Type")
                    .errorMessage("Warehouse Type must not exceed 100 characters")
                    .rejectedValue(rowData.getWarehouseType())
                    .build());
        }

        if (!isBlank(rowData.getLocationName()) && rowData.getLocationName().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Location Name")
                    .errorMessage("Location Name must not exceed 100 characters")
                    .rejectedValue(rowData.getLocationName())
                    .build());
        }

        // Foreign key validation
        if (!isBlank(rowData.getLocationName())) {
            var locationOpt = locationRepository.findByLocationName(rowData.getLocationName());
            if (locationOpt.isEmpty()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Location Name")
                        .errorMessage("Location '" + rowData.getLocationName() + "' not found in system")
                        .rejectedValue(rowData.getLocationName())
                        .build());
            }
        }

        // Format validation for warehouse code
        if (!isBlank(rowData.getWarehouseCode()) && !rowData.getWarehouseCode().matches("^[A-Z0-9_-]*$")) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Warehouse Code")
                    .errorMessage("Warehouse Code must contain only uppercase letters, numbers, hyphens, and underscores")
                    .rejectedValue(rowData.getWarehouseCode())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(WarehouseBulkUploadDto rowData) {
        // Check duplicate by warehouse code if provided
        if (!isBlank(rowData.getWarehouseCode())) {
            return warehouseRepository.findByWarehouseCode(rowData.getWarehouseCode()).isPresent();
        }
        
        // Check duplicate by warehouse name
        if (!isBlank(rowData.getWarehouseName())) {
            return warehouseRepository.findByWarehouseName(rowData.getWarehouseName()).isPresent();
        }
        
        return false;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
