package com.eps.module.api.epsone.managed_project.service;

import com.eps.module.api.epsone.managed_project.dto.ManagedProjectBulkUploadDto;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectRequestDto;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectResponseDto;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.bulk.service.BulkUploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManagedProjectService extends BulkUploadService<ManagedProjectBulkUploadDto, ManagedProject> {

    ManagedProjectResponseDto createManagedProject(ManagedProjectRequestDto requestDto);

    ManagedProjectResponseDto updateManagedProject(Long id, ManagedProjectRequestDto requestDto);

    void deleteManagedProject(Long id);

    ManagedProjectResponseDto getManagedProjectById(Long id);

    Page<ManagedProjectResponseDto> getAllManagedProjects(Pageable pageable);

    Page<ManagedProjectResponseDto> searchManagedProjects(String search, Pageable pageable);

    Page<ManagedProjectResponseDto> getManagedProjectsByBank(Long bankId, Pageable pageable);

    List<ManagedProjectResponseDto> getAllManagedProjectsList();
}
