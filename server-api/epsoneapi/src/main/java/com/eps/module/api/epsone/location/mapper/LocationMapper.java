package com.eps.module.api.epsone.location.mapper;

import com.eps.module.api.epsone.location.dto.LocationRequestDto;
import com.eps.module.api.epsone.location.dto.LocationResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.location.Location;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface LocationMapper {

    @Mapping(target = "city.id", source = "cityId")
    Location toEntity(LocationRequestDto dto);

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.cityName")
    @Mapping(target = "stateName", source = "city.state.stateName")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    LocationResponseDto toResponseDto(Location location);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "city.id", source = "cityId")
    void updateEntityFromDto(LocationRequestDto dto, @MappingTarget Location location);
}
