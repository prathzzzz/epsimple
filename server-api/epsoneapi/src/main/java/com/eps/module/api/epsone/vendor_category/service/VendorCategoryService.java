package com.eps.module.api.epsone.vendor_category.service;

import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryBulkUploadDto;
import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryRequestDto;
import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.vendor.VendorCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorCategoryService extends BulkUploadService<VendorCategoryBulkUploadDto, VendorCategory> {
    VendorCategoryResponseDto createVendorCategory(VendorCategoryRequestDto requestDto);
    VendorCategoryResponseDto updateVendorCategory(Long id, VendorCategoryRequestDto requestDto);
    void deleteVendorCategory(Long id);
    VendorCategoryResponseDto getVendorCategoryById(Long id);
    Page<VendorCategoryResponseDto> getAllVendorCategories(Pageable pageable);
    Page<VendorCategoryResponseDto> searchVendorCategories(String search, Pageable pageable);
    List<VendorCategoryResponseDto> getAllVendorCategoriesList();
}
