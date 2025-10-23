package com.eps.module.api.epsone.managedproject.controller;

import com.eps.module.api.epsone.managedproject.dto.ManagedProjectRequestDto;
import com.eps.module.api.epsone.managedproject.dto.ManagedProjectResponseDto;
import com.eps.module.api.epsone.managedproject.service.ManagedProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managed-projects")
@RequiredArgsConstructor
public class ManagedProjectController {

    private final ManagedProjectService managedProjectService;

    @PostMapping
    public ResponseEntity<ManagedProjectResponseDto> createManagedProject(
            @Valid @RequestBody ManagedProjectRequestDto requestDto) {
        ManagedProjectResponseDto responseDto = managedProjectService.createManagedProject(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ManagedProjectResponseDto>> getAllManagedProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ManagedProjectResponseDto> managedProjects = managedProjectService.getAllManagedProjects(pageable);
        return ResponseEntity.ok(managedProjects);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ManagedProjectResponseDto>> searchManagedProjects(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "projectName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ManagedProjectResponseDto> managedProjects = managedProjectService.searchManagedProjects(searchTerm, pageable);
        return ResponseEntity.ok(managedProjects);
    }

    @GetMapping("/bank/{bankId}")
    public ResponseEntity<Page<ManagedProjectResponseDto>> getManagedProjectsByBank(
            @PathVariable Long bankId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "projectName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ManagedProjectResponseDto> managedProjects = managedProjectService.getManagedProjectsByBank(bankId, pageable);
        return ResponseEntity.ok(managedProjects);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ManagedProjectResponseDto>> getAllManagedProjectsList() {
        List<ManagedProjectResponseDto> managedProjects = managedProjectService.getAllManagedProjectsList();
        return ResponseEntity.ok(managedProjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagedProjectResponseDto> getManagedProjectById(@PathVariable Long id) {
        ManagedProjectResponseDto managedProject = managedProjectService.getManagedProjectById(id);
        return ResponseEntity.ok(managedProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagedProjectResponseDto> updateManagedProject(
            @PathVariable Long id,
            @Valid @RequestBody ManagedProjectRequestDto requestDto) {
        ManagedProjectResponseDto responseDto = managedProjectService.updateManagedProject(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManagedProject(@PathVariable Long id) {
        managedProjectService.deleteManagedProject(id);
        return ResponseEntity.noContent().build();
    }
}
