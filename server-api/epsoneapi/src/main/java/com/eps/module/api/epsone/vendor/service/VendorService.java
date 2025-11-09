package com.eps.module.api.epsone.vendor.service;

import com.eps.module.api.epsone.vendor.dto.VendorBulkUploadDto;
import com.eps.module.api.epsone.vendor.dto.VendorRequestDto;
import com.eps.module.api.epsone.vendor.dto.VendorResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.vendor.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorService extends BulkUploadService<VendorBulkUploadDto, Vendor> {

    VendorResponseDto createVendor(VendorRequestDto requestDto);

    Page<VendorResponseDto> getAllVendors(Pageable pageable);

    Page<VendorResponseDto> searchVendors(String searchTerm, Pageable pageable);

    List<VendorResponseDto> getAllVendorsList();

    Page<VendorResponseDto> getVendorsByType(Long vendorTypeId, Pageable pageable);

    VendorResponseDto getVendorById(Long id);

    VendorResponseDto updateVendor(Long id, VendorRequestDto requestDto);

    void deleteVendor(Long id);
}
