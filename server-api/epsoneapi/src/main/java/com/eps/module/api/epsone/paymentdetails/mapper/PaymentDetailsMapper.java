package com.eps.module.api.epsone.paymentdetails.mapper;

import com.eps.module.api.epsone.paymentdetails.dto.PaymentDetailsRequestDto;
import com.eps.module.api.epsone.paymentdetails.dto.PaymentDetailsResponseDto;
import com.eps.module.payment.PaymentDetails;
import com.eps.module.payment.PaymentMethod;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentDetailsMapper {

    @Mapping(target = "paymentMethodId", source = "paymentMethod.id")
    @Mapping(target = "paymentMethodName", source = "paymentMethod.methodName")
    PaymentDetailsResponseDto toResponseDto(PaymentDetails paymentDetails);

    @Mapping(target = "paymentMethod", source = "paymentMethodId", qualifiedByName = "mapPaymentMethod")
    PaymentDetails toEntity(PaymentDetailsRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "paymentMethod", source = "paymentMethodId", qualifiedByName = "mapPaymentMethod")
    void updateEntityFromDto(PaymentDetailsRequestDto requestDto, @MappingTarget PaymentDetails paymentDetails);

    @Named("mapPaymentMethod")
    default PaymentMethod mapPaymentMethod(Long paymentMethodId) {
        if (paymentMethodId == null) {
            return null;
        }
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodId);
        return paymentMethod;
    }
}
