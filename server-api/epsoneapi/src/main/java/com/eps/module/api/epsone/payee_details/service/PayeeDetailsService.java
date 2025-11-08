package com.eps.module.api.epsone.payee_details.service;

import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsBulkUploadDto;
import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsRequestDto;
import com.eps.module.api.epsone.payee_details.dto.PayeeDetailsResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.payment.PayeeDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PayeeDetailsService extends BulkUploadService<PayeeDetailsBulkUploadDto, PayeeDetails> {

    PayeeDetailsResponseDto createPayeeDetails(PayeeDetailsRequestDto requestDto);

    Page<PayeeDetailsResponseDto> getAllPayeeDetails(Pageable pageable);

    Page<PayeeDetailsResponseDto> searchPayeeDetails(String searchTerm, Pageable pageable);

    List<PayeeDetailsResponseDto> getPayeeDetailsList();

    PayeeDetailsResponseDto getPayeeDetailsById(Long id);

    PayeeDetailsResponseDto updatePayeeDetails(Long id, PayeeDetailsRequestDto requestDto);

    void deletePayeeDetails(Long id);
}
