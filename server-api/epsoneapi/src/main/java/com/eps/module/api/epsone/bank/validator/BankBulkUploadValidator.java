package com.eps.module.api.epsone.bank.validator;

import com.eps.module.api.epsone.bank.dto.BankBulkUploadDto;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BankBulkUploadValidator implements BulkRowValidator<BankBulkUploadDto> {

    private final BankRepository bankRepository;

    @Override
    public List<BulkUploadErrorDto> validate(BankBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate bank name (required)
        if (dto.getBankName() == null || dto.getBankName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Bank name is required")
                    .build());
        } else if (dto.getBankName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Bank name cannot exceed 255 characters")
                    .build());
        }

        // Validate RBI bank code (optional, but must be valid format if provided)
        if (dto.getRbiBankCode() != null && !dto.getRbiBankCode().trim().isEmpty()) {
            if (dto.getRbiBankCode().length() > 10) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("RBI bank code cannot exceed 10 characters")
                        .build());
            }
            if (!dto.getRbiBankCode().matches("^[A-Za-z0-9]+$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("RBI bank code can only contain letters and numbers")
                        .build());
            }
            // Check for duplicate RBI code
            if (bankRepository.existsByRbiBankCode(dto.getRbiBankCode())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("RBI bank code '" + dto.getRbiBankCode() + "' already exists")
                        .build());
            }
        }

        // Validate EPS bank code (optional)
        if (dto.getEpsBankCode() != null && !dto.getEpsBankCode().trim().isEmpty()) {
            if (dto.getEpsBankCode().length() > 10) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("EPS bank code cannot exceed 10 characters")
                        .build());
            }
            if (!dto.getEpsBankCode().matches("^[A-Za-z0-9_-]+$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("EPS bank code can only contain letters, numbers, hyphens and underscores")
                        .build());
            }
            // Check for duplicate EPS code
            if (bankRepository.existsByEpsBankCode(dto.getEpsBankCode())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("EPS bank code '" + dto.getEpsBankCode() + "' already exists")
                        .build());
            }
        }

        // Validate alternate bank code (optional)
        if (dto.getBankCodeAlt() != null && !dto.getBankCodeAlt().trim().isEmpty()) {
            if (dto.getBankCodeAlt().length() > 10) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Alternate bank code cannot exceed 10 characters")
                        .build());
            }
            if (!dto.getBankCodeAlt().matches("^[A-Za-z0-9_-]+$")) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Alternate bank code can only contain letters, numbers, hyphens and underscores")
                        .build());
            }
            // Check for duplicate alternate code
            if (bankRepository.existsByBankCodeAlt(dto.getBankCodeAlt())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .errorMessage("Alternate bank code '" + dto.getBankCodeAlt() + "' already exists")
                        .build());
            }
        }

        // Validate bank logo URL (optional)
        if (dto.getBankLogo() != null && dto.getBankLogo().length() > 500) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .errorMessage("Bank logo URL cannot exceed 500 characters")
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(BankBulkUploadDto dto) {
        // Check for duplicate bank name (case-insensitive)
        return bankRepository.findByBankNameIgnoreCase(dto.getBankName()).isPresent();
    }
}
