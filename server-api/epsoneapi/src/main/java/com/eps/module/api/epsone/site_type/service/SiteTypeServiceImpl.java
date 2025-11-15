package com.eps.module.api.epsone.site_type.service;

import com.eps.module.api.epsone.site_type.dto.SiteTypeBulkUploadDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeErrorReportDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeResponseDto;
import com.eps.module.api.epsone.site_type.mapper.SiteTypeMapper;
import com.eps.module.api.epsone.site_type.processor.SiteTypeBulkUploadProcessor;
import com.eps.module.api.epsone.site_type.repository.SiteTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.site.SiteType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteTypeServiceImpl extends BaseBulkUploadService<SiteTypeBulkUploadDto, SiteType> implements SiteTypeService {

    private final SiteTypeRepository repository;
    private final SiteTypeMapper mapper;
    private final SiteTypeBulkUploadProcessor siteTypeBulkUploadProcessor;

    @Override
    @Transactional
    public SiteTypeResponseDto createSiteType(SiteTypeRequestDto requestDto) {
        SiteType siteType = mapper.toEntity(requestDto);
        SiteType savedSiteType = repository.save(siteType);
        return mapper.toResponseDto(savedSiteType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteTypeResponseDto> getAllSiteTypes(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteTypeResponseDto> searchSiteTypes(String searchTerm, Pageable pageable) {
        return repository.searchSiteTypes(searchTerm, pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteTypeResponseDto> getSiteTypeList() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SiteTypeResponseDto getSiteTypeById(Long id) {
        SiteType siteType = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site Type not found with id: " + id));
        return mapper.toResponseDto(siteType);
    }

    @Override
    @Transactional
    public SiteTypeResponseDto updateSiteType(Long id, SiteTypeRequestDto requestDto) {
        SiteType siteType = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site Type not found with id: " + id));
        
        mapper.updateEntityFromDto(requestDto, siteType);
        SiteType updatedSiteType = repository.save(siteType);
        return mapper.toResponseDto(updatedSiteType);
    }

    @Override
    @Transactional
    public void deleteSiteType(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Site Type not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // Bulk Upload Methods
    @Override
    protected BulkUploadProcessor<SiteTypeBulkUploadDto, SiteType> getProcessor() {
        return siteTypeBulkUploadProcessor;
    }

    @Override
    public Class<SiteTypeBulkUploadDto> getBulkUploadDtoClass() {
        return SiteTypeBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Site Type";
    }

    @Override
    public List<SiteType> getAllEntitiesForExport() {
        return repository.findAllForExport();
    }

    @Override
    public Function<SiteType, SiteTypeBulkUploadDto> getEntityToDtoMapper() {
        return entity -> SiteTypeBulkUploadDto.builder()
                .typeName(entity.getTypeName())
                .description(entity.getDescription())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        SiteTypeErrorReportDto.SiteTypeErrorReportDtoBuilder builder =
                SiteTypeErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.typeName((String) error.getRowData().get("typeName"))
                    .description((String) error.getRowData().get("description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return SiteTypeErrorReportDto.class;
    }
}
