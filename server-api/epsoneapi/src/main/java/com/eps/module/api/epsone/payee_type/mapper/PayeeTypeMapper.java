package com.eps.module.api.epsone.payee_type.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeRequestDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeResponseDto;
import com.eps.module.payment.PayeeType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface PayeeTypeMapper {

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    PayeeTypeResponseDto toResponseDto(PayeeType payeeType);

    PayeeType toEntity(PayeeTypeRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PayeeTypeRequestDto requestDto, @MappingTarget PayeeType payeeType);
}
