package com.eps.module.api.epsone.expenditures_voucher.service;

import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.expenditures_voucher.constant.ExpendituresVoucherErrorMessages;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherBulkUploadDto;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherErrorReportDto;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherRequestDto;
import com.eps.module.api.epsone.expenditures_voucher.dto.ExpendituresVoucherResponseDto;
import com.eps.module.api.epsone.expenditures_voucher.mapper.ExpendituresVoucherMapper;
import com.eps.module.api.epsone.expenditures_voucher.processor.ExpendituresVoucherBulkUploadProcessor;
import com.eps.module.api.epsone.expenditures_voucher.repository.ExpendituresVoucherRepository;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.ExpendituresVoucher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpendituresVoucherServiceImpl extends BaseBulkUploadService<ExpendituresVoucherBulkUploadDto, ExpendituresVoucher> implements ExpendituresVoucherService {

    private final ExpendituresVoucherRepository expendituresVoucherRepository;
    private final ExpendituresVoucherMapper expendituresVoucherMapper;
    private final CostItemRepository costItemRepository;
    private final VoucherRepository voucherRepository;
    private final ManagedProjectRepository managedProjectRepository;
    private final ExpendituresVoucherBulkUploadProcessor expendituresVoucherBulkUploadProcessor;

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<ExpendituresVoucherBulkUploadDto, ExpendituresVoucher> getProcessor() {
        return expendituresVoucherBulkUploadProcessor;
    }

    @Override
    public Class<ExpendituresVoucherBulkUploadDto> getBulkUploadDtoClass() {
        return ExpendituresVoucherBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Expenditures Voucher";
    }

    @Override
    public List<ExpendituresVoucher> getAllEntitiesForExport() {
        return expendituresVoucherRepository.findAllForExport();
    }

    @Override
    public Function<ExpendituresVoucher, ExpendituresVoucherBulkUploadDto> getEntityToDtoMapper() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        return expendituresVoucher -> {
            String costItemName = expendituresVoucher.getCostItem() != null 
                    ? expendituresVoucher.getCostItem().getCostItemFor() : "";
            
            String voucherNumber = expendituresVoucher.getVoucher() != null 
                    ? expendituresVoucher.getVoucher().getVoucherNumber() : "";
            
            String projectCode = expendituresVoucher.getManagedProject() != null 
                    ? expendituresVoucher.getManagedProject().getProjectCode() : "";

            return ExpendituresVoucherBulkUploadDto.builder()
                    .costItemName(costItemName)
                    .voucherNumber(voucherNumber)
                    .managedProjectCode(projectCode)
                    .incurredDate(expendituresVoucher.getIncurredDate() != null 
                            ? expendituresVoucher.getIncurredDate().format(dateFormatter) : "")
                    .description(expendituresVoucher.getDescription())
                    .build();
        };
    }

    @Override
    public ExpendituresVoucherErrorReportDto buildErrorReportDto(BulkUploadErrorDto errorDto) {
        return ExpendituresVoucherErrorReportDto.builder()
                .rowNumber(errorDto.getRowNumber())
                .costItemName((String) errorDto.getRowData().get("Cost Item Name"))
                .voucherNumber((String) errorDto.getRowData().get("Voucher Number"))
                .managedProjectCode((String) errorDto.getRowData().get("Managed Project Code"))
                .incurredDate((String) errorDto.getRowData().get("Incurred Date"))
                .description((String) errorDto.getRowData().get("Description"))
                .errorMessage(errorDto.getErrorMessage())
                .build();
    }

    @Override
    public Class<ExpendituresVoucherErrorReportDto> getErrorReportDtoClass() {
        return ExpendituresVoucherErrorReportDto.class;
    }

    // ========== CRUD Methods ==========

    @Override
    @Transactional
    public ExpendituresVoucherResponseDto createExpendituresVoucher(ExpendituresVoucherRequestDto requestDto) {
        log.info("Creating expenditures voucher with cost item ID: {}, voucher ID: {}, project ID: {}",
                requestDto.getCostItemId(), requestDto.getVoucherId(), requestDto.getManagedProjectId());

        // Validate cost item exists
        if (!costItemRepository.existsById(requestDto.getCostItemId())) {
            throw new ResourceNotFoundException(ExpendituresVoucherErrorMessages.COST_ITEM_NOT_FOUND + requestDto.getCostItemId());
        }

        // Validate voucher exists
        if (!voucherRepository.existsById(requestDto.getVoucherId())) {
            throw new ResourceNotFoundException(ExpendituresVoucherErrorMessages.VOUCHER_NOT_FOUND + requestDto.getVoucherId());
        }

        // Validate Managed Project exists
        if (!managedProjectRepository.existsById(requestDto.getManagedProjectId())) {
            throw new ResourceNotFoundException(ExpendituresVoucherErrorMessages.MANAGED_PROJECT_NOT_FOUND + requestDto.getManagedProjectId());
        }

        ExpendituresVoucher expendituresVoucher = expendituresVoucherMapper.toEntity(requestDto);
        ExpendituresVoucher savedExpendituresVoucher = expendituresVoucherRepository.save(expendituresVoucher);

        log.info("Successfully created expenditures voucher with ID: {}", savedExpendituresVoucher.getId());
        return expendituresVoucherMapper.toResponseDto(savedExpendituresVoucher);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpendituresVoucherResponseDto> getAllExpendituresVouchers(Pageable pageable) {
        log.info("Fetching all expenditures vouchers with pagination: {}", pageable);
        Page<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.findAllWithDetails(pageable);
        return expendituresVouchers.map(expendituresVoucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpendituresVoucherResponseDto> searchExpendituresVouchers(String searchTerm, Pageable pageable) {
        log.info("Searching expenditures vouchers with term: {}", searchTerm);
        Page<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.searchExpendituresVouchers(searchTerm, pageable);
        return expendituresVouchers.map(expendituresVoucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpendituresVoucherResponseDto getExpendituresVoucherById(Long id) {
        log.info("Fetching expenditures voucher with ID: {}", id);
        ExpendituresVoucher expendituresVoucher = expendituresVoucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExpendituresVoucherErrorMessages.EXPENDITURES_VOUCHER_NOT_FOUND + id));
        return expendituresVoucherMapper.toResponseDto(expendituresVoucher);
    }

    @Override
    @Transactional
    public ExpendituresVoucherResponseDto updateExpendituresVoucher(Long id, ExpendituresVoucherRequestDto requestDto) {
        log.info("Updating expenditures voucher with ID: {}", id);

        ExpendituresVoucher existingExpendituresVoucher = expendituresVoucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExpendituresVoucherErrorMessages.EXPENDITURES_VOUCHER_NOT_FOUND + id));

        // Validate cost item exists if changed
        if (!requestDto.getCostItemId().equals(existingExpendituresVoucher.getCostItem().getId())) {
            if (!costItemRepository.existsById(requestDto.getCostItemId())) {
                throw new ResourceNotFoundException(ExpendituresVoucherErrorMessages.COST_ITEM_NOT_FOUND + requestDto.getCostItemId());
            }
        }

        // Validate voucher exists if changed
        if (!requestDto.getVoucherId().equals(existingExpendituresVoucher.getVoucher().getId())) {
            if (!voucherRepository.existsById(requestDto.getVoucherId())) {
                throw new ResourceNotFoundException(ExpendituresVoucherErrorMessages.VOUCHER_NOT_FOUND + requestDto.getVoucherId());
            }
        }

        // Validate Managed Project exists if changed
        if (!requestDto.getManagedProjectId().equals(existingExpendituresVoucher.getManagedProject().getId())) {
            if (!managedProjectRepository.existsById(requestDto.getManagedProjectId())) {
                throw new ResourceNotFoundException(ExpendituresVoucherErrorMessages.MANAGED_PROJECT_NOT_FOUND + requestDto.getManagedProjectId());
            }
        }

        expendituresVoucherMapper.updateEntityFromDto(requestDto, existingExpendituresVoucher);
        ExpendituresVoucher updatedExpendituresVoucher = expendituresVoucherRepository.save(existingExpendituresVoucher);

        log.info("Successfully updated expenditures voucher with ID: {}", id);
        return expendituresVoucherMapper.toResponseDto(updatedExpendituresVoucher);
    }

    @Override
    @Transactional
    public void deleteExpendituresVoucher(Long id) {
        log.info("Deleting expenditures voucher with ID: {}", id);
        if (!expendituresVoucherRepository.existsById(id)) {
            throw new ResourceNotFoundException(ExpendituresVoucherErrorMessages.EXPENDITURES_VOUCHER_NOT_FOUND + id);
        }
        expendituresVoucherRepository.deleteById(id);
        log.info("Successfully deleted expenditures voucher with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpendituresVoucherResponseDto> getExpendituresVouchersByProjectId(Long projectId, Pageable pageable) {
        log.info("Fetching expenditures vouchers for project ID: {}", projectId);
        Page<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.findByManagedProjectId(projectId, pageable);
        return expendituresVouchers.map(expendituresVoucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpendituresVoucherResponseDto> getExpendituresVouchersByVoucherId(Long voucherId) {
        log.info("Fetching expenditures for voucher ID: {}", voucherId);
        List<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.findByVoucherId(voucherId);
        return expendituresVouchers.stream()
                .map(expendituresVoucherMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpendituresVoucherResponseDto> getAllExpendituresVouchersList() {
        log.info("Fetching all expenditures vouchers as list");
        List<ExpendituresVoucher> expendituresVouchers = expendituresVoucherRepository.findAllWithDetailsList();
        return expendituresVouchers.stream()
                .map(expendituresVoucherMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
