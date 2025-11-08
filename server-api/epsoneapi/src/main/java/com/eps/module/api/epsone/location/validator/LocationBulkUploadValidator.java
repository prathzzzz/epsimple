package com.eps.module.api.epsone.location.validator;

import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.api.epsone.location.dto.LocationBulkUploadDto;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LocationBulkUploadValidator implements BulkRowValidator<LocationBulkUploadDto> {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;

    @Override
    public List<BulkUploadErrorDto> validate(LocationBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate location name (required)
        if (dto.getLocationName() == null || dto.getLocationName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Location name is required")
                    .build());
        } else if (dto.getLocationName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Location name cannot exceed 255 characters")
                    .build());
        }

        // Validate address (optional, max 5000 chars)
        if (dto.getAddress() != null && dto.getAddress().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Address cannot exceed 5000 characters")
                    .build());
        }

        // Validate district (optional, max 100 chars)
        if (dto.getDistrict() != null && dto.getDistrict().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("District name cannot exceed 100 characters")
                    .build());
        }

        // Validate city name (required)
        if (dto.getCityName() == null || dto.getCityName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("City name is required")
                    .build());
        } else {
            // Check if city exists
            boolean cityExists = cityRepository.findByCityName(dto.getCityName()).isPresent();
            if (!cityExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("City not found: " + dto.getCityName())
                        .build());
            }
        }

        // Validate pincode (optional, must be 6 digits if provided)
        if (dto.getPincode() != null && !dto.getPincode().trim().isEmpty()) {
            if (!dto.getPincode().matches("^[0-9]{6}$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Pincode must be exactly 6 digits")
                        .build());
            }
        }

        // Validate region (optional, max 50 chars)
        if (dto.getRegion() != null && dto.getRegion().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Region cannot exceed 50 characters")
                    .build());
        }

        // Validate zone (optional, max 50 chars)
        if (dto.getZone() != null && dto.getZone().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Zone cannot exceed 50 characters")
                    .build());
        }

        // Validate longitude (optional)
        if (dto.getLongitude() != null && !dto.getLongitude().trim().isEmpty()) {
            try {
                BigDecimal longitude = new BigDecimal(dto.getLongitude());
                if (longitude.compareTo(new BigDecimal("-180")) < 0 || longitude.compareTo(new BigDecimal("180")) > 0) {
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .errorMessage("Longitude must be between -180 and 180")
                            .build());
                }
            } catch (NumberFormatException e) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Invalid longitude format")
                        .build());
            }
        }

        // Validate latitude (optional)
        if (dto.getLatitude() != null && !dto.getLatitude().trim().isEmpty()) {
            try {
                BigDecimal latitude = new BigDecimal(dto.getLatitude());
                if (latitude.compareTo(new BigDecimal("-90")) < 0 || latitude.compareTo(new BigDecimal("90")) > 0) {
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .errorMessage("Latitude must be between -90 and 90")
                            .build());
                }
            } catch (NumberFormatException e) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Invalid latitude format")
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(LocationBulkUploadDto dto) {
        // Check for duplicate location name (case-insensitive)
        return locationRepository.findByLocationName(dto.getLocationName()).isPresent();
    }
}
