package com.eps.module.api.epsone.voucher.service;

import com.eps.module.api.epsone.voucher.dto.VoucherBulkUploadDto;
import com.eps.module.api.epsone.voucher.dto.VoucherRequestDto;
import com.eps.module.api.epsone.voucher.dto.VoucherResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.payment.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VoucherService extends BulkUploadService<VoucherBulkUploadDto, Voucher> {

    VoucherResponseDto createVoucher(VoucherRequestDto requestDto);

    Page<VoucherResponseDto> getAllVouchers(Pageable pageable);

    Page<VoucherResponseDto> searchVouchers(String searchTerm, Pageable pageable);

    List<VoucherResponseDto> getVouchersList();

    Page<VoucherResponseDto> getVouchersByPayee(Long payeeId, Pageable pageable);

    VoucherResponseDto getVoucherById(Long id);

    VoucherResponseDto updateVoucher(Long id, VoucherRequestDto requestDto);

    VoucherResponseDto updatePaymentStatus(Long id, String paymentStatus);

    void deleteVoucher(Long id);
}
