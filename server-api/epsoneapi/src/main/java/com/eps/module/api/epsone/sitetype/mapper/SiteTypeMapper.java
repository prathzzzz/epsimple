package com.eps.module.api.epsone.sitetype.mapper;

import com.eps.module.api.epsone.sitetype.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.sitetype.dto.SiteTypeResponseDto;
import com.eps.module.site.SiteType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SiteTypeMapper {

    SiteTypeResponseDto toResponseDto(SiteType siteType);

    SiteType toEntity(SiteTypeRequestDto siteTypeRequestDto);

    void updateEntityFromDto(SiteTypeRequestDto siteTypeRequestDto, @MappingTarget SiteType siteType);
}
