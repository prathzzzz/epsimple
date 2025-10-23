package com.eps.module.api.epsone.managedproject.service;

import com.eps.module.api.epsone.managedproject.dto.ManagedProjectRequestDto;
import com.eps.module.api.epsone.managedproject.dto.ManagedProjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManagedProjectService {

    ManagedProjectResponseDto createManagedProject(ManagedProjectRequestDto requestDto);

    ManagedProjectResponseDto updateManagedProject(Long id, ManagedProjectRequestDto requestDto);

    void deleteManagedProject(Long id);

    ManagedProjectResponseDto getManagedProjectById(Long id);

    Page<ManagedProjectResponseDto> getAllManagedProjects(Pageable pageable);

    Page<ManagedProjectResponseDto> searchManagedProjects(String search, Pageable pageable);

    Page<ManagedProjectResponseDto> getManagedProjectsByBank(Long bankId, Pageable pageable);

    List<ManagedProjectResponseDto> getAllManagedProjectsList();
}
