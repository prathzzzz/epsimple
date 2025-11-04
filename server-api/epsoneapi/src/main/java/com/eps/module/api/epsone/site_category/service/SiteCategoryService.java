package com.eps.module.api.epsone.site_category.service;

import com.eps.module.api.epsone.site_category.dto.SiteCategoryRequestDto;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SiteCategoryService {

    SiteCategoryResponseDto createSiteCategory(SiteCategoryRequestDto requestDto);

    Page<SiteCategoryResponseDto> getAllSiteCategories(Pageable pageable);

    Page<SiteCategoryResponseDto> searchSiteCategories(String searchTerm, Pageable pageable);

    List<SiteCategoryResponseDto> getSiteCategoryList();

    SiteCategoryResponseDto getSiteCategoryById(Long id);

    SiteCategoryResponseDto updateSiteCategory(Long id, SiteCategoryRequestDto requestDto);

    void deleteSiteCategory(Long id);
}
