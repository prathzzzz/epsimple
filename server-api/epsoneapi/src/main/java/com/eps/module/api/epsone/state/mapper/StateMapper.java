package com.eps.module.api.epsone.state.mapper;

import com.eps.module.api.epsone.state.dto.StateRequestDto;
import com.eps.module.api.epsone.state.dto.StateResponseDto;
import com.eps.module.location.State;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StateMapper {

    StateResponseDto toResponseDto(State state);

    State toEntity(StateRequestDto stateRequestDto);

    void updateEntityFromDto(StateRequestDto stateRequestDto, @MappingTarget State state);
}
