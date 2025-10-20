package com.eps.module.api.epsone.vendorcategory.service;

import com.eps.module.api.epsone.vendorcategory.dto.VendorCategoryRequestDto;
import com.eps.module.api.epsone.vendorcategory.dto.VendorCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorCategoryService {
    VendorCategoryResponseDto createVendorCategory(VendorCategoryRequestDto requestDto);
    VendorCategoryResponseDto updateVendorCategory(Long id, VendorCategoryRequestDto requestDto);
    void deleteVendorCategory(Long id);
    VendorCategoryResponseDto getVendorCategoryById(Long id);
    Page<VendorCategoryResponseDto> getAllVendorCategories(Pageable pageable);
    Page<VendorCategoryResponseDto> searchVendorCategories(String search, Pageable pageable);
    List<VendorCategoryResponseDto> getAllVendorCategoriesList();
}
