package com.eps.module.api.epsone.location.validator;

import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.api.epsone.location.constant.LocationErrorMessages;
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
                    .errorMessage(LocationErrorMessages.LOCATION_NAME_REQUIRED)
                    .build());
        } else if (dto.getLocationName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(LocationErrorMessages.LOCATION_NAME_TOO_LONG)
                    .build());
        }

        // Validate address (optional, max 5000 chars)
        if (dto.getAddress() != null && dto.getAddress().length() > 5000) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(LocationErrorMessages.ADDRESS_TOO_LONG)
                    .build());
        }

        // Validate district (optional, max 100 chars)
        if (dto.getDistrict() != null && dto.getDistrict().length() > 100) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(LocationErrorMessages.DISTRICT_TOO_LONG)
                    .build());
        }

        // Validate city name (required)
        if (dto.getCityName() == null || dto.getCityName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(LocationErrorMessages.CITY_NAME_REQUIRED)
                    .build());
        } else {
            // Check if city exists
            boolean cityExists = cityRepository.findByCityName(dto.getCityName()).isPresent();
            if (!cityExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(LocationErrorMessages.CITY_NOT_FOUND_NAME + dto.getCityName())
                        .build());
            }
        }

        // Validate pincode (optional, must be 6 digits if provided)
        if (dto.getPincode() != null && !dto.getPincode().trim().isEmpty()) {
            if (!dto.getPincode().matches("^[0-9]{6}$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(LocationErrorMessages.PINCODE_INVALID_FORMAT)
                        .build());
            }
        }

        // Validate region (optional, max 50 chars)
        if (dto.getRegion() != null && dto.getRegion().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(LocationErrorMessages.REGION_TOO_LONG)
                    .build());
        }

        // Validate zone (optional, max 50 chars)
        if (dto.getZone() != null && dto.getZone().length() > 50) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage(LocationErrorMessages.ZONE_TOO_LONG)
                    .build());
        }

        // Validate longitude (optional)
        if (dto.getLongitude() != null && !dto.getLongitude().trim().isEmpty()) {
            try {
                BigDecimal longitude = new BigDecimal(dto.getLongitude());
                if (longitude.compareTo(new BigDecimal("-180")) < 0 || longitude.compareTo(new BigDecimal("180")) > 0) {
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .errorMessage(LocationErrorMessages.LONGITUDE_INVALID_RANGE)
                            .build());
                }
            } catch (NumberFormatException e) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(LocationErrorMessages.LONGITUDE_INVALID_FORMAT)
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
                            .errorMessage(LocationErrorMessages.LATITUDE_INVALID_RANGE)
                            .build());
                }
            } catch (NumberFormatException e) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(LocationErrorMessages.LATITUDE_INVALID_FORMAT)
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
