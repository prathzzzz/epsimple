package com.eps.module.api.epsone.payment_details.mapper;

import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsRequestDto;
import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsResponseDto;
import com.eps.module.crypto.service.CryptoService;
import com.eps.module.payment.PaymentDetails;
import com.eps.module.payment.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public abstract class PaymentDetailsMapper {

    @Autowired
    protected CryptoService cryptoService;

    @Mapping(target = "paymentMethodId", source = "paymentMethod.id")
    @Mapping(target = "paymentMethodName", source = "paymentMethod.methodName")
    @Mapping(target = "vpa", source = "vpa", qualifiedByName = "decryptField")
    @Mapping(target = "beneficiaryName", source = "beneficiaryName", qualifiedByName = "decryptField")
    @Mapping(target = "beneficiaryAccountNumber", source = "beneficiaryAccountNumber", qualifiedByName = "decryptField")
    public abstract PaymentDetailsResponseDto toResponseDto(PaymentDetails paymentDetails);

    @Mapping(target = "paymentMethod", source = "paymentMethodId", qualifiedByName = "mapPaymentMethod")
    @Mapping(target = "vpa", ignore = true)
    @Mapping(target = "vpaHash", ignore = true)
    @Mapping(target = "beneficiaryName", ignore = true)
    @Mapping(target = "beneficiaryNameHash", ignore = true)
    @Mapping(target = "beneficiaryAccountNumber", ignore = true)
    @Mapping(target = "beneficiaryAccountNumberHash", ignore = true)
    public abstract PaymentDetails toEntity(PaymentDetailsRequestDto requestDto);

    @AfterMapping
    protected void encryptSensitiveFields(PaymentDetailsRequestDto source, @MappingTarget PaymentDetails target) {
        log.debug("=== @AfterMapping encryptSensitiveFields called ===");
        log.debug("Source DTO - vpa: '{}', beneficiaryName: '{}', beneficiaryAccountNumber: '{}'", 
            source.getVpa(), source.getBeneficiaryName(), source.getBeneficiaryAccountNumber());
        
        encryptAndHashField(source.getVpa(), target::setVpa, target::setVpaHash);
        encryptAndHashField(source.getBeneficiaryName(), target::setBeneficiaryName, target::setBeneficiaryNameHash);
        encryptAndHashField(source.getBeneficiaryAccountNumber(), target::setBeneficiaryAccountNumber, target::setBeneficiaryAccountNumberHash);
        
        log.debug("After encryption - vpa: '{}', vpaHash: '{}'", target.getVpa(), target.getVpaHash());
        log.debug("After encryption - beneficiaryName: '{}', beneficiaryNameHash: '{}'", target.getBeneficiaryName(), target.getBeneficiaryNameHash());
        log.debug("=== @AfterMapping encryptSensitiveFields completed ===");
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "paymentMethod", source = "paymentMethodId", qualifiedByName = "mapPaymentMethod")
    @Mapping(target = "vpa", ignore = true)
    @Mapping(target = "vpaHash", ignore = true)
    @Mapping(target = "beneficiaryName", ignore = true)
    @Mapping(target = "beneficiaryNameHash", ignore = true)
    @Mapping(target = "beneficiaryAccountNumber", ignore = true)
    @Mapping(target = "beneficiaryAccountNumberHash", ignore = true)
    public abstract void updateEntityFromDto(PaymentDetailsRequestDto requestDto, @MappingTarget PaymentDetails paymentDetails);

    @AfterMapping
    protected void encryptSensitiveFieldsForUpdate(PaymentDetailsRequestDto source, @MappingTarget PaymentDetails target) {
        if (source.getVpa() != null) {
            encryptAndHashField(source.getVpa(), target::setVpa, target::setVpaHash);
        }
        if (source.getBeneficiaryName() != null) {
            encryptAndHashField(source.getBeneficiaryName(), target::setBeneficiaryName, target::setBeneficiaryNameHash);
        }
        if (source.getBeneficiaryAccountNumber() != null) {
            encryptAndHashField(source.getBeneficiaryAccountNumber(), target::setBeneficiaryAccountNumber, target::setBeneficiaryAccountNumberHash);
        }
    }

    @Named("mapPaymentMethod")
    public PaymentMethod mapPaymentMethod(Long paymentMethodId) {
        if (paymentMethodId == null) {
            return null;
        }
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(paymentMethodId);
        return paymentMethod;
    }

    @Named("decryptField")
    protected String decryptField(String encryptedValue) {
        log.debug("decryptField called with value: '{}'", encryptedValue);
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            log.debug("Value is null or empty, returning as-is");
            return encryptedValue;
        }
        log.debug("Calling cryptoService.decrypt for value length: {}", encryptedValue.length());
        return cryptoService.decrypt(encryptedValue);
    }

    protected void encryptAndHashField(String plaintext, java.util.function.Consumer<String> encryptedSetter, java.util.function.Consumer<String> hashSetter) {
        log.debug("encryptAndHashField called with plaintext: '{}'", plaintext);
        if (plaintext == null || plaintext.isEmpty()) {
            encryptedSetter.accept(plaintext);
            hashSetter.accept(null);
            return;
        }
        log.debug("Encrypting and hashing plaintext");
        CryptoService.EncryptedData encryptedData = cryptoService.encryptWithHash(plaintext);
        log.debug("Encrypted data: {}, hash: {}", encryptedData.encryptedValue(), encryptedData.hash());
        encryptedSetter.accept(encryptedData.encryptedValue());
        hashSetter.accept(encryptedData.hash());
    }
}
