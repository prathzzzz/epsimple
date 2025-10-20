package com.eps.module.api.epsone.bank.helper;

import com.eps.module.api.epsone.bank.dto.BankRequestDto;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.bank.Bank;
import com.eps.module.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Helper utilities for Bank domain: validation and minor utilities.
 */
@Component
@RequiredArgsConstructor
public class BankHelper {

    private final BankRepository bankRepository;

    /**
     * Validate uniqueness constraints for a new Bank creation request.
     */
    public void validateBankUniqueness(BankRequestDto bankRequestDto) {
        if (bankRequestDto.getBankName() != null) {
            bankRepository.findByBankName(bankRequestDto.getBankName())
                .ifPresent(bank -> {
                    throw new CustomException("Bank with name '" + bankRequestDto.getBankName() + "' already exists");
                });
        }

        if (bankRequestDto.getRbiBankCode() != null && !bankRequestDto.getRbiBankCode().isEmpty()) {
            bankRepository.findByRbiBankCode(bankRequestDto.getRbiBankCode())
                .ifPresent(bank -> {
                    throw new CustomException("Bank with RBI code '" + bankRequestDto.getRbiBankCode() + "' already exists");
                });
        }

        if (bankRequestDto.getEpsBankCode() != null && !bankRequestDto.getEpsBankCode().isEmpty()) {
            bankRepository.findByEpsBankCode(bankRequestDto.getEpsBankCode())
                .ifPresent(bank -> {
                    throw new CustomException("Bank with EPS code '" + bankRequestDto.getEpsBankCode() + "' already exists");
                });
        }

        if (bankRequestDto.getBankCodeAlt() != null && !bankRequestDto.getBankCodeAlt().isEmpty()) {
            bankRepository.findByBankCodeAlt(bankRequestDto.getBankCodeAlt())
                .ifPresent(bank -> {
                    throw new CustomException("Bank with alternate code '" + bankRequestDto.getBankCodeAlt() + "' already exists");
                });
        }
    }

    /**
     * Validate uniqueness constraints when updating an existing Bank.
     */
    public void validateBankUniquenessForUpdate(Long id, BankRequestDto bankRequestDto, Bank existingBank) {
        if (bankRequestDto.getBankName() != null &&
            !bankRequestDto.getBankName().equals(existingBank.getBankName())) {
            bankRepository.findByBankName(bankRequestDto.getBankName())
                .ifPresent(bank -> {
                    throw new CustomException("Bank with name '" + bankRequestDto.getBankName() + "' already exists");
                });
        }

        if (bankRequestDto.getRbiBankCode() != null &&
            !bankRequestDto.getRbiBankCode().equals(existingBank.getRbiBankCode())) {
            bankRepository.findByRbiBankCode(bankRequestDto.getRbiBankCode())
                .ifPresent(bank -> {
                    throw new CustomException("Bank with RBI code '" + bankRequestDto.getRbiBankCode() + "' already exists");
                });
        }

        if (bankRequestDto.getEpsBankCode() != null &&
            !bankRequestDto.getEpsBankCode().equals(existingBank.getEpsBankCode())) {
            bankRepository.findByEpsBankCode(bankRequestDto.getEpsBankCode())
                .ifPresent(bank -> {
                    throw new CustomException("Bank with EPS code '" + bankRequestDto.getEpsBankCode() + "' already exists");
                });
        }

        if (bankRequestDto.getBankCodeAlt() != null &&
            !bankRequestDto.getBankCodeAlt().equals(existingBank.getBankCodeAlt())) {
            bankRepository.findByBankCodeAlt(bankRequestDto.getBankCodeAlt())
                .ifPresent(bank -> {
                    throw new CustomException("Bank with alternate code '" + bankRequestDto.getBankCodeAlt() + "' already exists");
                });
        }
    }

    /**
     * Extract file path from URL like: http://localhost:8080/api/files/banks/logos/filename.png
     * Returns: banks/logos/filename.png
     */
    public String extractFilePathFromUrl(String fileUrl) {
        if (fileUrl == null) return null;
        if (fileUrl.contains("/api/files/")) {
            return fileUrl.substring(fileUrl.indexOf("/api/files/") + 11);
        }
        return fileUrl;
    }
}
