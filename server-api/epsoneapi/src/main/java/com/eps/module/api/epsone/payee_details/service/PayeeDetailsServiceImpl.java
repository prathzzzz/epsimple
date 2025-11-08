package com.eps.module.api.epsone.payee_details.service;

import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsRequestDto;
import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsResponseDto;
import com.eps.module.api.epsone.payee_details.mapper.PayeeDetailsMapper;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.bank.Bank;
import com.eps.module.payment.PayeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayeeDetailsServiceImpl implements PayeeDetailsService {

    private final PayeeDetailsRepository payeeDetailsRepository;
    private final BankRepository bankRepository;
    private final PayeeRepository payeeRepository;
    private final PayeeDetailsMapper payeeDetailsMapper;

    @Override
    @Transactional
    public PayeeDetailsResponseDto createPayeeDetails(PayeeDetailsRequestDto requestDto) {
        // Validate bank if provided
        Bank bank = null;
        if (requestDto.getBankId() != null) {
            bank = bankRepository.findById(requestDto.getBankId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Bank with ID " + requestDto.getBankId() + " not found"));
        }

        // Validate PAN uniqueness if provided
        if (requestDto.getPanNumber() != null && !requestDto.getPanNumber().isEmpty()) {
            if (payeeDetailsRepository.existsByPanNumber(requestDto.getPanNumber())) {
                throw new IllegalArgumentException(
                        "PAN number '" + requestDto.getPanNumber() + "' is already registered");
            }
        }

        // Validate Aadhaar uniqueness if provided
        if (requestDto.getAadhaarNumber() != null && !requestDto.getAadhaarNumber().isEmpty()) {
            if (payeeDetailsRepository.existsByAadhaarNumber(requestDto.getAadhaarNumber())) {
                throw new IllegalArgumentException(
                        "Aadhaar number '" + requestDto.getAadhaarNumber() + "' is already registered");
            }
        }

        // Validate account number uniqueness for the bank if both provided
        if (requestDto.getAccountNumber() != null && !requestDto.getAccountNumber().isEmpty() 
                && requestDto.getBankId() != null) {
            if (payeeDetailsRepository.existsByAccountNumberAndBankId(
                    requestDto.getAccountNumber(), requestDto.getBankId())) {
                throw new IllegalArgumentException(
                        "Account number '" + requestDto.getAccountNumber() + 
                        "' is already registered with " + bank.getBankName());
            }
        }

        PayeeDetails payeeDetails = payeeDetailsMapper.toEntity(requestDto);
        payeeDetails.setBank(bank);

        PayeeDetails savedPayeeDetails = payeeDetailsRepository.save(payeeDetails);
        return payeeDetailsMapper.toDto(savedPayeeDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeDetailsResponseDto> getAllPayeeDetails(Pageable pageable) {
        Page<PayeeDetails> payeeDetailsPage = payeeDetailsRepository.findAll(pageable);
        return payeeDetailsPage.map(payeeDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeDetailsResponseDto> searchPayeeDetails(String searchTerm, Pageable pageable) {
        Page<PayeeDetails> payeeDetailsPage = payeeDetailsRepository.searchPayeeDetails(searchTerm, pageable);
        return payeeDetailsPage.map(payeeDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayeeDetailsResponseDto> getPayeeDetailsList() {
        List<PayeeDetails> payeeDetailsList = payeeDetailsRepository.findAllList();
        return payeeDetailsList.stream()
                .map(payeeDetailsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PayeeDetailsResponseDto getPayeeDetailsById(Long id) {
        PayeeDetails payeeDetails = payeeDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payee details with ID " + id + " not found"));
        return payeeDetailsMapper.toDto(payeeDetails);
    }

    @Override
    @Transactional
    public PayeeDetailsResponseDto updatePayeeDetails(Long id, PayeeDetailsRequestDto requestDto) {
        PayeeDetails existingPayeeDetails = payeeDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payee details with ID " + id + " not found"));

        // Validate bank if provided
        Bank bank = null;
        if (requestDto.getBankId() != null) {
            bank = bankRepository.findById(requestDto.getBankId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Bank with ID " + requestDto.getBankId() + " not found"));
        }

        // Validate PAN uniqueness if changed
        if (requestDto.getPanNumber() != null && !requestDto.getPanNumber().isEmpty()) {
            if (payeeDetailsRepository.existsByPanNumberAndIdNot(requestDto.getPanNumber(), id)) {
                throw new IllegalArgumentException(
                        "PAN number '" + requestDto.getPanNumber() + "' is already registered with another payee");
            }
        }

        // Validate Aadhaar uniqueness if changed
        if (requestDto.getAadhaarNumber() != null && !requestDto.getAadhaarNumber().isEmpty()) {
            if (payeeDetailsRepository.existsByAadhaarNumberAndIdNot(requestDto.getAadhaarNumber(), id)) {
                throw new IllegalArgumentException(
                        "Aadhaar number '" + requestDto.getAadhaarNumber() + "' is already registered with another payee");
            }
        }

        // Validate account number uniqueness for the bank if both provided
        if (requestDto.getAccountNumber() != null && !requestDto.getAccountNumber().isEmpty() 
                && requestDto.getBankId() != null) {
            if (payeeDetailsRepository.existsByAccountNumberAndBankIdAndIdNot(
                    requestDto.getAccountNumber(), requestDto.getBankId(), id)) {
                throw new IllegalArgumentException(
                        "Account number '" + requestDto.getAccountNumber() + 
                        "' is already registered with " + bank.getBankName() + " for another payee");
            }
        }

        // Update fields
        existingPayeeDetails.setPayeeName(requestDto.getPayeeName());
        existingPayeeDetails.setPanNumber(requestDto.getPanNumber());
        existingPayeeDetails.setAadhaarNumber(requestDto.getAadhaarNumber());
        existingPayeeDetails.setBank(bank);
        existingPayeeDetails.setIfscCode(requestDto.getIfscCode());
        existingPayeeDetails.setBeneficiaryName(requestDto.getBeneficiaryName());
        existingPayeeDetails.setAccountNumber(requestDto.getAccountNumber());

        PayeeDetails updatedPayeeDetails = payeeDetailsRepository.save(existingPayeeDetails);
        return payeeDetailsMapper.toDto(updatedPayeeDetails);
    }

    @Override
    @Transactional
    public void deletePayeeDetails(Long id) {
        if (!payeeDetailsRepository.existsById(id)) {
            throw new IllegalArgumentException("Payee details with ID " + id + " not found");
        }

        // Check for dependencies - payees
        long payeeCount = payeeRepository.countByPayeeDetailsId(id);
        if (payeeCount > 0) {
            throw new IllegalStateException("Cannot delete payee details as it has " + payeeCount + " associated payees");
        }
        
        payeeDetailsRepository.deleteById(id);
    }
}
