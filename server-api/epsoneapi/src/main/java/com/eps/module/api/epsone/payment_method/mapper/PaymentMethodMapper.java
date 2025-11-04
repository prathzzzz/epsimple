package com.eps.module.api.epsone.payment_method.mapper;

import com.eps.module.api.epsone.payment_method.dto.PaymentMethodRequestDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodResponseDto;
import com.eps.module.payment.PaymentMethod;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMethodMapper {

    PaymentMethodResponseDto toResponseDto(PaymentMethod paymentMethod);

    PaymentMethod toEntity(PaymentMethodRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PaymentMethodRequestDto requestDto, @MappingTarget PaymentMethod paymentMethod);
}
