package com.eps.module.api.epsone.bank.service;

import com.eps.module.api.epsone.bank.dto.BankRequestDto;
import com.eps.module.api.epsone.bank.dto.BankResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BankService {

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
