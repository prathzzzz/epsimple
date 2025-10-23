package com.eps.module.api.epsone.vendorcategory.mapper;

import com.eps.module.api.epsone.vendorcategory.dto.VendorCategoryRequestDto;
import com.eps.module.api.epsone.vendorcategory.dto.VendorCategoryResponseDto;
import com.eps.module.vendor.VendorCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VendorCategoryMapper {
    VendorCategoryResponseDto toResponseDto(VendorCategory vendorCategory);
    VendorCategory toEntity(VendorCategoryRequestDto requestDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(VendorCategoryRequestDto requestDto, @MappingTarget VendorCategory vendorCategory);
    List<VendorCategoryResponseDto> toResponseDtoList(List<VendorCategory> vendorCategories);
}
