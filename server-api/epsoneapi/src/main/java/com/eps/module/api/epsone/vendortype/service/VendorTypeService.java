package com.eps.module.api.epsone.vendortype.service;

import com.eps.module.api.epsone.vendortype.dto.VendorTypeRequestDto;
import com.eps.module.api.epsone.vendortype.dto.VendorTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorTypeService {
    VendorTypeResponseDto createVendorType(VendorTypeRequestDto requestDto);
    VendorTypeResponseDto updateVendorType(Long id, VendorTypeRequestDto requestDto);
    void deleteVendorType(Long id);
    VendorTypeResponseDto getVendorTypeById(Long id);
    Page<VendorTypeResponseDto> getAllVendorTypes(Pageable pageable);
    Page<VendorTypeResponseDto> searchVendorTypes(String search, Pageable pageable);
    List<VendorTypeResponseDto> getAllVendorTypesList();
}
