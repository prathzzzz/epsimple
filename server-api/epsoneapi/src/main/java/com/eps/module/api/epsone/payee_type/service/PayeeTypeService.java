package com.eps.module.api.epsone.payee_type.service;

import com.eps.module.api.epsone.payee_type.dto.PayeeTypeBulkUploadDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeRequestDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.payment.PayeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PayeeTypeService extends BulkUploadService<PayeeTypeBulkUploadDto, PayeeType> {

    PayeeTypeResponseDto createPayeeType(PayeeTypeRequestDto requestDto);

    Page<PayeeTypeResponseDto> getAllPayeeTypes(Pageable pageable);

    Page<PayeeTypeResponseDto> searchPayeeTypes(String searchTerm, Pageable pageable);

    List<PayeeTypeResponseDto> getPayeeTypesList();

    PayeeTypeResponseDto getPayeeTypeById(Long id);

    PayeeTypeResponseDto updatePayeeType(Long id, PayeeTypeRequestDto requestDto);

    void deletePayeeType(Long id);
}
