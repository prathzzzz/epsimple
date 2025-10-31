package com.eps.module.api.epsone.voucher.service;

import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.paymentdetails.repository.PaymentDetailsRepository;
import com.eps.module.api.epsone.voucher.dto.VoucherRequestDto;
import com.eps.module.api.epsone.voucher.dto.VoucherResponseDto;
import com.eps.module.api.epsone.voucher.mapper.VoucherMapper;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
import com.eps.module.payment.Voucher;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PaymentDetails;
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
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final PayeeRepository payeeRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;

    @Override
    @Transactional
    public VoucherResponseDto createVoucher(VoucherRequestDto requestDto) {
        log.info("Creating new voucher: {}", requestDto.getVoucherNumber());

        // Validate unique voucher number
        if (voucherRepository.existsByVoucherNumberIgnoreCase(requestDto.getVoucherNumber())) {
            throw new IllegalArgumentException("Voucher number '" + requestDto.getVoucherNumber() + "' already exists");
        }

        // Validate payee exists
        Payee payee = payeeRepository.findById(requestDto.getPayeeId())
                .orElseThrow(() -> new IllegalArgumentException("Payee not found with ID: " + requestDto.getPayeeId()));

        // Validate payment details if provided
        if (requestDto.getPaymentDetailsId() != null) {
            PaymentDetails paymentDetails = paymentDetailsRepository.findById(requestDto.getPaymentDetailsId())
                    .orElseThrow(() -> new IllegalArgumentException("Payment details not found with ID: " + requestDto.getPaymentDetailsId()));
        }

        Voucher voucher = voucherMapper.toEntity(requestDto);
        voucher.setPayee(payee);
        
        Voucher savedVoucher = voucherRepository.save(voucher);
        log.info("Voucher created successfully with ID: {}", savedVoucher.getId());

        return voucherMapper.toResponseDto(savedVoucher);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherResponseDto> getAllVouchers(Pageable pageable) {
        log.info("Fetching all vouchers with pagination: {}", pageable);
        Page<Voucher> vouchers = voucherRepository.findAll(pageable);
        return vouchers.map(voucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherResponseDto> searchVouchers(String searchTerm, Pageable pageable) {
        log.info("Searching vouchers with term: {}", searchTerm);
        Page<Voucher> vouchers = voucherRepository.searchVouchers(searchTerm, pageable);
        return vouchers.map(voucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoucherResponseDto> getVouchersList() {
        log.info("Fetching all vouchers as list");
        return voucherRepository.findAll().stream()
                .map(voucherMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherResponseDto> getVouchersByPayee(Long payeeId, Pageable pageable) {
        log.info("Fetching vouchers for payee ID: {}", payeeId);
        
        // Validate payee exists
        if (!payeeRepository.existsById(payeeId)) {
            throw new IllegalArgumentException("Payee not found with ID: " + payeeId);
        }

        Page<Voucher> vouchers = voucherRepository.findByPayeeId(payeeId, pageable);
        return vouchers.map(voucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherResponseDto getVoucherById(Long id) {
        log.info("Fetching voucher by ID: {}", id);
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found with ID: " + id));
        return voucherMapper.toResponseDto(voucher);
    }

    @Override
    @Transactional
    public VoucherResponseDto updateVoucher(Long id, VoucherRequestDto requestDto) {
        log.info("Updating voucher ID: {}", id);

        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found with ID: " + id));

        // Validate unique voucher number if changed
        if (!voucher.getVoucherNumber().equalsIgnoreCase(requestDto.getVoucherNumber()) &&
            voucherRepository.existsByVoucherNumberIgnoreCaseAndIdNot(requestDto.getVoucherNumber(), id)) {
            throw new IllegalArgumentException("Voucher number '" + requestDto.getVoucherNumber() + "' already exists");
        }

        // Validate payee exists
        Payee payee = payeeRepository.findById(requestDto.getPayeeId())
                .orElseThrow(() -> new IllegalArgumentException("Payee not found with ID: " + requestDto.getPayeeId()));

        // Validate payment details if provided
        if (requestDto.getPaymentDetailsId() != null) {
            PaymentDetails paymentDetails = paymentDetailsRepository.findById(requestDto.getPaymentDetailsId())
                    .orElseThrow(() -> new IllegalArgumentException("Payment details not found with ID: " + requestDto.getPaymentDetailsId()));
        }

        voucherMapper.updateEntityFromDto(requestDto, voucher);
        voucher.setPayee(payee);

        Voucher updatedVoucher = voucherRepository.save(voucher);
        log.info("Voucher updated successfully: {}", id);

        return voucherMapper.toResponseDto(updatedVoucher);
    }

    @Override
    @Transactional
    public VoucherResponseDto updatePaymentStatus(Long id, String paymentStatus) {
        log.info("Updating payment status for voucher ID: {} to {}", id, paymentStatus);

        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voucher not found with ID: " + id));

        voucher.setPaymentStatus(paymentStatus);
        Voucher updatedVoucher = voucherRepository.save(voucher);

        log.info("Payment status updated successfully for voucher ID: {}", id);
        return voucherMapper.toResponseDto(updatedVoucher);
    }

    @Override
    @Transactional
    public void deleteVoucher(Long id) {
        log.info("Deleting voucher ID: {}", id);

        if (!voucherRepository.existsById(id)) {
            throw new IllegalArgumentException("Voucher not found with ID: " + id);
        }

        voucherRepository.deleteById(id);
        log.info("Voucher deleted successfully: {}", id);
    }
}
