package com.eps.module.api.epsone.state.mapper;

import com.eps.module.api.epsone.state.dto.StateRequestDto;
import com.eps.module.api.epsone.state.dto.StateResponseDto;
import com.eps.module.location.State;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-20T18:51:08+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class StateMapperImpl implements StateMapper {

    @Override
    public StateResponseDto toResponseDto(State state) {
        if ( state == null ) {
            return null;
        }

        StateResponseDto.StateResponseDtoBuilder stateResponseDto = StateResponseDto.builder();

        stateResponseDto.id( state.getId() );
        stateResponseDto.stateName( state.getStateName() );
        stateResponseDto.stateCode( state.getStateCode() );
        stateResponseDto.stateCodeAlt( state.getStateCodeAlt() );
        stateResponseDto.createdAt( state.getCreatedAt() );
        stateResponseDto.updatedAt( state.getUpdatedAt() );

        return stateResponseDto.build();
    }

    @Override
    public State toEntity(StateRequestDto stateRequestDto) {
        if ( stateRequestDto == null ) {
            return null;
        }

        State.StateBuilder state = State.builder();

        state.stateName( stateRequestDto.getStateName() );
        state.stateCode( stateRequestDto.getStateCode() );
        state.stateCodeAlt( stateRequestDto.getStateCodeAlt() );

        return state.build();
    }

    @Override
    public void updateEntityFromDto(StateRequestDto stateRequestDto, State state) {
        if ( stateRequestDto == null ) {
            return;
        }

        state.setStateName( stateRequestDto.getStateName() );
        state.setStateCode( stateRequestDto.getStateCode() );
        state.setStateCodeAlt( stateRequestDto.getStateCodeAlt() );
    }
}
