package com.eps.module.api.epsone.paymentmethod.service;

import com.eps.module.api.epsone.paymentmethod.dto.PaymentMethodRequestDto;
import com.eps.module.api.epsone.paymentmethod.dto.PaymentMethodResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentMethodService {

    PaymentMethodResponseDto createPaymentMethod(PaymentMethodRequestDto requestDto);

    Page<PaymentMethodResponseDto> getAllPaymentMethods(Pageable pageable);

    Page<PaymentMethodResponseDto> searchPaymentMethods(String searchTerm, Pageable pageable);

    List<PaymentMethodResponseDto> getPaymentMethodsList();

    PaymentMethodResponseDto getPaymentMethodById(Long id);

    PaymentMethodResponseDto updatePaymentMethod(Long id, PaymentMethodRequestDto requestDto);

    void deletePaymentMethod(Long id);
}
