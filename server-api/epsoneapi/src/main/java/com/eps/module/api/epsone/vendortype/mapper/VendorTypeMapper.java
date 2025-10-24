package com.eps.module.api.epsone.vendortype.mapper;

import com.eps.module.api.epsone.vendorcategory.mapper.VendorCategoryMapper;
import com.eps.module.api.epsone.vendortype.dto.VendorTypeRequestDto;
import com.eps.module.api.epsone.vendortype.dto.VendorTypeResponseDto;
import com.eps.module.vendor.VendorType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VendorCategoryMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VendorTypeMapper {
    VendorTypeResponseDto toResponseDto(VendorType vendorType);
    
    @Mapping(target = "vendorCategory", ignore = true)
    VendorType toEntity(VendorTypeRequestDto requestDto);
    
    @Mapping(target = "vendorCategory", ignore = true)
    void updateEntityFromDto(VendorTypeRequestDto requestDto, @MappingTarget VendorType vendorType);
    
    List<VendorTypeResponseDto> toResponseDtoList(List<VendorType> vendorTypes);
}
