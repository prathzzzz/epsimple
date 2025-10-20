package com.eps.module.api.epsone.persontype.mapper;

import com.eps.module.api.epsone.persontype.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.persontype.dto.PersonTypeResponseDto;
import com.eps.module.person.PersonType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-20T20:11:06+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class PersonTypeMapperImpl implements PersonTypeMapper {

    @Override
    public PersonTypeResponseDto toResponseDto(PersonType personType) {
        if ( personType == null ) {
            return null;
        }

        PersonTypeResponseDto.PersonTypeResponseDtoBuilder personTypeResponseDto = PersonTypeResponseDto.builder();

        personTypeResponseDto.id( personType.getId() );
        personTypeResponseDto.typeName( personType.getTypeName() );
        personTypeResponseDto.description( personType.getDescription() );
        personTypeResponseDto.createdAt( personType.getCreatedAt() );
        personTypeResponseDto.updatedAt( personType.getUpdatedAt() );

        return personTypeResponseDto.build();
    }

    @Override
    public PersonType toEntity(PersonTypeRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        PersonType.PersonTypeBuilder personType = PersonType.builder();

        personType.typeName( requestDto.getTypeName() );
        personType.description( requestDto.getDescription() );

        return personType.build();
    }

    @Override
    public void updateEntityFromDto(PersonTypeRequestDto requestDto, PersonType personType) {
        if ( requestDto == null ) {
            return;
        }

        personType.setTypeName( requestDto.getTypeName() );
        personType.setDescription( requestDto.getDescription() );
    }

    @Override
    public List<PersonTypeResponseDto> toResponseDtoList(List<PersonType> personTypes) {
        if ( personTypes == null ) {
            return null;
        }

        List<PersonTypeResponseDto> list = new ArrayList<PersonTypeResponseDto>( personTypes.size() );
        for ( PersonType personType : personTypes ) {
            list.add( toResponseDto( personType ) );
        }

        return list;
    }
}
