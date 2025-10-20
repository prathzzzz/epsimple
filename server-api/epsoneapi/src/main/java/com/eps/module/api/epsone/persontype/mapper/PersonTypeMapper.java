package com.eps.module.api.epsone.persontype.mapper;

import com.eps.module.api.epsone.persontype.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.persontype.dto.PersonTypeResponseDto;
import com.eps.module.person.PersonType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonTypeMapper {
    PersonTypeResponseDto toResponseDto(PersonType personType);
    PersonType toEntity(PersonTypeRequestDto requestDto);
    void updateEntityFromDto(PersonTypeRequestDto requestDto, @MappingTarget PersonType personType);
    List<PersonTypeResponseDto> toResponseDtoList(List<PersonType> personTypes);
}
