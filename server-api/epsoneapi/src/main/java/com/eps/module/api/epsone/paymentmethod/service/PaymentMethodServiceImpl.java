package com.eps.module.api.epsone.paymentmethod.service;

import com.eps.module.api.epsone.paymentmethod.dto.PaymentMethodRequestDto;
import com.eps.module.api.epsone.paymentmethod.dto.PaymentMethodResponseDto;
import com.eps.module.api.epsone.paymentmethod.mapper.PaymentMethodMapper;
import com.eps.module.api.epsone.paymentmethod.repository.PaymentMethodRepository;
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
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @Override
    @Transactional
    public PaymentMethodResponseDto createPaymentMethod(PaymentMethodRequestDto requestDto) {
        log.info("Creating payment method: {}", requestDto.getMethodName());

        if (paymentMethodRepository.existsByMethodNameIgnoreCase(requestDto.getMethodName())) {
            throw new IllegalArgumentException("Payment method '" + requestDto.getMethodName() + "' already exists");
        }

        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(requestDto);
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        log.info("Payment method created successfully with ID: {}", savedPaymentMethod.getId());
        return paymentMethodMapper.toResponseDto(savedPaymentMethod);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodResponseDto> getAllPaymentMethods(Pageable pageable) {
        log.info("Fetching all payment methods with pagination: {}", pageable);
        Page<PaymentMethod> paymentMethods = paymentMethodRepository.findAll(pageable);
        return paymentMethods.map(paymentMethodMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodResponseDto> searchPaymentMethods(String searchTerm, Pageable pageable) {
        log.info("Searching payment methods with term: {}", searchTerm);
        Page<PaymentMethod> paymentMethods = paymentMethodRepository.searchPaymentMethods(searchTerm, pageable);
        return paymentMethods.map(paymentMethodMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodResponseDto> getPaymentMethodsList() {
        log.info("Fetching all payment methods as list");
        return paymentMethodRepository.findAll().stream()
                .map(paymentMethodMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethodResponseDto getPaymentMethodById(Long id) {
        log.info("Fetching payment method with ID: {}", id);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found with ID: " + id));
        return paymentMethodMapper.toResponseDto(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodResponseDto updatePaymentMethod(Long id, PaymentMethodRequestDto requestDto) {
        log.info("Updating payment method with ID: {}", id);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found with ID: " + id));

        if (paymentMethodRepository.existsByMethodNameAndIdNot(requestDto.getMethodName(), id)) {
            throw new IllegalArgumentException("Payment method '" + requestDto.getMethodName() + "' already exists");
        }

        paymentMethodMapper.updateEntityFromDto(requestDto, paymentMethod);
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        log.info("Payment method updated successfully with ID: {}", id);
        return paymentMethodMapper.toResponseDto(updatedPaymentMethod);
    }

    @Override
    @Transactional
    public void deletePaymentMethod(Long id) {
        log.info("Deleting payment method with ID: {}", id);

        if (!paymentMethodRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment method not found with ID: " + id);
        }

        paymentMethodRepository.deleteById(id);
        log.info("Payment method deleted successfully with ID: {}", id);
    }
}
