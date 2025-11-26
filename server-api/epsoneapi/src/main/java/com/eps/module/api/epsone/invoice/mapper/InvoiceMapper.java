package com.eps.module.api.epsone.invoice.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.invoice.dto.InvoiceRequestDto;
import com.eps.module.api.epsone.invoice.dto.InvoiceResponseDto;
import com.eps.module.payment.Invoice;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PaymentDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "payeeId", source = "payee.id")
    @Mapping(target = "payeeName", expression = "java(buildPayeeName(invoice))")
    @Mapping(target = "payeeTypeName", source = "payee.payeeType.payeeType")
    @Mapping(target = "paymentDetailsId", source = "paymentDetails.id")
    @Mapping(target = "transactionNumber", source = "paymentDetails.transactionNumber")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    InvoiceResponseDto toResponseDto(Invoice invoice);

    @Mapping(target = "payee", source = "payeeId", qualifiedByName = "mapPayee")
    @Mapping(target = "paymentDetails", source = "paymentDetailsId", qualifiedByName = "mapPaymentDetails")
    Invoice toEntity(InvoiceRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "payee", source = "payeeId", qualifiedByName = "mapPayee")
    @Mapping(target = "paymentDetails", source = "paymentDetailsId", qualifiedByName = "mapPaymentDetails")
    void updateEntityFromDto(InvoiceRequestDto requestDto, @MappingTarget Invoice invoice);

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

    default String buildPayeeName(Invoice invoice) {
        if (invoice == null || invoice.getPayee() == null || invoice.getPayee().getPayeeDetails() == null) {
            return null;
        }
        return invoice.getPayee().getPayeeDetails().getPayeeName();
    }
}
