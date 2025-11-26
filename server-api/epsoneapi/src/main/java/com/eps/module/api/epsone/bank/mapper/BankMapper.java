package com.eps.module.api.epsone.bank.mapper;

import com.eps.module.api.epsone.bank.dto.BankRequestDto;
import com.eps.module.api.epsone.bank.dto.BankResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.bank.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {AuditFieldMapper.class}
)
public interface BankMapper {

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    BankResponseDto toResponseDto(Bank bank);

    List<BankResponseDto> toResponseDtoList(List<Bank> banks);

    Bank toEntity(BankRequestDto bankRequestDto);

    void updateEntityFromDto(BankRequestDto bankRequestDto, @MappingTarget Bank bank);
}
