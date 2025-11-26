package com.eps.module.api.epsone.person_type.mapper;

import com.eps.module.api.epsone.person_type.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.person_type.dto.PersonTypeResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.person.PersonType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface PersonTypeMapper {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    PersonTypeResponseDto toResponseDto(PersonType personType);
    PersonType toEntity(PersonTypeRequestDto requestDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PersonTypeRequestDto requestDto, @MappingTarget PersonType personType);
    List<PersonTypeResponseDto> toResponseDtoList(List<PersonType> personTypes);
}
