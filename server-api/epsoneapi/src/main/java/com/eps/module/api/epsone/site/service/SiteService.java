package com.eps.module.api.epsone.site.service;

import com.eps.module.api.epsone.site.dto.SiteBulkUploadDto;
import com.eps.module.api.epsone.site.dto.SiteRequestDto;
import com.eps.module.api.epsone.site.dto.SiteResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.site.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SiteService extends BulkUploadService<SiteBulkUploadDto, Site> {

    SiteResponseDto createSite(SiteRequestDto requestDto);

    Page<SiteResponseDto> getAllSites(Pageable pageable);

    Page<SiteResponseDto> searchSites(String searchTerm, Pageable pageable);

    List<SiteResponseDto> getSiteList();

    SiteResponseDto getSiteById(Long id);

    SiteResponseDto updateSite(Long id, SiteRequestDto requestDto);

    void deleteSite(Long id);
}
