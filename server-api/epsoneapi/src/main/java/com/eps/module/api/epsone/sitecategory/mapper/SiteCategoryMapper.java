package com.eps.module.api.epsone.sitecategory.mapper;

import com.eps.module.api.epsone.sitecategory.dto.SiteCategoryRequestDto;
import com.eps.module.api.epsone.sitecategory.dto.SiteCategoryResponseDto;
import com.eps.module.site.SiteCategory;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SiteCategoryMapper {

    SiteCategoryResponseDto toResponseDto(SiteCategory siteCategory);

    SiteCategory toEntity(SiteCategoryRequestDto siteCategoryRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SiteCategoryRequestDto siteCategoryRequestDto, @MappingTarget SiteCategory siteCategory);
}
