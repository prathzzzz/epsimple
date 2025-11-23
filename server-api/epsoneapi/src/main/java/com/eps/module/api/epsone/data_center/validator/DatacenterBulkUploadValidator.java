package com.eps.module.api.epsone.data_center.validator;

import com.eps.module.api.epsone.data_center.constant.DatacenterErrorMessages;
import com.eps.module.api.epsone.data_center.dto.DatacenterBulkUploadDto;
import com.eps.module.api.epsone.data_center.repository.DatacenterRepository;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatacenterBulkUploadValidator implements BulkRowValidator<DatacenterBulkUploadDto> {

    private final DatacenterRepository datacenterRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<BulkUploadErrorDto> validate(DatacenterBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate datacenter name (required)
        if (dto.getDatacenterName() == null || dto.getDatacenterName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(DatacenterErrorMessages.DATACENTER_NAME_REQUIRED)
                    .build());
        } else if (dto.getDatacenterName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(DatacenterErrorMessages.DATACENTER_NAME_LENGTH_EXCEEDED)
                    .build());
        }

        // Validate datacenter code (optional, but must be valid format if provided)
        if (dto.getDatacenterCode() != null && !dto.getDatacenterCode().trim().isEmpty()) {
            if (dto.getDatacenterCode().length() > 50) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(DatacenterErrorMessages.DATACENTER_CODE_LENGTH_EXCEEDED)
                        .build());
            }
            // Check format (uppercase letters, numbers, hyphens, underscores)
            if (!dto.getDatacenterCode().matches("^[A-Z0-9_-]*$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(DatacenterErrorMessages.DATACENTER_CODE_INVALID_FORMAT)
                        .build());
            }
            // Check for duplicate code
            if (datacenterRepository.existsByDatacenterCode(dto.getDatacenterCode())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(String.format(DatacenterErrorMessages.DATACENTER_CODE_EXISTS, dto.getDatacenterCode()))
                        .build());
            }
        }

        // Validate datacenter type (optional, max 100 chars)
        if (dto.getDatacenterType() != null && dto.getDatacenterType().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(DatacenterErrorMessages.DATACENTER_TYPE_LENGTH_EXCEEDED)
                    .build());
        }

        // Validate location name (required)
        if (dto.getLocationName() == null || dto.getLocationName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(DatacenterErrorMessages.LOCATION_NAME_REQUIRED)
                    .build());
        } else {
            // Check if location exists
            boolean locationExists = locationRepository.findByLocationName(dto.getLocationName()).isPresent();
            if (!locationExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(DatacenterErrorMessages.LOCATION_NOT_FOUND_NAME + dto.getLocationName())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(DatacenterBulkUploadDto dto) {
        // Check for duplicate datacenter name (case-insensitive)
        return datacenterRepository.findByDatacenterNameIgnoreCase(dto.getDatacenterName()).isPresent();
    }
}
