package com.eps.module.api.epsone.site_type.service;

import com.eps.module.api.epsone.site_type.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SiteTypeService {

    SiteTypeResponseDto createSiteType(SiteTypeRequestDto requestDto);

    Page<SiteTypeResponseDto> getAllSiteTypes(Pageable pageable);

    Page<SiteTypeResponseDto> searchSiteTypes(String searchTerm, Pageable pageable);

    List<SiteTypeResponseDto> getSiteTypeList();

    SiteTypeResponseDto getSiteTypeById(Long id);

    SiteTypeResponseDto updateSiteType(Long id, SiteTypeRequestDto requestDto);

    void deleteSiteType(Long id);
}
