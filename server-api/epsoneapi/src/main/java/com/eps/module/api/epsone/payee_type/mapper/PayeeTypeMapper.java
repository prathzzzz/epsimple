package com.eps.module.api.epsone.payee_type.mapper;

import com.eps.module.api.epsone.payee_type.dto.PayeeTypeRequestDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeResponseDto;
import com.eps.module.payment.PayeeType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayeeTypeMapper {

    PayeeTypeResponseDto toResponseDto(PayeeType payeeType);

    PayeeType toEntity(PayeeTypeRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PayeeTypeRequestDto requestDto, @MappingTarget PayeeType payeeType);
}
