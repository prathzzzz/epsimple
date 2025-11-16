package com.eps.module.api.epsone.expenditures_voucher.service;

import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherBulkUploadDto;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherRequestDto;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.cost.ExpendituresVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExpendituresVoucherService extends BulkUploadService<ExpendituresVoucherBulkUploadDto, ExpendituresVoucher> {

    ExpendituresVoucherResponseDto createExpendituresVoucher(ExpendituresVoucherRequestDto requestDto);

    Page<ExpendituresVoucherResponseDto> getAllExpendituresVouchers(Pageable pageable);

    Page<ExpendituresVoucherResponseDto> searchExpendituresVouchers(String searchTerm, Pageable pageable);

    ExpendituresVoucherResponseDto getExpendituresVoucherById(Long id);

    ExpendituresVoucherResponseDto updateExpendituresVoucher(Long id, ExpendituresVoucherRequestDto requestDto);

    void deleteExpendituresVoucher(Long id);

    Page<ExpendituresVoucherResponseDto> getExpendituresVouchersByProjectId(Long projectId, Pageable pageable);

    List<ExpendituresVoucherResponseDto> getExpendituresVouchersByVoucherId(Long voucherId);

    List<ExpendituresVoucherResponseDto> getAllExpendituresVouchersList();
}
