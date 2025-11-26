package com.eps.module.api.epsone.site_category.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryRequestDto;
import com.eps.module.api.epsone.site_category.dto.SiteCategoryResponseDto;
import com.eps.module.site.SiteCategory;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface SiteCategoryMapper {

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    SiteCategoryResponseDto toResponseDto(SiteCategory siteCategory);

    SiteCategory toEntity(SiteCategoryRequestDto siteCategoryRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SiteCategoryRequestDto siteCategoryRequestDto, @MappingTarget SiteCategory siteCategory);
}
