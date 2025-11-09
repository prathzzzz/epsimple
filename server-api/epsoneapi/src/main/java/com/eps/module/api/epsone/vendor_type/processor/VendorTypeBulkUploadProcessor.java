package com.eps.module.api.epsone.vendor_type.processor;

import com.eps.module.api.epsone.vendor_type.dto.VendorTypeBulkUploadDto;
import com.eps.module.api.epsone.vendor_type.validator.VendorTypeBulkUploadValidator;
import com.eps.module.api.epsone.vendor_type.repository.VendorTypeRepository;
import com.eps.module.api.epsone.vendor_category.repository.VendorCategoryRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.vendor.VendorType;
import com.eps.module.vendor.VendorCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VendorTypeBulkUploadProcessor extends BulkUploadProcessor<VendorTypeBulkUploadDto, VendorType> {

    private final VendorTypeBulkUploadValidator validator;
    private final VendorTypeRepository vendorTypeRepository;
    private final VendorCategoryRepository vendorCategoryRepository;

    @Override
    protected BulkRowValidator<VendorTypeBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected VendorType convertToEntity(VendorTypeBulkUploadDto dto) {
        // Find the vendor category by name
        VendorCategory vendorCategory = vendorCategoryRepository
                .findByCategoryNameIgnoreCase(dto.getVendorCategoryName().trim())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vendor category not found: " + dto.getVendorCategoryName()));

        return VendorType.builder()
                .typeName(capitalizeFirstLetter(dto.getTypeName()))
                .vendorCategory(vendorCategory)
                .description(dto.getDescription())
                .build();
    }

    @Override
    protected void saveEntity(VendorType entity) {
        vendorTypeRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(VendorTypeBulkUploadDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("typeName", dto.getTypeName());
        map.put("vendorCategoryName", dto.getVendorCategoryName());
        map.put("description", dto.getDescription());
        return map;
    }

    @Override
    protected boolean isEmptyRow(VendorTypeBulkUploadDto dto) {
        return (dto.getTypeName() == null || dto.getTypeName().trim().isEmpty()) &&
               (dto.getVendorCategoryName() == null || dto.getVendorCategoryName().trim().isEmpty()) &&
               (dto.getDescription() == null || dto.getDescription().trim().isEmpty());
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        String trimmed = input.trim();
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}
