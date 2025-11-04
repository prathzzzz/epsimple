package com.eps.module.api.epsone.site_type.mapper;

import com.eps.module.api.epsone.site_type.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeResponseDto;
import com.eps.module.site.SiteType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SiteTypeMapper {

    SiteTypeResponseDto toResponseDto(SiteType siteType);

    SiteType toEntity(SiteTypeRequestDto siteTypeRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SiteTypeRequestDto siteTypeRequestDto, @MappingTarget SiteType siteType);
}
