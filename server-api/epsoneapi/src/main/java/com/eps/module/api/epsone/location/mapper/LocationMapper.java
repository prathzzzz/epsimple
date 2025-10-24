package com.eps.module.api.epsone.location.mapper;

import com.eps.module.api.epsone.location.dto.LocationRequestDto;
import com.eps.module.api.epsone.location.dto.LocationResponseDto;
import com.eps.module.location.Location;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    @Mapping(target = "city.id", source = "cityId")
    Location toEntity(LocationRequestDto dto);

    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.cityName")
    @Mapping(target = "stateName", source = "city.state.stateName")
    LocationResponseDto toResponseDto(Location location);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "city.id", source = "cityId")
    void updateEntityFromDto(LocationRequestDto dto, @MappingTarget Location location);
}
