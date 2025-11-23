package com.eps.module.api.epsone.site_category.service;

import com.eps.module.api.epsone.site_category.constant.SiteCategoryErrorMessages;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryBulkUploadDto;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryErrorReportDto;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryRequestDto;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryResponseDto;
import com.eps.module.api.epsone.site_category.mapper.SiteCategoryMapper;
import com.eps.module.api.epsone.site_category.processor.SiteCategoryBulkUploadProcessor;
import com.eps.module.api.epsone.site_category.repository.SiteCategoryRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.site.SiteCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteCategoryServiceImpl extends BaseBulkUploadService<SiteCategoryBulkUploadDto, SiteCategory> implements SiteCategoryService {

    private final SiteCategoryRepository repository;
    private final SiteCategoryMapper mapper;
    private final SiteCategoryBulkUploadProcessor siteCategoryBulkUploadProcessor;

    @Override
    @Transactional
    public SiteCategoryResponseDto createSiteCategory(SiteCategoryRequestDto requestDto) {
        // Check if category name already exists
        if (repository.existsByCategoryName(requestDto.getCategoryName())) {
            throw new ConflictException(String.format(SiteCategoryErrorMessages.SITE_CATEGORY_NAME_EXISTS, requestDto.getCategoryName()));
        }
        
        // Check if category code already exists (if provided)
        if (requestDto.getCategoryCode() != null && !requestDto.getCategoryCode().trim().isEmpty()) {
            if (repository.existsByCategoryCode(requestDto.getCategoryCode())) {
                throw new ConflictException(String.format(SiteCategoryErrorMessages.SITE_CATEGORY_CODE_EXISTS, requestDto.getCategoryCode()));
            }
        }
        
        SiteCategory siteCategory = mapper.toEntity(requestDto);
        SiteCategory savedSiteCategory = repository.save(siteCategory);
        return mapper.toResponseDto(savedSiteCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteCategoryResponseDto> getAllSiteCategories(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteCategoryResponseDto> searchSiteCategories(String searchTerm, Pageable pageable) {
        return repository.searchSiteCategories(searchTerm, pageable)
                .map(mapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteCategoryResponseDto> getSiteCategoryList() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SiteCategoryResponseDto getSiteCategoryById(Long id) {
        SiteCategory siteCategory = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SiteCategoryErrorMessages.SITE_CATEGORY_NOT_FOUND_ID + id));
        return mapper.toResponseDto(siteCategory);
    }

    @Override
    @Transactional
    public SiteCategoryResponseDto updateSiteCategory(Long id, SiteCategoryRequestDto requestDto) {
        SiteCategory siteCategory = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SiteCategoryErrorMessages.SITE_CATEGORY_NOT_FOUND_ID + id));
        
        // Check if category name is being changed and if it already exists
        if (!siteCategory.getCategoryName().equals(requestDto.getCategoryName())) {
            if (repository.existsByCategoryName(requestDto.getCategoryName())) {
                throw new ConflictException(String.format(SiteCategoryErrorMessages.SITE_CATEGORY_NAME_EXISTS, requestDto.getCategoryName()));
            }
        }
        
        // Check if category code is being changed and if it already exists
        if (requestDto.getCategoryCode() != null && !requestDto.getCategoryCode().trim().isEmpty()) {
            if (!requestDto.getCategoryCode().equals(siteCategory.getCategoryCode())) {
                if (repository.existsByCategoryCode(requestDto.getCategoryCode())) {
                    throw new ConflictException(String.format(SiteCategoryErrorMessages.SITE_CATEGORY_CODE_EXISTS, requestDto.getCategoryCode()));
                }
            }
        }
        
        mapper.updateEntityFromDto(requestDto, siteCategory);
        SiteCategory updatedSiteCategory = repository.save(siteCategory);
        return mapper.toResponseDto(updatedSiteCategory);
    }

    @Override
    @Transactional
    public void deleteSiteCategory(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(SiteCategoryErrorMessages.SITE_CATEGORY_NOT_FOUND_ID + id);
        }
        repository.deleteById(id);
    }

    // Bulk Upload Methods
    @Override
    protected BulkUploadProcessor<SiteCategoryBulkUploadDto, SiteCategory> getProcessor() {
        return siteCategoryBulkUploadProcessor;
    }

    @Override
    public Class<SiteCategoryBulkUploadDto> getBulkUploadDtoClass() {
        return SiteCategoryBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Site Category";
    }

    @Override
    public List<SiteCategory> getAllEntitiesForExport() {
        return repository.findAllForExport();
    }

    @Override
    public Function<SiteCategory, SiteCategoryBulkUploadDto> getEntityToDtoMapper() {
        return entity -> SiteCategoryBulkUploadDto.builder()
                .categoryName(entity.getCategoryName())
                .categoryCode(entity.getCategoryCode())
                .description(entity.getDescription())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        SiteCategoryErrorReportDto.SiteCategoryErrorReportDtoBuilder builder =
                SiteCategoryErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.categoryName((String) error.getRowData().get("categoryName"))
                    .categoryCode((String) error.getRowData().get("categoryCode"))
                    .description((String) error.getRowData().get("description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return SiteCategoryErrorReportDto.class;
    }
}
