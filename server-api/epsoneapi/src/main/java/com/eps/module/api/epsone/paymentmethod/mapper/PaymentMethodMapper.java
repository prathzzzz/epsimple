package com.eps.module.api.epsone.paymentmethod.mapper;

import com.eps.module.api.epsone.paymentmethod.dto.PaymentMethodRequestDto;
import com.eps.module.api.epsone.paymentmethod.dto.PaymentMethodResponseDto;
import com.eps.module.payment.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodResponseDto toResponseDto(PaymentMethod paymentMethod);

    PaymentMethod toEntity(PaymentMethodRequestDto requestDto);

    void updateEntityFromDto(PaymentMethodRequestDto requestDto, @MappingTarget PaymentMethod paymentMethod);
}
