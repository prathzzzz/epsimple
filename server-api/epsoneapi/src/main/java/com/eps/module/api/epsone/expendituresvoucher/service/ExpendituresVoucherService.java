package com.eps.module.api.epsone.expendituresvoucher.service;

import com.eps.module.api.epsone.costitem.repository.CostItemRepository;
import com.eps.module.api.epsone.expendituresvoucher.dto.ExpendituresVoucherRequestDto;
import com.eps.module.api.epsone.expendituresvoucher.dto.ExpendituresVoucherResponseDto;
import com.eps.module.api.epsone.expendituresvoucher.mapper.ExpendituresVoucherMapper;
import com.eps.module.api.epsone.expendituresvoucher.repository.ExpendituresVoucherRepository;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
import com.eps.module.api.epsone.managedproject.repository.ManagedProjectRepository;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.ExpendituresVoucher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpendituresVoucherService {

    private final ExpendituresVoucherRepository expendituresVoucherRepository;
    private final ExpendituresVoucherMapper expendituresVoucherMapper;
    private final CostItemRepository costItemRepository;
    private final VoucherRepository voucherRepository;
    private final ManagedProjectRepository managedProjectRepository;

    @Transactional
    public ExpendituresVoucherResponseDto createExpendituresVoucher(ExpendituresVoucherRequestDto requestDto) {
        log.info("Creating expenditures voucher with cost item ID: {}, voucher ID: {}, project ID: {}",
                requestDto.getCostItemId(), requestDto.getVoucherId(), requestDto.getManagedProjectId());

        // Validate cost item exists
        if (!costItemRepository.existsById(requestDto.getCostItemId())) {
            throw new ResourceNotFoundException("Cost item not found with ID: " + requestDto.getCostItemId());
        }

        // Validate voucher exists
        if (!voucherRepository.existsById(requestDto.getVoucherId())) {
            throw new ResourceNotFoundException("Voucher not found with ID: " + requestDto.getVoucherId());
        }

        // Validate managed project exists
        if (!managedProjectRepository.existsById(requestDto.getManagedProjectId())) {
            throw new ResourceNotFoundException("Managed project not found with ID: " + requestDto.getManagedProjectId());
        }

        ExpendituresVoucher expendituresVoucher = expendituresVoucherMapper.toEntity(requestDto);
        ExpendituresVoucher savedExpendituresVoucher = expendituresVoucherRepository.save(expendituresVoucher);

        log.info("Successfully created expenditures voucher with ID: {}", savedExpendituresVoucher.getId());
        return expendituresVoucherMapper.toResponseDto(savedExpendituresVoucher);
    }

    @Transactional(readOnly = true)
    public Page<ExpendituresVoucherResponseDto> getAllExpendituresVouchers(Pageable pageable) {
        log.info("Fetching all expenditures vouchers with pagination: {}", pageable);
        Page<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.findAllWithDetails(pageable);
        return expendituresVouchers.map(expendituresVoucherMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ExpendituresVoucherResponseDto> searchExpendituresVouchers(String searchTerm, Pageable pageable) {
        log.info("Searching expenditures vouchers with term: {}", searchTerm);
        Page<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.searchExpendituresVouchers(searchTerm, pageable);
        return expendituresVouchers.map(expendituresVoucherMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public ExpendituresVoucherResponseDto getExpendituresVoucherById(Long id) {
        log.info("Fetching expenditures voucher with ID: {}", id);
        ExpendituresVoucher expendituresVoucher = expendituresVoucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expenditures voucher not found with ID: " + id));
        return expendituresVoucherMapper.toResponseDto(expendituresVoucher);
    }

    @Transactional
    public ExpendituresVoucherResponseDto updateExpendituresVoucher(Long id, ExpendituresVoucherRequestDto requestDto) {
        log.info("Updating expenditures voucher with ID: {}", id);

        ExpendituresVoucher existingExpendituresVoucher = expendituresVoucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expenditures voucher not found with ID: " + id));

        // Validate cost item exists if changed
        if (!requestDto.getCostItemId().equals(existingExpendituresVoucher.getCostItem().getId())) {
            if (!costItemRepository.existsById(requestDto.getCostItemId())) {
                throw new ResourceNotFoundException("Cost item not found with ID: " + requestDto.getCostItemId());
            }
        }

        // Validate voucher exists if changed
        if (!requestDto.getVoucherId().equals(existingExpendituresVoucher.getVoucher().getId())) {
            if (!voucherRepository.existsById(requestDto.getVoucherId())) {
                throw new ResourceNotFoundException("Voucher not found with ID: " + requestDto.getVoucherId());
            }
        }

        // Validate managed project exists if changed
        if (!requestDto.getManagedProjectId().equals(existingExpendituresVoucher.getManagedProject().getId())) {
            if (!managedProjectRepository.existsById(requestDto.getManagedProjectId())) {
                throw new ResourceNotFoundException("Managed project not found with ID: " + requestDto.getManagedProjectId());
            }
        }

        expendituresVoucherMapper.updateEntityFromDto(requestDto, existingExpendituresVoucher);
        ExpendituresVoucher updatedExpendituresVoucher = expendituresVoucherRepository.save(existingExpendituresVoucher);

        log.info("Successfully updated expenditures voucher with ID: {}", id);
        return expendituresVoucherMapper.toResponseDto(updatedExpendituresVoucher);
    }

    @Transactional
    public void deleteExpendituresVoucher(Long id) {
        log.info("Deleting expenditures voucher with ID: {}", id);
        if (!expendituresVoucherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expenditures voucher not found with ID: " + id);
        }
        expendituresVoucherRepository.deleteById(id);
        log.info("Successfully deleted expenditures voucher with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<ExpendituresVoucherResponseDto> getExpendituresVouchersByProjectId(Long projectId, Pageable pageable) {
        log.info("Fetching expenditures vouchers for project ID: {}", projectId);
        Page<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.findByManagedProjectId(projectId, pageable);
        return expendituresVouchers.map(expendituresVoucherMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public List<ExpendituresVoucherResponseDto> getExpendituresVouchersByVoucherId(Long voucherId) {
        log.info("Fetching expenditures for voucher ID: {}", voucherId);
        List<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.findByVoucherId(voucherId);
        return expendituresVouchers.stream()
                .map(expendituresVoucherMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpendituresVoucherResponseDto> getAllExpendituresVouchersList() {
        log.info("Fetching all expenditures vouchers as list");
        List<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.findAllWithDetailsList();
        return expendituresVouchers.stream()
                .map(expendituresVoucherMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
