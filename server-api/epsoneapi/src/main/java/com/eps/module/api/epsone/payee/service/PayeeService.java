package com.eps.module.api.epsone.payee.service;

import com.eps.module.api.epsone.payee.dto.PayeeBulkUploadDto;
import com.eps.module.api.epsone.payee.dto.PayeeRequestDto;
import com.eps.module.api.epsone.payee.dto.PayeeResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.payment.Payee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PayeeService extends BulkUploadService<PayeeBulkUploadDto, Payee> {

    PayeeResponseDto createPayee(PayeeRequestDto requestDto);

    Page<PayeeResponseDto> getAllPayees(Pageable pageable);

    Page<PayeeResponseDto> searchPayees(String searchTerm, Pageable pageable);

    List<PayeeResponseDto> getPayeesList();

    PayeeResponseDto getPayeeById(Long id);

    PayeeResponseDto updatePayee(Long id, PayeeRequestDto requestDto);

    void deletePayee(Long id);

    long countPayees();
}
