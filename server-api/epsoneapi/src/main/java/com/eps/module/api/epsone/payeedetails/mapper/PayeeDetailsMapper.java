package com.eps.module.api.epsone.payeedetails.mapper;

import com.eps.module.api.epsone.payeedetails.dto.PayeeDetailsRequestDto;
import com.eps.module.api.epsone.payeedetails.dto.PayeeDetailsResponseDto;
import com.eps.module.payment.PayeeDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayeeDetailsMapper {

    @Mapping(target = "bank", ignore = true)
    PayeeDetails toEntity(PayeeDetailsRequestDto dto);

    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "bank.bankName", target = "bankName")
    PayeeDetailsResponseDto toDto(PayeeDetails entity);
}
