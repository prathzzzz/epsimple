package com.eps.module.api.epsone.paymentdetails.service;

import com.eps.module.api.epsone.paymentdetails.dto.PaymentDetailsRequestDto;
import com.eps.module.api.epsone.paymentdetails.dto.PaymentDetailsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentDetailsService {

    PaymentDetailsResponseDto createPaymentDetails(PaymentDetailsRequestDto requestDto);

    Page<PaymentDetailsResponseDto> getAllPaymentDetails(Pageable pageable);

    Page<PaymentDetailsResponseDto> searchPaymentDetails(String searchTerm, Pageable pageable);

    List<PaymentDetailsResponseDto> getPaymentDetailsList();

    PaymentDetailsResponseDto getPaymentDetailsById(Long id);

    PaymentDetailsResponseDto updatePaymentDetails(Long id, PaymentDetailsRequestDto requestDto);

    void deletePaymentDetails(Long id);
}
