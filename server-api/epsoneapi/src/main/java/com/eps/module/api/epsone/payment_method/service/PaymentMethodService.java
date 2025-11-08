package com.eps.module.api.epsone.payment_method.service;

import com.eps.module.api.epsone.payment_method.dto.PaymentMethodBulkUploadDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodRequestDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.payment.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentMethodService extends BulkUploadService<PaymentMethodBulkUploadDto, PaymentMethod> {

    PaymentMethodResponseDto createPaymentMethod(PaymentMethodRequestDto requestDto);

    Page<PaymentMethodResponseDto> getAllPaymentMethods(Pageable pageable);

    Page<PaymentMethodResponseDto> searchPaymentMethods(String searchTerm, Pageable pageable);

    List<PaymentMethodResponseDto> getPaymentMethodsList();

    PaymentMethodResponseDto getPaymentMethodById(Long id);

    PaymentMethodResponseDto updatePaymentMethod(Long id, PaymentMethodRequestDto requestDto);

    void deletePaymentMethod(Long id);
}
