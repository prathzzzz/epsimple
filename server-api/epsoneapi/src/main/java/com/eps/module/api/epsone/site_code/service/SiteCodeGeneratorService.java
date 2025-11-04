package com.eps.module.api.epsone.site_code.service;

import com.eps.module.api.epsone.site_code.dto.GeneratedSiteCodeDto;
import com.eps.module.api.epsone.site_code.dto.SiteCodeGeneratorRequestDto;
import com.eps.module.api.epsone.site_code.dto.SiteCodeGeneratorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SiteCodeGeneratorService {

    SiteCodeGeneratorResponseDto createSiteCodeGenerator(SiteCodeGeneratorRequestDto requestDto);

    Page<SiteCodeGeneratorResponseDto> getAllSiteCodeGenerators(Pageable pageable);

    Page<SiteCodeGeneratorResponseDto> searchSiteCodeGenerators(String searchTerm, Pageable pageable);

    List<SiteCodeGeneratorResponseDto> getListSiteCodeGenerators();

    SiteCodeGeneratorResponseDto getSiteCodeGeneratorById(Long id);

    SiteCodeGeneratorResponseDto updateSiteCodeGenerator(Long id, SiteCodeGeneratorRequestDto requestDto);

    void deleteSiteCodeGenerator(Long id);

    GeneratedSiteCodeDto generateSiteCode(Long projectId, Long stateId);

    GeneratedSiteCodeDto previewSiteCode(Long projectId, Long stateId);
}
