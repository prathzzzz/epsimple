package com.eps.module.api.epsone.landlord.service;

import com.eps.module.api.epsone.landlord.dto.LandlordBulkUploadDto;
import com.eps.module.api.epsone.landlord.dto.LandlordRequestDto;
import com.eps.module.api.epsone.landlord.dto.LandlordResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.vendor.Landlord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LandlordService extends BulkUploadService<LandlordBulkUploadDto, Landlord> {

    LandlordResponseDto createLandlord(LandlordRequestDto requestDto);

    Page<LandlordResponseDto> getAllLandlords(Pageable pageable);

    Page<LandlordResponseDto> searchLandlords(String searchTerm, Pageable pageable);

    List<LandlordResponseDto> getAllLandlordsList();

    LandlordResponseDto getLandlordById(Long id);

    LandlordResponseDto updateLandlord(Long id, LandlordRequestDto requestDto);

    void deleteLandlord(Long id);
}
