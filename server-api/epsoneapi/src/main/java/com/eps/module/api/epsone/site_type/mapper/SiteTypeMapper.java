package com.eps.module.api.epsone.site_type.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.site_type.dto.SiteTypeRequestDto;
import com.eps.module.api.epsone.site_type.dto.SiteTypeResponseDto;
import com.eps.module.site.SiteType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface SiteTypeMapper {

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    SiteTypeResponseDto toResponseDto(SiteType siteType);

    SiteType toEntity(SiteTypeRequestDto siteTypeRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SiteTypeRequestDto siteTypeRequestDto, @MappingTarget SiteType siteType);
}
