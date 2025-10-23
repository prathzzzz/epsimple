package com.eps.module.api.epsone.city.mapper;

import com.eps.module.api.epsone.city.dto.CityRequestDto;
import com.eps.module.api.epsone.city.dto.CityResponseDto;
import com.eps.module.location.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    City toEntity(CityRequestDto requestDto);

    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.stateName", target = "stateName")
    @Mapping(source = "state.stateCode", target = "stateCode")
    CityResponseDto toResponseDto(City city);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    void updateEntityFromDto(CityRequestDto requestDto, @MappingTarget City city);
}
