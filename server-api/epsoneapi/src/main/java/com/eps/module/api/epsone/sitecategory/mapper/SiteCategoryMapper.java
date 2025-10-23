package com.eps.module.api.epsone.sitecategory.mapper;

import com.eps.module.api.epsone.sitecategory.dto.SiteCategoryRequestDto;
import com.eps.module.api.epsone.sitecategory.dto.SiteCategoryResponseDto;
import com.eps.module.site.SiteCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SiteCategoryMapper {

    SiteCategoryResponseDto toResponseDto(SiteCategory siteCategory);

    SiteCategory toEntity(SiteCategoryRequestDto siteCategoryRequestDto);

    void updateEntityFromDto(SiteCategoryRequestDto siteCategoryRequestDto, @MappingTarget SiteCategory siteCategory);
}
