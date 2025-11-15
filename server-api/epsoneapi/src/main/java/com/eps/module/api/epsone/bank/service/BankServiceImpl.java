package com.eps.module.api.epsone.bank.service;

import com.eps.module.api.epsone.bank.dto.BankRequestDto;
import com.eps.module.api.epsone.bank.dto.BankResponseDto;
import com.eps.module.api.epsone.bank.dto.BankBulkUploadDto;
import com.eps.module.api.epsone.bank.dto.BankErrorReportDto;
import com.eps.module.api.epsone.bank.mapper.BankMapper;
import com.eps.module.api.epsone.bank.processor.BankBulkUploadProcessor;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.storage.dto.FileUploadResponseDto;
import com.eps.module.api.epsone.storage.service.FileStorageService;
import com.eps.module.bank.Bank;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.payment.PayeeDetails;
import com.eps.module.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.eps.module.api.epsone.bank.helper.BankHelper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BankServiceImpl extends BaseBulkUploadService<BankBulkUploadDto, Bank> implements BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;
    private final FileStorageService fileStorageService;
    private final BankHelper bankHelper;
    private final ManagedProjectRepository managedProjectRepository;
    private final PayeeDetailsRepository payeeDetailsRepository;
    private final BankBulkUploadProcessor bankBulkUploadProcessor;

    @Override
    public BankResponseDto createBank(BankRequestDto bankRequestDto) {
        log.info("Creating new bank: {}", bankRequestDto.getBankName());

        // Validate bank data
        bankHelper.validateBankUniqueness(bankRequestDto);

        Bank bank = bankMapper.toEntity(bankRequestDto);
        Bank savedBank = bankRepository.save(bank);
        
        log.info("Bank created successfully with ID: {}", savedBank.getId());
        return bankMapper.toResponseDto(savedBank);
    }

    @Override
    public BankResponseDto createBankWithLogo(BankRequestDto bankRequestDto, MultipartFile logo) {
        log.info("Creating new bank with logo: {}", bankRequestDto.getBankName());

        // Validate bank data
        bankHelper.validateBankUniqueness(bankRequestDto);

        // Upload logo if provided
        if (logo != null && !logo.isEmpty()) {
            FileUploadResponseDto uploadResponse = fileStorageService.uploadFile(logo, "banks/logos");
            bankRequestDto.setBankLogo(uploadResponse.getFileUrl());
        }

        Bank bank = bankMapper.toEntity(bankRequestDto);
        Bank savedBank = bankRepository.save(bank);
        
        log.info("Bank created successfully with ID: {}", savedBank.getId());
        return bankMapper.toResponseDto(savedBank);
    }

    @Override
    @Transactional(readOnly = true)
    public BankResponseDto getBankById(Long id) {
        log.info("Fetching bank with ID: {}", id);
        Bank bank = bankRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank not found with ID: " + id));
        return bankMapper.toResponseDto(bank);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankResponseDto> getAllBanks(Pageable pageable) {
        log.info("Fetching all banks with pagination: page={}, size={}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        Page<Bank> banks = bankRepository.findAll(pageable);
        return banks.map(bankMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankResponseDto> searchBanks(String search, Pageable pageable) {
        log.info("Searching banks with keyword: {}", search);
        Page<Bank> banks = bankRepository.searchBanks(search, pageable);
        return banks.map(bankMapper::toResponseDto);
    }

    @Override
    public BankResponseDto updateBank(Long id, BankRequestDto bankRequestDto) {
        log.info("Updating bank with ID: {}", id);

        Bank existingBank = bankRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank not found with ID: " + id));

        // Validate updated data
        bankHelper.validateBankUniquenessForUpdate(id, bankRequestDto, existingBank);

        bankMapper.updateEntityFromDto(bankRequestDto, existingBank);
        Bank updatedBank = bankRepository.save(existingBank);
        
        log.info("Bank updated successfully with ID: {}", id);
        return bankMapper.toResponseDto(updatedBank);
    }

    @Override
    public BankResponseDto updateBankWithLogo(Long id, BankRequestDto bankRequestDto, MultipartFile logo) {
        log.info("Updating bank with ID: {} with logo", id);

        Bank existingBank = bankRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank not found with ID: " + id));

        // Validate updated data
        bankHelper.validateBankUniquenessForUpdate(id, bankRequestDto, existingBank);

        // Upload new logo if provided
        if (logo != null && !logo.isEmpty()) {
            // Delete old logo if exists
            if (existingBank.getBankLogo() != null && !existingBank.getBankLogo().isEmpty()) {
                try {
                    String oldLogoPath = bankHelper.extractFilePathFromUrl(existingBank.getBankLogo());
                    fileStorageService.deleteFile(oldLogoPath);
                } catch (Exception e) {
                    log.warn("Failed to delete old logo: {}", e.getMessage());
                }
            }

            FileUploadResponseDto uploadResponse = fileStorageService.uploadFile(logo, "banks/logos");
            bankRequestDto.setBankLogo(uploadResponse.getFileUrl());
        }

        bankMapper.updateEntityFromDto(bankRequestDto, existingBank);
        Bank updatedBank = bankRepository.save(existingBank);
        
        log.info("Bank updated successfully with ID: {}", id);
        return bankMapper.toResponseDto(updatedBank);
    }

    @Override
    public void deleteBank(Long id) {
        log.info("Deleting bank with ID: {}", id);

        Bank bank = bankRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank not found with ID: " + id));

        // Check if this bank is being used by any managed projects
        log.debug("Checking for dependent managed projects for bank ID: {}", id);
        Page<ManagedProject> dependentProjects = managedProjectRepository.findByBankId(id, PageRequest.of(0, 6));
        log.debug("Found {} dependent managed projects", dependentProjects.getTotalElements());
        
        if (!dependentProjects.isEmpty()) {
            long totalCount = dependentProjects.getTotalElements();
            List<String> projectNames = dependentProjects.getContent().stream()
                    .limit(5)
                    .map(ManagedProject::getProjectName)
                    .collect(Collectors.toList());
            
            String projectNamesList = String.join(", ", projectNames);
            String errorMessage = String.format(
                    "Cannot delete '%s' bank because it is being used by %d managed project%s: %s%s. Please delete or reassign these managed projects first.",
                    bank.getBankName(),
                    totalCount,
                    totalCount > 1 ? "s" : "",
                    projectNamesList,
                    totalCount > 5 ? " and " + (totalCount - 5) + " more" : ""
            );
            
            log.warn("Attempted to delete bank '{}' which is referenced by {} managed projects", bank.getBankName(), totalCount);
            throw new IllegalStateException(errorMessage);
        }

        // Check if this bank is being used by any payee details
        log.debug("Checking for dependent payee details for bank ID: {}", id);
        Page<PayeeDetails> dependentPayeeDetails = payeeDetailsRepository.findByBankId(id, PageRequest.of(0, 6));
        log.debug("Found {} dependent payee details", dependentPayeeDetails.getTotalElements());
        
        if (!dependentPayeeDetails.isEmpty()) {
            long totalCount = dependentPayeeDetails.getTotalElements();
            List<String> payeeNames = dependentPayeeDetails.getContent().stream()
                    .limit(5)
                    .map(PayeeDetails::getPayeeName)
                    .collect(Collectors.toList());
            
            String payeeNamesList = String.join(", ", payeeNames);
            String errorMessage = String.format(
                    "Cannot delete '%s' bank because it is being used by %d payee detail%s: %s%s. Please delete or reassign these payee details first.",
                    bank.getBankName(),
                    totalCount,
                    totalCount > 1 ? "s" : "",
                    payeeNamesList,
                    totalCount > 5 ? " and " + (totalCount - 5) + " more" : ""
            );
            
            log.warn("Attempted to delete bank '{}' which is referenced by {} payee details", bank.getBankName(), totalCount);
            throw new IllegalStateException(errorMessage);
        }

        // Delete logo file if exists
        if (bank.getBankLogo() != null && !bank.getBankLogo().isEmpty()) {
            try {
                String logoPath = bankHelper.extractFilePathFromUrl(bank.getBankLogo());
                fileStorageService.deleteFile(logoPath);
            } catch (Exception e) {
                log.warn("Failed to delete bank logo: {}", e.getMessage());
            }
        }

        bankRepository.delete(bank);
        log.info("Bank deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankResponseDto> getAllBanksList() {
        log.info("Fetching all banks as list");
        List<Bank> banks = bankRepository.findAll();
        return bankMapper.toResponseDtoList(banks);
    }

    // Bulk upload methods
    @Override
    protected BulkUploadProcessor<BankBulkUploadDto, Bank> getProcessor() {
        return bankBulkUploadProcessor;
    }

    @Override
    public Class<BankBulkUploadDto> getBulkUploadDtoClass() {
        return BankBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Bank";
    }

    @Override
    public List<Bank> getAllEntitiesForExport() {
        log.info("Fetching all banks for export");
        return bankRepository.findAllForExport();
    }

    @Override
    public Function<Bank, BankBulkUploadDto> getEntityToDtoMapper() {
        return bank -> BankBulkUploadDto.builder()
                .bankName(bank.getBankName())
                .rbiBankCode(bank.getRbiBankCode())
                .epsBankCode(bank.getEpsBankCode())
                .bankCodeAlt(bank.getBankCodeAlt())
                .bankLogo(bank.getBankLogo())
                .build();
    }

    @Override
    protected BankErrorReportDto buildErrorReportDto(BulkUploadErrorDto error) {
        BankErrorReportDto errorDto = new BankErrorReportDto();
        errorDto.setRowNumber(error.getRowNumber());
        // include error type for export (e.g., VALIDATION, DUPLICATE, ERROR)
        errorDto.setErrorType(error.getErrorType());
        errorDto.setBankName(error.getRowData() != null ? (String) error.getRowData().get("bankName") : null);
        errorDto.setRbiBankCode(error.getRowData() != null ? (String) error.getRowData().get("rbiBankCode") : null);
        errorDto.setEpsBankCode(error.getRowData() != null ? (String) error.getRowData().get("epsBankCode") : null);
        errorDto.setBankCodeAlt(error.getRowData() != null ? (String) error.getRowData().get("bankCodeAlt") : null);
        errorDto.setBankLogo(error.getRowData() != null ? (String) error.getRowData().get("bankLogo") : null);
        errorDto.setErrorMessage(error.getErrorMessage());
        return errorDto;
    }

    @Override
    protected Class<BankErrorReportDto> getErrorReportDtoClass() {
        return BankErrorReportDto.class;
    }

}
