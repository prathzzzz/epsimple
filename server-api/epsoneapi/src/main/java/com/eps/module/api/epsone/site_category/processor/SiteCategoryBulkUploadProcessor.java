package com.eps.module.api.epsone.site_category.processor;

import com.eps.module.api.epsone.site_category.dto.SiteCategoryBulkUploadDto;
import com.eps.module.api.epsone.site_category.repository.SiteCategoryRepository;
import com.eps.module.api.epsone.site_category.validator.SiteCategoryBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.site.SiteCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteCategoryBulkUploadProcessor extends BulkUploadProcessor<SiteCategoryBulkUploadDto, SiteCategory> {

    private final SiteCategoryRepository siteCategoryRepository;
    private final SiteCategoryBulkUploadValidator siteCategoryBulkUploadValidator;

    @Override
    public BulkRowValidator<SiteCategoryBulkUploadDto> getValidator() {
        return siteCategoryBulkUploadValidator;
    }

    @Override
    @Transactional
    public SiteCategory convertToEntity(SiteCategoryBulkUploadDto dto) {
        return SiteCategory.builder()
                .categoryName(dto.getCategoryName() != null ? dto.getCategoryName().trim() : null)
                .categoryCode(dto.getCategoryCode() != null 
                        ? dto.getCategoryCode().trim().toUpperCase()
                        : null)
                .description(dto.getDescription() != null && !dto.getDescription().isBlank() 
                        ? dto.getDescription().trim() 
                        : null)
                .build();
    }

    @Override
    public void saveEntity(SiteCategory entity) {
        siteCategoryRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(SiteCategoryBulkUploadDto dto) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("categoryName", dto.getCategoryName());
        data.put("categoryCode", dto.getCategoryCode());
        data.put("description", dto.getDescription());
        return data;
    }

    @Override
    public boolean isEmptyRow(SiteCategoryBulkUploadDto dto) {
        return (dto.getCategoryName() == null || dto.getCategoryName().isBlank()) &&
               (dto.getCategoryCode() == null || dto.getCategoryCode().isBlank()) &&
               (dto.getDescription() == null || dto.getDescription().isBlank());
    }
}
