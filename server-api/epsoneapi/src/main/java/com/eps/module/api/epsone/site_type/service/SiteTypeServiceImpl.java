package com.eps.module.api.epsone.site_type.service;

import com.eps.module.api.epsone.site_type.constant.SiteTypeErrorMessages;
import com.eps.module.api.epsone.site_type.dto.SiteTypeBulkUploadDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeErrorReportDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeResponseDto;
import com.eps.module.api.epsone.site_type.mapper.SiteTypeMapper;
import com.eps.module.api.epsone.site_type.processor.SiteTypeBulkUploadProcessor;
import com.eps.module.api.epsone.site_type.repository.SiteTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ConflictException;
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
        // Check if type name already exists
        if (repository.existsByTypeNameIgnoreCase(requestDto.getTypeName())) {
            throw new ConflictException(String.format(SiteTypeErrorMessages.SITE_TYPE_NAME_EXISTS, requestDto.getTypeName()));
        }
        
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
                .orElseThrow(() -> new ResourceNotFoundException(SiteTypeErrorMessages.SITE_TYPE_NOT_FOUND_ID + id));
        return mapper.toResponseDto(siteType);
    }

    @Override
    @Transactional
    public SiteTypeResponseDto updateSiteType(Long id, SiteTypeRequestDto requestDto) {
        SiteType siteType = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SiteTypeErrorMessages.SITE_TYPE_NOT_FOUND_ID + id));
        
        // Check if type name is being changed and if it already exists
        if (!siteType.getTypeName().equals(requestDto.getTypeName())) {
            if (repository.existsByTypeNameIgnoreCase(requestDto.getTypeName())) {
                throw new ConflictException(String.format(SiteTypeErrorMessages.SITE_TYPE_NAME_EXISTS, requestDto.getTypeName()));
            }
        }
        
        mapper.updateEntityFromDto(requestDto, siteType);
        SiteType updatedSiteType = repository.save(siteType);
        return mapper.toResponseDto(updatedSiteType);
    }

    @Override
    @Transactional
    public void deleteSiteType(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(SiteTypeErrorMessages.SITE_TYPE_NOT_FOUND_ID + id);
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
