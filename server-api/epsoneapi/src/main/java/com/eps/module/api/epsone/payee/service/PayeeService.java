package com.eps.module.api.epsone.payee.service;

import com.eps.module.api.epsone.payee.dto.PayeeRequestDto;
import com.eps.module.api.epsone.payee.dto.PayeeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PayeeService {

    PayeeResponseDto createPayee(PayeeRequestDto requestDto);

    Page<PayeeResponseDto> getAllPayees(Pageable pageable);

    Page<PayeeResponseDto> searchPayees(String searchTerm, Pageable pageable);

    List<PayeeResponseDto> getPayeesList();

    PayeeResponseDto getPayeeById(Long id);

    PayeeResponseDto updatePayee(Long id, PayeeRequestDto requestDto);

    void deletePayee(Long id);

    long countPayees();
}
