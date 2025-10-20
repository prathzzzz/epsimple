package com.eps.module.api.epsone.bank.mapper;

import com.eps.module.api.epsone.bank.dto.BankRequestDto;
import com.eps.module.api.epsone.bank.dto.BankResponseDto;
import com.eps.module.bank.Bank;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-20T16:41:11+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class BankMapperImpl implements BankMapper {

    @Override
    public BankResponseDto toResponseDto(Bank bank) {
        if ( bank == null ) {
            return null;
        }

        BankResponseDto.BankResponseDtoBuilder bankResponseDto = BankResponseDto.builder();

        bankResponseDto.id( bank.getId() );
        bankResponseDto.bankName( bank.getBankName() );
        bankResponseDto.rbiBankCode( bank.getRbiBankCode() );
        bankResponseDto.epsBankCode( bank.getEpsBankCode() );
        bankResponseDto.bankCodeAlt( bank.getBankCodeAlt() );
        bankResponseDto.bankLogo( bank.getBankLogo() );
        bankResponseDto.createdAt( bank.getCreatedAt() );
        bankResponseDto.updatedAt( bank.getUpdatedAt() );

        return bankResponseDto.build();
    }

    @Override
    public List<BankResponseDto> toResponseDtoList(List<Bank> banks) {
        if ( banks == null ) {
            return null;
        }

        List<BankResponseDto> list = new ArrayList<BankResponseDto>( banks.size() );
        for ( Bank bank : banks ) {
            list.add( toResponseDto( bank ) );
        }

        return list;
    }

    @Override
    public Bank toEntity(BankRequestDto bankRequestDto) {
        if ( bankRequestDto == null ) {
            return null;
        }

        Bank.BankBuilder bank = Bank.builder();

        bank.bankName( bankRequestDto.getBankName() );
        bank.rbiBankCode( bankRequestDto.getRbiBankCode() );
        bank.epsBankCode( bankRequestDto.getEpsBankCode() );
        bank.bankCodeAlt( bankRequestDto.getBankCodeAlt() );
        bank.bankLogo( bankRequestDto.getBankLogo() );

        return bank.build();
    }

    @Override
    public void updateEntityFromDto(BankRequestDto bankRequestDto, Bank bank) {
        if ( bankRequestDto == null ) {
            return;
        }

        if ( bankRequestDto.getBankName() != null ) {
            bank.setBankName( bankRequestDto.getBankName() );
        }
        if ( bankRequestDto.getRbiBankCode() != null ) {
            bank.setRbiBankCode( bankRequestDto.getRbiBankCode() );
        }
        if ( bankRequestDto.getEpsBankCode() != null ) {
            bank.setEpsBankCode( bankRequestDto.getEpsBankCode() );
        }
        if ( bankRequestDto.getBankCodeAlt() != null ) {
            bank.setBankCodeAlt( bankRequestDto.getBankCodeAlt() );
        }
        if ( bankRequestDto.getBankLogo() != null ) {
            bank.setBankLogo( bankRequestDto.getBankLogo() );
        }
    }
}
