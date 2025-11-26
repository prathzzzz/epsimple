package com.eps.module.api.epsone.person_details.mapper;

import com.eps.module.api.epsone.person_details.dto.PersonDetailsRequestDto;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.person.PersonDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface PersonDetailsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personType", ignore = true)
    PersonDetails toEntity(PersonDetailsRequestDto requestDto);

    @Mapping(source = "personType.id", target = "personTypeId")
    @Mapping(source = "personType.typeName", target = "personTypeName")
    @Mapping(target = "fullName", expression = "java(buildFullName(personDetails))")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    PersonDetailsResponseDto toResponseDto(PersonDetails personDetails);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personType", ignore = true)
    void updateEntityFromDto(PersonDetailsRequestDto requestDto, @MappingTarget PersonDetails personDetails);

    default String buildFullName(PersonDetails personDetails) {
        StringBuilder fullName = new StringBuilder();
        if (personDetails.getFirstName() != null && !personDetails.getFirstName().isEmpty()) {
            fullName.append(personDetails.getFirstName());
        }
        if (personDetails.getMiddleName() != null && !personDetails.getMiddleName().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(personDetails.getMiddleName());
        }
        if (personDetails.getLastName() != null && !personDetails.getLastName().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(personDetails.getLastName());
        }
        return fullName.length() > 0 ? fullName.toString() : null;
    }
}
