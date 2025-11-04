package com.eps.module.api.epsone.site_activity_work_expenditure.service;

import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureRequestDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureResponseDto;
import org.springframework.data.domain.Page;

public interface SiteActivityWorkExpenditureService {

    SiteActivityWorkExpenditureResponseDto createSiteActivityWorkExpenditure(SiteActivityWorkExpenditureRequestDto requestDto);

    Page<SiteActivityWorkExpenditureResponseDto> getAllSiteActivityWorkExpenditures(
            int page, int size, String sortBy, String sortOrder);

    Page<SiteActivityWorkExpenditureResponseDto> getExpendituresBySiteId(
            Long siteId, int page, int size, String sortBy, String sortOrder);

    Page<SiteActivityWorkExpenditureResponseDto> getExpendituresByActivityWorkId(
            Long activityWorkId, int page, int size, String sortBy, String sortOrder);

    Page<SiteActivityWorkExpenditureResponseDto> searchExpenditures(
            String searchTerm, int page, int size, String sortBy, String sortOrder);

    SiteActivityWorkExpenditureResponseDto getExpenditureById(Long id);

    SiteActivityWorkExpenditureResponseDto updateSiteActivityWorkExpenditure(
            Long id, SiteActivityWorkExpenditureRequestDto requestDto);

    void deleteSiteActivityWorkExpenditure(Long id);
}
