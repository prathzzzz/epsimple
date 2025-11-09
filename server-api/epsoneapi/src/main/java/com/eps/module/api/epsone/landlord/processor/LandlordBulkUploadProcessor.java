package com.eps.module.api.epsone.landlord.processor;

import com.eps.module.api.epsone.landlord.dto.LandlordBulkUploadDto;
import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.landlord.validator.LandlordBulkUploadValidator;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.person.PersonDetails;
import com.eps.module.vendor.Landlord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LandlordBulkUploadProcessor extends BulkUploadProcessor<LandlordBulkUploadDto, Landlord> {

    private final LandlordBulkUploadValidator validator;
    private final LandlordRepository landlordRepository;
    private final PersonDetailsRepository personDetailsRepository;

    @Override
    public BulkRowValidator<LandlordBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    @Transactional
    public Landlord convertToEntity(LandlordBulkUploadDto dto) {
        // Find existing PersonDetails by contact number
        PersonDetails personDetails = personDetailsRepository.findByContactNumber(dto.getContactNumber().trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person Details not found with Contact Number: " + dto.getContactNumber()));

        // Create and return Landlord with existing PersonDetails
        return Landlord.builder()
                .landlordDetails(personDetails)
                .rentSharePercentage(dto.getRentSharePercentage())
                .build();
    }

    @Override
    public void saveEntity(Landlord entity) {
        landlordRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(LandlordBulkUploadDto dto) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("contactNumber", dto.getContactNumber());
        data.put("rentSharePercentage", dto.getRentSharePercentage());
        return data;
    }

    @Override
    public boolean isEmptyRow(LandlordBulkUploadDto dto) {
        return (dto.getContactNumber() == null || dto.getContactNumber().isBlank()) &&
               dto.getRentSharePercentage() == null;
    }
}
