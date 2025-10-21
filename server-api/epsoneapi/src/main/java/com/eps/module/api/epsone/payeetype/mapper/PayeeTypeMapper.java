package com.eps.module.api.epsone.payeetype.mapper;

import com.eps.module.api.epsone.payeetype.dto.PayeeTypeRequestDto;
import com.eps.module.api.epsone.payeetype.dto.PayeeTypeResponseDto;
import com.eps.module.payment.PayeeType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PayeeTypeMapper {

    PayeeTypeResponseDto toResponseDto(PayeeType payeeType);

    PayeeType toEntity(PayeeTypeRequestDto requestDto);

    void updateEntityFromDto(PayeeTypeRequestDto requestDto, @MappingTarget PayeeType payeeType);
}
