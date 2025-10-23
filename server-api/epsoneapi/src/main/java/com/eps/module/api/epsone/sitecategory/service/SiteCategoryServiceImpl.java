package com.eps.module.api.epsone.sitecategory.service;

import com.eps.module.api.epsone.sitecategory.dto.SiteCategoryRequestDto;
import com.eps.module.api.epsone.sitecategory.dto.SiteCategoryResponseDto;
import com.eps.module.api.epsone.sitecategory.mapper.SiteCategoryMapper;
import com.eps.module.api.epsone.sitecategory.repository.SiteCategoryRepository;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.site.SiteCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteCategoryServiceImpl implements SiteCategoryService {

    private final SiteCategoryRepository repository;
    private final SiteCategoryMapper mapper;

    @Override
    @Transactional
    public SiteCategoryResponseDto createSiteCategory(SiteCategoryRequestDto requestDto) {
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
                .orElseThrow(() -> new ResourceNotFoundException("Site Category not found with id: " + id));
        return mapper.toResponseDto(siteCategory);
    }

    @Override
    @Transactional
    public SiteCategoryResponseDto updateSiteCategory(Long id, SiteCategoryRequestDto requestDto) {
        SiteCategory siteCategory = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site Category not found with id: " + id));
        
        mapper.updateEntityFromDto(requestDto, siteCategory);
        SiteCategory updatedSiteCategory = repository.save(siteCategory);
        return mapper.toResponseDto(updatedSiteCategory);
    }

    @Override
    @Transactional
    public void deleteSiteCategory(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Site Category not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
