package com.eps.module.api.epsone.managedproject.service;

import com.eps.module.api.epsone.managedproject.dto.ManagedProjectRequestDto;
import com.eps.module.api.epsone.managedproject.dto.ManagedProjectResponseDto;
import com.eps.module.api.epsone.managedproject.mapper.ManagedProjectMapper;
import com.eps.module.api.epsone.managedproject.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.bank.Bank;
import com.eps.module.bank.ManagedProject;
import com.eps.module.site.Site;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagedProjectServiceImpl implements ManagedProjectService {

    private final ManagedProjectRepository managedProjectRepository;
    private final BankRepository bankRepository;
    private final ManagedProjectMapper managedProjectMapper;

    @Override
    @Transactional
    public ManagedProjectResponseDto createManagedProject(ManagedProjectRequestDto requestDto) {
        // Validate bank exists
        Bank bank = bankRepository.findById(requestDto.getBankId())
            .orElseThrow(() -> new IllegalArgumentException("Bank not found with id: " + requestDto.getBankId()));

        // Check if project code already exists (if provided)
        if (requestDto.getProjectCode() != null && !requestDto.getProjectCode().trim().isEmpty()) {
            if (managedProjectRepository.existsByProjectCode(requestDto.getProjectCode())) {
                throw new IllegalArgumentException("Project with code '" + requestDto.getProjectCode() + "' already exists");
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
            .orElseThrow(() -> new IllegalArgumentException("Managed project not found with id: " + id));

        // Validate bank exists
        Bank bank = bankRepository.findById(requestDto.getBankId())
            .orElseThrow(() -> new IllegalArgumentException("Bank not found with id: " + requestDto.getBankId()));

        // Check if project code is being changed and if it already exists
        if (requestDto.getProjectCode() != null && !requestDto.getProjectCode().trim().isEmpty()) {
            if (managedProjectRepository.existsByProjectCodeAndIdNot(requestDto.getProjectCode(), id)) {
                throw new IllegalArgumentException("Project with code '" + requestDto.getProjectCode() + "' already exists");
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
        
        ManagedProject managedProject = managedProjectRepository.findByIdWithBank(id)
            .orElseThrow(() -> new IllegalArgumentException("Managed project not found with id: " + id));
        
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
                    "Cannot delete '%s' managed project because it is being used by %d site%s: %s. Please delete or reassign these sites first.",
                    managedProject.getProjectName(),
                    totalCount,
                    totalCount > 1 ? "s" : "",
                    siteCodesList
            );
            log.warn("Failed to delete managed project with ID {}: {}", id, errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        
        managedProjectRepository.deleteById(id);
        log.info("Managed project deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ManagedProjectResponseDto getManagedProjectById(Long id) {
        ManagedProject managedProject = managedProjectRepository.findByIdWithBank(id)
            .orElseThrow(() -> new IllegalArgumentException("Managed project not found with id: " + id));
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
            throw new IllegalArgumentException("Bank not found with id: " + bankId);
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
}
