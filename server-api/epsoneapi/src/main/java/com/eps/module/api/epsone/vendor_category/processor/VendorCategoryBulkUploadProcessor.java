package com.eps.module.api.epsone.vendor_category.processor;

import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryBulkUploadDto;
import com.eps.module.api.epsone.vendor_category.validator.VendorCategoryBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.vendor.VendorCategory;
import com.eps.module.api.epsone.vendor_category.repository.VendorCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VendorCategoryBulkUploadProcessor extends BulkUploadProcessor<VendorCategoryBulkUploadDto, VendorCategory> {

    private final VendorCategoryBulkUploadValidator validator;
    private final VendorCategoryRepository vendorCategoryRepository;

    @Override
    protected BulkRowValidator<VendorCategoryBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected VendorCategory convertToEntity(VendorCategoryBulkUploadDto dto) {
        return VendorCategory.builder()
                .categoryName(capitalizeFirstLetter(dto.getCategoryName()))
                .description(dto.getDescription())
                .build();
    }

    @Override
    protected void saveEntity(VendorCategory entity) {
        vendorCategoryRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(VendorCategoryBulkUploadDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryName", dto.getCategoryName());
        map.put("description", dto.getDescription());
        return map;
    }

    @Override
    protected boolean isEmptyRow(VendorCategoryBulkUploadDto dto) {
        return (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) &&
               (dto.getDescription() == null || dto.getDescription().trim().isEmpty());
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        String trimmed = input.trim();
        return trimmed.substring(0,1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}
