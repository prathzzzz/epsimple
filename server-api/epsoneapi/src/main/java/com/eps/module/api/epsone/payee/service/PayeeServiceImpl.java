package com.eps.module.api.epsone.payee.service;

import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.payee.dto.PayeeRequestDto;
import com.eps.module.api.epsone.payee.dto.PayeeResponseDto;
import com.eps.module.api.epsone.payee.mapper.PayeeMapper;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.payee_type.repository.PayeeTypeRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PayeeDetails;
import com.eps.module.payment.PayeeType;
import com.eps.module.vendor.Landlord;
import com.eps.module.vendor.Vendor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayeeServiceImpl implements PayeeService {

    private final PayeeRepository payeeRepository;
    private final PayeeTypeRepository payeeTypeRepository;
    private final PayeeDetailsRepository payeeDetailsRepository;
    private final VendorRepository vendorRepository;
    private final LandlordRepository landlordRepository;
    private final PayeeMapper payeeMapper;

    @Override
    @Transactional
    public PayeeResponseDto createPayee(PayeeRequestDto requestDto) {
        log.info("Creating new payee with payeeTypeId: {}, payeeDetailsId: {}", 
                requestDto.getPayeeTypeId(), requestDto.getPayeeDetailsId());

        // Validate payee type exists
        PayeeType payeeType = payeeTypeRepository.findById(requestDto.getPayeeTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payee type not found with id: " + requestDto.getPayeeTypeId()));

        // Validate payee details exists
        PayeeDetails payeeDetails = payeeDetailsRepository.findById(requestDto.getPayeeDetailsId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payee details not found with id: " + requestDto.getPayeeDetailsId()));

        // Check if payee details already used
        if (payeeRepository.countByPayeeDetailsId(requestDto.getPayeeDetailsId()) > 0) {
            throw new IllegalArgumentException(
                    "Payee details with id " + requestDto.getPayeeDetailsId() + " is already assigned to another payee");
        }

        // Validate vendor if provided
        Vendor vendor = null;
        if (requestDto.getVendorId() != null) {
            vendor = vendorRepository.findById(requestDto.getVendorId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Vendor not found with id: " + requestDto.getVendorId()));
        }

        // Validate landlord if provided
        Landlord landlord = null;
        if (requestDto.getLandlordId() != null) {
            landlord = landlordRepository.findById(requestDto.getLandlordId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Landlord not found with id: " + requestDto.getLandlordId()));
        }

        Payee payee = payeeMapper.toEntity(requestDto, payeeType, payeeDetails, vendor, landlord);
        Payee savedPayee = payeeRepository.save(payee);

        log.info("Payee created successfully with id: {}", savedPayee.getId());
        return payeeMapper.toDto(savedPayee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeResponseDto> getAllPayees(Pageable pageable) {
        log.info("Fetching all payees with pagination");
        Page<Payee> payees = payeeRepository.findAllWithDetails(pageable);
        return payees.map(payeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeResponseDto> searchPayees(String searchTerm, Pageable pageable) {
        log.info("Searching payees with term: {}", searchTerm);
        Page<Payee> payees = payeeRepository.searchPayees(searchTerm, pageable);
        return payees.map(payeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayeeResponseDto> getPayeesList() {
        log.info("Fetching all payees as list");
        List<Payee> payees = payeeRepository.findAll();
        return payees.stream()
                .map(payeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PayeeResponseDto getPayeeById(Long id) {
        log.info("Fetching payee by id: {}", id);
        Payee payee = payeeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Payee not found with id: " + id));
        return payeeMapper.toDto(payee);
    }

    @Override
    @Transactional
    public PayeeResponseDto updatePayee(Long id, PayeeRequestDto requestDto) {
        log.info("Updating payee with id: {}", id);

        Payee payee = payeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payee not found with id: " + id));

        // Validate payee type exists
        PayeeType payeeType = payeeTypeRepository.findById(requestDto.getPayeeTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payee type not found with id: " + requestDto.getPayeeTypeId()));

        // Validate payee details exists
        PayeeDetails payeeDetails = payeeDetailsRepository.findById(requestDto.getPayeeDetailsId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payee details not found with id: " + requestDto.getPayeeDetailsId()));

        // Check if payee details already used by another payee
        long count = payeeRepository.countByPayeeDetailsId(requestDto.getPayeeDetailsId());
        if (count > 0 && !payee.getPayeeDetails().getId().equals(requestDto.getPayeeDetailsId())) {
            throw new IllegalArgumentException(
                    "Payee details with id " + requestDto.getPayeeDetailsId() + " is already assigned to another payee");
        }

        // Validate vendor if provided
        Vendor vendor = null;
        if (requestDto.getVendorId() != null) {
            vendor = vendorRepository.findById(requestDto.getVendorId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Vendor not found with id: " + requestDto.getVendorId()));
        }

        // Validate landlord if provided
        Landlord landlord = null;
        if (requestDto.getLandlordId() != null) {
            landlord = landlordRepository.findById(requestDto.getLandlordId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Landlord not found with id: " + requestDto.getLandlordId()));
        }

        payeeMapper.updateEntity(payee, requestDto, payeeType, payeeDetails, vendor, landlord);
        Payee updatedPayee = payeeRepository.save(payee);

        log.info("Payee updated successfully with id: {}", updatedPayee.getId());
        return payeeMapper.toDto(updatedPayee);
    }

    @Override
    @Transactional
    public void deletePayee(Long id) {
        log.info("Deleting payee with id: {}", id);

        if (!payeeRepository.existsById(id)) {
            throw new IllegalArgumentException("Payee not found with id: " + id);
        }

        // TODO: Add dependency checks when invoice/voucher modules are implemented

        payeeRepository.deleteById(id);
        log.info("Payee deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPayees() {
        return payeeRepository.count();
    }
}
