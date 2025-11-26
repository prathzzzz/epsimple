package com.eps.module.api.epsone.vendor_category.mapper;

import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryRequestDto;
import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.vendor.VendorCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface VendorCategoryMapper {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    VendorCategoryResponseDto toResponseDto(VendorCategory vendorCategory);
    VendorCategory toEntity(VendorCategoryRequestDto requestDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(VendorCategoryRequestDto requestDto, @MappingTarget VendorCategory vendorCategory);
    List<VendorCategoryResponseDto> toResponseDtoList(List<VendorCategory> vendorCategories);
}
