package com.eps.module.api.epsone.genericstatustype.mapper;

import com.eps.module.api.epsone.genericstatustype.dto.GenericStatusTypeRequestDto;
import com.eps.module.api.epsone.genericstatustype.dto.GenericStatusTypeResponseDto;
import com.eps.module.status.GenericStatusType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenericStatusTypeMapper {

    GenericStatusTypeResponseDto toResponseDto(GenericStatusType genericStatusType);

    GenericStatusType toEntity(GenericStatusTypeRequestDto genericStatusTypeRequestDto);

    void updateEntityFromDto(GenericStatusTypeRequestDto genericStatusTypeRequestDto, @MappingTarget GenericStatusType genericStatusType);
}
