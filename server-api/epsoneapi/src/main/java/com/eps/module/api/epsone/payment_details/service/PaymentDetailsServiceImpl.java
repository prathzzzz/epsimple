package com.eps.module.api.epsone.payment_details.service;

import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsRequestDto;
import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsResponseDto;
import com.eps.module.api.epsone.payment_details.mapper.PaymentDetailsMapper;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.api.epsone.payment_method.repository.PaymentMethodRepository;
import com.eps.module.payment.PaymentDetails;
import com.eps.module.payment.PaymentMethod;
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
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentDetailsMapper paymentDetailsMapper;

    @Override
    @Transactional
    public PaymentDetailsResponseDto createPaymentDetails(PaymentDetailsRequestDto requestDto) {
        log.info("Creating payment details for amount: {}", requestDto.getPaymentAmount());

        // Validate that payment method exists
        PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDto.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment method not found with ID: " + requestDto.getPaymentMethodId()));

        PaymentDetails paymentDetails = paymentDetailsMapper.toEntity(requestDto);
        paymentDetails.setPaymentMethod(paymentMethod);
        
        PaymentDetails savedPaymentDetails = paymentDetailsRepository.save(paymentDetails);

        log.info("Payment details created successfully with ID: {}", savedPaymentDetails.getId());
        return paymentDetailsMapper.toResponseDto(savedPaymentDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDetailsResponseDto> getAllPaymentDetails(Pageable pageable) {
        log.info("Fetching all payment details with pagination: {}", pageable);
        Page<PaymentDetails> paymentDetails = paymentDetailsRepository.findAll(pageable);
        return paymentDetails.map(paymentDetailsMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDetailsResponseDto> searchPaymentDetails(String searchTerm, Pageable pageable) {
        log.info("Searching payment details with term: {}", searchTerm);
        Page<PaymentDetails> paymentDetails = paymentDetailsRepository.searchPaymentDetails(searchTerm, pageable);
        return paymentDetails.map(paymentDetailsMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDetailsResponseDto> getPaymentDetailsList() {
        log.info("Fetching all payment details as list");
        return paymentDetailsRepository.findAll().stream()
                .map(paymentDetailsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDetailsResponseDto getPaymentDetailsById(Long id) {
        log.info("Fetching payment details with ID: {}", id);
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment details not found with ID: " + id));
        return paymentDetailsMapper.toResponseDto(paymentDetails);
    }

    @Override
    @Transactional
    public PaymentDetailsResponseDto updatePaymentDetails(Long id, PaymentDetailsRequestDto requestDto) {
        log.info("Updating payment details with ID: {}", id);

        PaymentDetails paymentDetails = paymentDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment details not found with ID: " + id));

        // Validate that payment method exists if it's being updated
        if (requestDto.getPaymentMethodId() != null) {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDto.getPaymentMethodId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Payment method not found with ID: " + requestDto.getPaymentMethodId()));
            paymentDetails.setPaymentMethod(paymentMethod);
        }

        paymentDetailsMapper.updateEntityFromDto(requestDto, paymentDetails);
        PaymentDetails updatedPaymentDetails = paymentDetailsRepository.save(paymentDetails);

        log.info("Payment details updated successfully with ID: {}", id);
        return paymentDetailsMapper.toResponseDto(updatedPaymentDetails);
    }

    @Override
    @Transactional
    public void deletePaymentDetails(Long id) {
        log.info("Deleting payment details with ID: {}", id);

        if (!paymentDetailsRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment details not found with ID: " + id);
        }

        paymentDetailsRepository.deleteById(id);
        log.info("Payment details deleted successfully with ID: {}", id);
    }
}
