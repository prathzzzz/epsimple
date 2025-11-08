package com.eps.module.api.epsone.payment_details.service;

import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsBulkUploadDto;
import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsRequestDto;
import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.payment.PaymentDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentDetailsService extends BulkUploadService<PaymentDetailsBulkUploadDto, PaymentDetails> {

    PaymentDetailsResponseDto createPaymentDetails(PaymentDetailsRequestDto requestDto);

    Page<PaymentDetailsResponseDto> getAllPaymentDetails(Pageable pageable);

    Page<PaymentDetailsResponseDto> searchPaymentDetails(String searchTerm, Pageable pageable);

    List<PaymentDetailsResponseDto> getPaymentDetailsList();

    PaymentDetailsResponseDto getPaymentDetailsById(Long id);

    PaymentDetailsResponseDto updatePaymentDetails(Long id, PaymentDetailsRequestDto requestDto);

    void deletePaymentDetails(Long id);
}
