package com.eps.module.api.epsone.city.mapper;

import com.eps.module.api.epsone.city.dto.CityRequestDto;
import com.eps.module.api.epsone.city.dto.CityResponseDto;
import com.eps.module.location.City;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    City toEntity(CityRequestDto requestDto);

    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.stateName", target = "stateName")
    @Mapping(source = "state.stateCode", target = "stateCode")
    CityResponseDto toResponseDto(City city);

    List<CityResponseDto> toResponseDtoList(List<City> cities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CityRequestDto requestDto, @MappingTarget City city);
}
