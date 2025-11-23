package com.eps.module.api.epsone.managed_project.service;

import com.eps.module.api.epsone.managed_project.constant.ManagedProjectErrorMessages;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectBulkUploadDto;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectErrorReportDto;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectRequestDto;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectResponseDto;
import com.eps.module.api.epsone.managed_project.mapper.ManagedProjectMapper;
import com.eps.module.api.epsone.managed_project.processor.ManagedProjectBulkUploadProcessor;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.bank.Bank;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.BadRequestException;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.site.Site;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ManagedProjectServiceImpl extends BaseBulkUploadService<ManagedProjectBulkUploadDto, ManagedProject> implements ManagedProjectService {

    private final ManagedProjectRepository managedProjectRepository;
    private final BankRepository bankRepository;
    private final ManagedProjectMapper managedProjectMapper;
    private final ManagedProjectBulkUploadProcessor managedProjectBulkUploadProcessor;

    @Override
    @Transactional
    public ManagedProjectResponseDto createManagedProject(ManagedProjectRequestDto requestDto) {
        // Validate bank exists
        Bank bank = bankRepository.findById(requestDto.getBankId())
            .orElseThrow(() -> new ResourceNotFoundException(ManagedProjectErrorMessages.BANK_NOT_FOUND + requestDto.getBankId()));

        // Check if project code already exists (if provided)
        if (requestDto.getProjectCode() != null && !requestDto.getProjectCode().trim().isEmpty()) {
            if (managedProjectRepository.existsByProjectCode(requestDto.getProjectCode())) {
                throw new ConflictException(String.format(ManagedProjectErrorMessages.PROJECT_CODE_ALREADY_EXISTS, requestDto.getProjectCode()));
            }
        }

        ManagedProject managedProject = managedProjectMapper.toEntity(requestDto);
        managedProject.setBank(bank);

        ManagedProject savedManagedProject = managedProjectRepository.save(managedProject);
        return managedProjectMapper.toResponseDto(savedManagedProject);
    }

    @Override
    @Transactional
    public ManagedProjectResponseDto updateManagedProject(Long id, ManagedProjectRequestDto requestDto) {
        ManagedProject existingManagedProject = managedProjectRepository.findByIdWithBank(id)
            .orElseThrow(() -> new ResourceNotFoundException(ManagedProjectErrorMessages.MANAGED_PROJECT_NOT_FOUND + id));

        // Validate bank exists
        Bank bank = bankRepository.findById(requestDto.getBankId())
            .orElseThrow(() -> new ResourceNotFoundException(ManagedProjectErrorMessages.BANK_NOT_FOUND + requestDto.getBankId()));

        // Check if project code is being changed and if it already exists
        if (requestDto.getProjectCode() != null && !requestDto.getProjectCode().trim().isEmpty()) {
            if (managedProjectRepository.existsByProjectCodeAndIdNot(requestDto.getProjectCode(), id)) {
                throw new ConflictException(String.format(ManagedProjectErrorMessages.PROJECT_CODE_ALREADY_EXISTS, requestDto.getProjectCode()));
            }
        }

        managedProjectMapper.updateEntityFromDto(requestDto, existingManagedProject);
        existingManagedProject.setBank(bank);

        ManagedProject updatedManagedProject = managedProjectRepository.save(existingManagedProject);
        return managedProjectMapper.toResponseDto(updatedManagedProject);
    }

    @Override
    @Transactional
    public void deleteManagedProject(Long id) {
        log.info("Deleting managed project with ID: {}", id);
        
        ManagedProject managedProject = managedProjectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ManagedProjectErrorMessages.MANAGED_PROJECT_NOT_FOUND + id));
        
        // Check for dependent sites
        Page<Site> dependentSites = managedProjectRepository.findSitesByProjectId(id, PageRequest.of(0, 6));
        
        if (!dependentSites.isEmpty()) {
            long totalCount = dependentSites.getTotalElements();
            List<String> siteCodes = dependentSites.getContent().stream()
                    .limit(5)
                    .map(Site::getSiteCode)
                    .collect(Collectors.toList());
            
            String siteCodesList = String.join(", ", siteCodes);
            String errorMessage = String.format(
                    ManagedProjectErrorMessages.CANNOT_DELETE_USED_BY_SITES,
                    managedProject.getProjectName(),
                    totalCount,
                    totalCount > 1 ? "s" : "",
                    siteCodesList
            );
            log.warn("Failed to delete Managed Project with ID {}: {}", id, errorMessage);
            throw new BadRequestException(errorMessage);
        }
        
        managedProjectRepository.deleteById(id);
        log.info("Managed Project deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ManagedProjectResponseDto getManagedProjectById(Long id) {
        ManagedProject managedProject = managedProjectRepository.findByIdWithBank(id)
            .orElseThrow(() -> new ResourceNotFoundException(ManagedProjectErrorMessages.MANAGED_PROJECT_NOT_FOUND + id));
        return managedProjectMapper.toResponseDto(managedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManagedProjectResponseDto> getAllManagedProjects(Pageable pageable) {
        Page<ManagedProject> managedProjects = managedProjectRepository.findAllWithBank(pageable);
        return managedProjects.map(managedProjectMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManagedProjectResponseDto> searchManagedProjects(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getAllManagedProjects(pageable);
        }
        Page<ManagedProject> managedProjects = managedProjectRepository.searchManagedProjects(search.trim(), pageable);
        return managedProjects.map(managedProjectMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManagedProjectResponseDto> getManagedProjectsByBank(Long bankId, Pageable pageable) {
        // Validate bank exists
        if (!bankRepository.existsById(bankId)) {
            throw new ResourceNotFoundException(ManagedProjectErrorMessages.BANK_NOT_FOUND + bankId);
        }
        
        Page<ManagedProject> managedProjects = managedProjectRepository.findByBankId(bankId, pageable);
        return managedProjects.map(managedProjectMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagedProjectResponseDto> getAllManagedProjectsList() {
        List<ManagedProject> managedProjects = managedProjectRepository.findAllManagedProjectsList();
        return managedProjectMapper.toResponseDtoList(managedProjects);
    }

    // Bulk upload methods
    @Override
    protected BulkUploadProcessor<ManagedProjectBulkUploadDto, ManagedProject> getProcessor() {
        return managedProjectBulkUploadProcessor;
    }

    @Override
    public Class<ManagedProjectBulkUploadDto> getBulkUploadDtoClass() {
        return ManagedProjectBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "ManagedProject";
    }

    @Override
    public List<ManagedProject> getAllEntitiesForExport() {
        log.info("Fetching all Managed Projects for export");
        return managedProjectRepository.findAllForExport();
    }

    @Override
    public Function<ManagedProject, ManagedProjectBulkUploadDto> getEntityToDtoMapper() {
        return managedProject -> ManagedProjectBulkUploadDto.builder()
                .projectName(managedProject.getProjectName())
                .projectCode(managedProject.getProjectCode())
                .projectType(managedProject.getProjectType())
                .projectDescription(managedProject.getProjectDescription())
                .bankName(managedProject.getBank() != null ? managedProject.getBank().getBankName() : null)
                .rbiBankCode(managedProject.getBank() != null ? managedProject.getBank().getRbiBankCode() : null)
                .epsBankCode(managedProject.getBank() != null ? managedProject.getBank().getEpsBankCode() : null)
                .build();
    }

    @Override
    protected ManagedProjectErrorReportDto buildErrorReportDto(BulkUploadErrorDto error) {
        ManagedProjectErrorReportDto errorDto = new ManagedProjectErrorReportDto();
        errorDto.setRowNumber(error.getRowNumber());
        errorDto.setErrorType(error.getErrorType());
        errorDto.setBankName(error.getRowData() != null ? (String) error.getRowData().get("bankName") : null);
        errorDto.setProjectName(error.getRowData() != null ? (String) error.getRowData().get("projectName") : null);
        errorDto.setProjectCode(error.getRowData() != null ? (String) error.getRowData().get("projectCode") : null);
        errorDto.setProjectType(error.getRowData() != null ? (String) error.getRowData().get("projectType") : null);
        errorDto.setProjectDescription(error.getRowData() != null ? (String) error.getRowData().get("projectDescription") : null);
        errorDto.setErrorMessage(error.getErrorMessage());
        return errorDto;
    }

    @Override
    protected Class<ManagedProjectErrorReportDto> getErrorReportDtoClass() {
        return ManagedProjectErrorReportDto.class;
    }
}
