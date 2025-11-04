package com.eps.module.api.epsone.payee_type.service;

import com.eps.module.api.epsone.payee_type.dto.PayeeTypeRequestDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PayeeTypeService {

    PayeeTypeResponseDto createPayeeType(PayeeTypeRequestDto requestDto);

    Page<PayeeTypeResponseDto> getAllPayeeTypes(Pageable pageable);

    Page<PayeeTypeResponseDto> searchPayeeTypes(String searchTerm, Pageable pageable);

    List<PayeeTypeResponseDto> getPayeeTypesList();

    PayeeTypeResponseDto getPayeeTypeById(Long id);

    PayeeTypeResponseDto updatePayeeType(Long id, PayeeTypeRequestDto requestDto);

    void deletePayeeType(Long id);
}
