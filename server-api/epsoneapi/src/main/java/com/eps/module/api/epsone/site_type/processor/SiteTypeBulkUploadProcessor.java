package com.eps.module.api.epsone.site_type.processor;

import com.eps.module.api.epsone.site_type.dto.SiteTypeBulkUploadDto;
import com.eps.module.api.epsone.site_type.repository.SiteTypeRepository;
import com.eps.module.api.epsone.site_type.validator.SiteTypeBulkUploadValidator;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.site.SiteType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteTypeBulkUploadProcessor extends BulkUploadProcessor<SiteTypeBulkUploadDto, SiteType> {

    private final SiteTypeRepository siteTypeRepository;
    private final SiteTypeBulkUploadValidator siteTypeBulkUploadValidator;

    @Override
    public BulkRowValidator<SiteTypeBulkUploadDto> getValidator() {
        return siteTypeBulkUploadValidator;
    }

    @Override
    @Transactional
    public SiteType convertToEntity(SiteTypeBulkUploadDto dto) {
        return SiteType.builder()
                .typeName(dto.getTypeName() != null ? dto.getTypeName().trim() : null)
                .description(dto.getDescription() != null && !dto.getDescription().isBlank() 
                        ? dto.getDescription().trim() 
                        : null)
                .build();
    }

    @Override
    public void saveEntity(SiteType entity) {
        siteTypeRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(SiteTypeBulkUploadDto dto) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("typeName", dto.getTypeName());
        data.put("description", dto.getDescription());
        return data;
    }

    @Override
    public boolean isEmptyRow(SiteTypeBulkUploadDto dto) {
        return (dto.getTypeName() == null || dto.getTypeName().isBlank()) &&
               (dto.getDescription() == null || dto.getDescription().isBlank());
    }
}
