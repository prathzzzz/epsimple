package com.eps.module.api.epsone.bank.service;

import com.eps.module.api.epsone.bank.dto.BankBulkUploadDto;
import com.eps.module.api.epsone.bank.dto.BankRequestDto;
import com.eps.module.api.epsone.bank.dto.BankResponseDto;
import com.eps.module.bank.Bank;
import com.eps.module.common.bulk.service.BulkUploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BankService extends BulkUploadService<BankBulkUploadDto, Bank> {

    BankResponseDto createBank(BankRequestDto bankRequestDto);

    BankResponseDto createBankWithLogo(BankRequestDto bankRequestDto, MultipartFile logo);

    BankResponseDto getBankById(Long id);

    Page<BankResponseDto> getAllBanks(Pageable pageable);

    Page<BankResponseDto> searchBanks(String search, Pageable pageable);

    BankResponseDto updateBank(Long id, BankRequestDto bankRequestDto);

    BankResponseDto updateBankWithLogo(Long id, BankRequestDto bankRequestDto, MultipartFile logo);

    void deleteBank(Long id);

    List<BankResponseDto> getAllBanksList();
}
