package com.eps.module.api.epsone.payee_details.mapper;

import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsRequestDto;
import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsResponseDto;
import com.eps.module.crypto.service.CryptoService;
import com.eps.module.payment.PayeeDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.Builder;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public abstract class PayeeDetailsMapper {

    @Autowired
    protected CryptoService cryptoService;

    @Mapping(target = "bank", ignore = true)
    @Mapping(target = "panNumber", ignore = true)
    @Mapping(target = "panNumberHash", ignore = true)
    @Mapping(target = "aadhaarNumber", ignore = true)
    @Mapping(target = "aadhaarNumberHash", ignore = true)
    @Mapping(target = "beneficiaryName", ignore = true)
    @Mapping(target = "beneficiaryNameHash", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "accountNumberHash", ignore = true)
    public abstract PayeeDetails toEntity(PayeeDetailsRequestDto dto);

    @AfterMapping
    protected void encryptSensitiveFields(PayeeDetailsRequestDto source, @MappingTarget PayeeDetails target) {
        encryptAndHashField(source.getPanNumber(), target::setPanNumber, target::setPanNumberHash);
        encryptAndHashField(source.getAadhaarNumber(), target::setAadhaarNumber, target::setAadhaarNumberHash);
        encryptAndHashField(source.getBeneficiaryName(), target::setBeneficiaryName, target::setBeneficiaryNameHash);
        encryptAndHashField(source.getAccountNumber(), target::setAccountNumber, target::setAccountNumberHash);
    }

    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "bank.bankName", target = "bankName")
    @Mapping(target = "panNumber", source = "panNumber", qualifiedByName = "decryptField")
    @Mapping(target = "aadhaarNumber", source = "aadhaarNumber", qualifiedByName = "decryptField")
    @Mapping(target = "beneficiaryName", source = "beneficiaryName", qualifiedByName = "decryptField")
    @Mapping(target = "accountNumber", source = "accountNumber", qualifiedByName = "decryptField")
    public abstract PayeeDetailsResponseDto toDto(PayeeDetails entity);

    @Named("decryptField")
    protected String decryptField(String encryptedValue) {
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            return encryptedValue;
        }
        return cryptoService.decrypt(encryptedValue);
    }

    protected void encryptAndHashField(String plaintext, java.util.function.Consumer<String> encryptedSetter, java.util.function.Consumer<String> hashSetter) {
        if (plaintext == null || plaintext.isEmpty()) {
            encryptedSetter.accept(plaintext);
            hashSetter.accept(null);
            return;
        }
        CryptoService.EncryptedData encryptedData = cryptoService.encryptWithHash(plaintext);
        encryptedSetter.accept(encryptedData.encryptedValue());
        hashSetter.accept(encryptedData.hash());
    }
}
