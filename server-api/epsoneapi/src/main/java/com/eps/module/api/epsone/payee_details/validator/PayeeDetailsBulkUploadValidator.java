package com.eps.module.api.epsone.payee_details.validator;

import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsBulkUploadDto;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.bank.Bank;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.crypto.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PayeeDetailsBulkUploadValidator implements BulkRowValidator<PayeeDetailsBulkUploadDto> {

    private final BankRepository bankRepository;
    private final PayeeDetailsRepository payeeDetailsRepository;
    private final CryptoService cryptoService;

    @Override
    public List<BulkUploadErrorDto> validate(PayeeDetailsBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Payee Name
        if (rowData.getPayeeName() == null || rowData.getPayeeName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Payee Name")
                    .errorMessage("Payee name is required")
                    .rejectedValue(rowData.getPayeeName())
                    .build());
        } else if (rowData.getPayeeName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Payee Name")
                    .errorMessage("Payee name cannot exceed 255 characters")
                    .rejectedValue(rowData.getPayeeName())
                    .build());
        }

        // Validate Bank Name if provided
        if (rowData.getBankName() != null && !rowData.getBankName().trim().isEmpty()) {
            boolean bankExists = bankRepository.findByBankName(rowData.getBankName()).isPresent();
            if (!bankExists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Bank Name")
                        .errorMessage("Bank '" + rowData.getBankName() + "' does not exist")
                        .rejectedValue(rowData.getBankName())
                        .build());
            }
        }

        // Validate PAN uniqueness if provided
        if (rowData.getPanNumber() != null && !rowData.getPanNumber().trim().isEmpty()) {
            String panHash = cryptoService.hash(rowData.getPanNumber());
            if (payeeDetailsRepository.existsByPanNumberHash(panHash)) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("PAN Number")
                        .errorMessage("PAN number '" + rowData.getPanNumber() + "' is already registered")
                        .rejectedValue(rowData.getPanNumber())
                        .build());
            }
        }

        // Validate Aadhaar uniqueness if provided
        if (rowData.getAadhaarNumber() != null && !rowData.getAadhaarNumber().trim().isEmpty()) {
            String aadhaarHash = cryptoService.hash(rowData.getAadhaarNumber());
            if (payeeDetailsRepository.existsByAadhaarNumberHash(aadhaarHash)) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Aadhaar Number")
                        .errorMessage("Aadhaar number '" + rowData.getAadhaarNumber() + "' is already registered")
                        .rejectedValue(rowData.getAadhaarNumber())
                        .build());
            }
        }

        // Validate account number uniqueness for the bank if both provided
        if (rowData.getAccountNumber() != null && !rowData.getAccountNumber().trim().isEmpty() 
                && rowData.getBankName() != null && !rowData.getBankName().trim().isEmpty()) {
            Bank bank = bankRepository.findByBankName(rowData.getBankName()).orElse(null);
            if (bank != null) {
                String accountNumberHash = cryptoService.hash(rowData.getAccountNumber());
                if (payeeDetailsRepository.existsByAccountNumberHashAndBankId(
                        accountNumberHash, bank.getId())) {
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .fieldName("Account Number")
                            .errorMessage("Account number '" + rowData.getAccountNumber() + 
                                    "' is already registered with " + bank.getBankName())
                            .rejectedValue(rowData.getAccountNumber())
                            .build());
                }
            }
        }

        // Validate field lengths
        if (rowData.getPanNumber() != null && rowData.getPanNumber().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("PAN Number")
                    .errorMessage("PAN number cannot exceed 255 characters")
                    .rejectedValue(rowData.getPanNumber())
                    .build());
        }

        if (rowData.getAadhaarNumber() != null && rowData.getAadhaarNumber().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Aadhaar Number")
                    .errorMessage("Aadhaar number cannot exceed 255 characters")
                    .rejectedValue(rowData.getAadhaarNumber())
                    .build());
        }

        if (rowData.getIfscCode() != null && rowData.getIfscCode().length() > 20) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("IFSC Code")
                    .errorMessage("IFSC code cannot exceed 20 characters")
                    .rejectedValue(rowData.getIfscCode())
                    .build());
        }

        if (rowData.getBeneficiaryName() != null && rowData.getBeneficiaryName().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Beneficiary Name")
                    .errorMessage("Beneficiary name cannot exceed 255 characters")
                    .rejectedValue(rowData.getBeneficiaryName())
                    .build());
        }

        if (rowData.getAccountNumber() != null && rowData.getAccountNumber().length() > 255) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Account Number")
                    .errorMessage("Account number cannot exceed 255 characters")
                    .rejectedValue(rowData.getAccountNumber())
                    .build());
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(PayeeDetailsBulkUploadDto rowData) {
        // Check for duplicates in the database
        // PAN number is the primary unique identifier
        if (rowData.getPanNumber() != null && !rowData.getPanNumber().trim().isEmpty()) {
            String panHash = cryptoService.hash(rowData.getPanNumber());
            return payeeDetailsRepository.existsByPanNumberHash(panHash);
        }
        
        // If no PAN, check Aadhaar
        if (rowData.getAadhaarNumber() != null && !rowData.getAadhaarNumber().trim().isEmpty()) {
            String aadhaarHash = cryptoService.hash(rowData.getAadhaarNumber());
            return payeeDetailsRepository.existsByAadhaarNumberHash(aadhaarHash);
        }
        
        // If no PAN or Aadhaar, check account number + bank
        if (rowData.getAccountNumber() != null && !rowData.getAccountNumber().trim().isEmpty() 
                && rowData.getBankName() != null && !rowData.getBankName().trim().isEmpty()) {
            Bank bank = bankRepository.findByBankName(rowData.getBankName()).orElse(null);
            if (bank != null) {
                String accountNumberHash = cryptoService.hash(rowData.getAccountNumber());
                return payeeDetailsRepository.existsByAccountNumberHashAndBankId(
                        accountNumberHash, bank.getId());
            }
        }
        
        return false;
    }
}
