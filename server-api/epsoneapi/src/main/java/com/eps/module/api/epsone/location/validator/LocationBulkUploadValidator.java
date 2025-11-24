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
        } else {
            // Check if location name contains at least one alphabetic character
            if (!dto.getLocationName().matches(".*[a-zA-Z].*")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(LocationErrorMessages.LOCATION_NAME_INVALID_FORMAT)
                        .build());
            } else {
                // Capitalize location name
                String capitalizedName = capitalizeWords(dto.getLocationName().trim());
                dto.setLocationName(capitalizedName);
            }
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

        // Validate and clean pincode (optional, must be 6 digits if provided)
        if (dto.getPincode() != null && !dto.getPincode().trim().isEmpty()) {
            String trimmedPincode = dto.getPincode().trim();
            // Remove any non-digit characters (spaces, hyphens, etc.)
            String cleanedPincode = trimmedPincode.replaceAll("[^0-9]", "");
            
            if (!cleanedPincode.matches("^[0-9]{6}$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(LocationErrorMessages.PINCODE_INVALID_FORMAT + " (received: '" + dto.getPincode() + "', cleaned: '" + cleanedPincode + "', length: " + cleanedPincode.length() + ")")
                        .build());
            } else {
                // Set the cleaned pincode back to DTO so it's saved correctly
                dto.setPincode(cleanedPincode);
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
            if (dto.getLongitude().length() > 50) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(LocationErrorMessages.LONGITUDE_TOO_LONG)
                        .build());
            }
        }

        // Validate latitude (optional)
        if (dto.getLatitude() != null && !dto.getLatitude().trim().isEmpty()) {
            if (dto.getLatitude().length() > 50) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage(LocationErrorMessages.LATITUDE_TOO_LONG)
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

    /**
     * Capitalize the first letter of each word in a string
     */
    private String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        String[] words = input.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(word.substring(0, 1).toUpperCase())
                      .append(word.substring(1).toLowerCase());
            }
        }
        
        return result.toString();
    }
}
