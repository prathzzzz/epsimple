package com.eps.module.api.epsone.generic_status_type.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeRequestDto;
import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeResponseDto;
import com.eps.module.status.GenericStatusType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface GenericStatusTypeMapper {

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    GenericStatusTypeResponseDto toResponseDto(GenericStatusType genericStatusType);

    GenericStatusType toEntity(GenericStatusTypeRequestDto genericStatusTypeRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(GenericStatusTypeRequestDto genericStatusTypeRequestDto, @MappingTarget GenericStatusType genericStatusType);
}
