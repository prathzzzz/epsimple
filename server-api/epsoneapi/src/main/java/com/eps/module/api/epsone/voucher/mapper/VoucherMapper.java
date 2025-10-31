package com.eps.module.api.epsone.voucher.mapper;

import com.eps.module.api.epsone.voucher.dto.VoucherRequestDto;
import com.eps.module.api.epsone.voucher.dto.VoucherResponseDto;
import com.eps.module.payment.Voucher;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PaymentDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VoucherMapper {

    @Mapping(target = "payeeId", source = "payee.id")
    @Mapping(target = "payeeName", expression = "java(buildPayeeName(voucher))")
    @Mapping(target = "payeeTypeName", source = "payee.payeeType.payeeType")
    @Mapping(target = "paymentDetailsId", source = "paymentDetails.id")
    @Mapping(target = "transactionNumber", source = "paymentDetails.transactionNumber")
    VoucherResponseDto toResponseDto(Voucher voucher);

    @Mapping(target = "payee", source = "payeeId", qualifiedByName = "mapPayee")
    @Mapping(target = "paymentDetails", source = "paymentDetailsId", qualifiedByName = "mapPaymentDetails")
    Voucher toEntity(VoucherRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "payee", source = "payeeId", qualifiedByName = "mapPayee")
    @Mapping(target = "paymentDetails", source = "paymentDetailsId", qualifiedByName = "mapPaymentDetails")
    void updateEntityFromDto(VoucherRequestDto requestDto, @MappingTarget Voucher voucher);

    @Named("mapPayee")
    default Payee mapPayee(Long payeeId) {
        if (payeeId == null) {
            return null;
        }
        Payee payee = new Payee();
        payee.setId(payeeId);
        return payee;
    }

    @Named("mapPaymentDetails")
    default PaymentDetails mapPaymentDetails(Long paymentDetailsId) {
        if (paymentDetailsId == null) {
            return null;
        }
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setId(paymentDetailsId);
        return paymentDetails;
    }

    default String buildPayeeName(Voucher voucher) {
        if (voucher == null || voucher.getPayee() == null || voucher.getPayee().getPayeeDetails() == null) {
            return null;
        }
        return voucher.getPayee().getPayeeDetails().getPayeeName();
    }
}
