package com.eps.module.api.epsone.vendor_type.mapper;

import com.eps.module.api.epsone.vendor_category.mapper.VendorCategoryMapper;
import com.eps.module.api.epsone.vendor_type.dto.VendorTypeRequestDto;
import com.eps.module.api.epsone.vendor_type.dto.VendorTypeResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.vendor.VendorType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {VendorCategoryMapper.class, AuditFieldMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VendorTypeMapper {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    VendorTypeResponseDto toResponseDto(VendorType vendorType);
    
    @Mapping(target = "vendorCategory", ignore = true)
    VendorType toEntity(VendorTypeRequestDto requestDto);
    
    @Mapping(target = "vendorCategory", ignore = true)
    void updateEntityFromDto(VendorTypeRequestDto requestDto, @MappingTarget VendorType vendorType);
    
    List<VendorTypeResponseDto> toResponseDtoList(List<VendorType> vendorTypes);
}
