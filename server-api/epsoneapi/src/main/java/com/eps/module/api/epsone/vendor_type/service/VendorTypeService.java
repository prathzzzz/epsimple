package com.eps.module.api.epsone.vendor_type.service;

import com.eps.module.api.epsone.vendor_type.dto.VendorTypeBulkUploadDto;
import com.eps.module.api.epsone.vendor_type.dto.VendorTypeRequestDto;
import com.eps.module.api.epsone.vendor_type.dto.VendorTypeResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.vendor.VendorType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorTypeService extends BulkUploadService<VendorTypeBulkUploadDto, VendorType> {
    VendorTypeResponseDto createVendorType(VendorTypeRequestDto requestDto);
    VendorTypeResponseDto updateVendorType(Long id, VendorTypeRequestDto requestDto);
    void deleteVendorType(Long id);
    VendorTypeResponseDto getVendorTypeById(Long id);
    Page<VendorTypeResponseDto> getAllVendorTypes(Pageable pageable);
    Page<VendorTypeResponseDto> searchVendorTypes(String search, Pageable pageable);
    List<VendorTypeResponseDto> getAllVendorTypesList();
}
