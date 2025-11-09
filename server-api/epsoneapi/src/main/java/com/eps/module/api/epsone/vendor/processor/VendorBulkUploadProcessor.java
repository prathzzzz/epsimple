package com.eps.module.api.epsone.vendor.processor;

import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.vendor.dto.VendorBulkUploadDto;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.api.epsone.vendor.validator.VendorBulkUploadValidator;
import com.eps.module.api.epsone.vendor_type.repository.VendorTypeRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.person.PersonDetails;
import com.eps.module.vendor.Vendor;
import com.eps.module.vendor.VendorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VendorBulkUploadProcessor extends BulkUploadProcessor<VendorBulkUploadDto, Vendor> {

    private final VendorBulkUploadValidator validator;
    private final VendorRepository vendorRepository;
    private final VendorTypeRepository vendorTypeRepository;
    private final PersonDetailsRepository personDetailsRepository;

    @Override
    public BulkRowValidator<VendorBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    @Transactional
    public Vendor convertToEntity(VendorBulkUploadDto dto) {
        // Find existing PersonDetails by contact number
        PersonDetails personDetails = personDetailsRepository.findByContactNumber(dto.getContactNumber())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person Details not found with contact number: " + dto.getContactNumber()));

        // Find vendor type
        VendorType vendorType = vendorTypeRepository.findByTypeNameIgnoreCase(dto.getVendorTypeName())
                .orElseThrow(() -> new IllegalArgumentException("Vendor Type not found: " + dto.getVendorTypeName()));

        // Create vendor
        return Vendor.builder()
                .vendorType(vendorType)
                .vendorDetails(personDetails)
                .vendorCodeAlt(dto.getVendorCodeAlt() != null && !dto.getVendorCodeAlt().isBlank() 
                        ? dto.getVendorCodeAlt().trim() 
                        : null)
                .build();
    }

    @Override
    public void saveEntity(Vendor entity) {
        vendorRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(VendorBulkUploadDto dto) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("contactNumber", dto.getContactNumber());
        data.put("vendorTypeName", dto.getVendorTypeName());
        data.put("vendorCodeAlt", dto.getVendorCodeAlt());
        return data;
    }

    @Override
    public boolean isEmptyRow(VendorBulkUploadDto dto) {
        return (dto.getContactNumber() == null || dto.getContactNumber().isBlank()) &&
               (dto.getVendorTypeName() == null || dto.getVendorTypeName().isBlank()) &&
               (dto.getVendorCodeAlt() == null || dto.getVendorCodeAlt().isBlank());
    }
}
