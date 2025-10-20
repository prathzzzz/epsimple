package com.eps.module.api.epsone.bank.mapper;

import com.eps.module.api.epsone.bank.dto.BankRequestDto;
import com.eps.module.api.epsone.bank.dto.BankResponseDto;
import com.eps.module.bank.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BankMapper {

    BankResponseDto toResponseDto(Bank bank);

    List<BankResponseDto> toResponseDtoList(List<Bank> banks);

    @Mapping(target = "id", ignore = true)
    Bank toEntity(BankRequestDto bankRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BankRequestDto bankRequestDto, @MappingTarget Bank bank);
}
