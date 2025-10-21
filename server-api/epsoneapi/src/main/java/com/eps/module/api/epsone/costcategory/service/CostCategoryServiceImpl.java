package com.eps.module.api.epsone.costcategory.service;

import com.eps.module.api.epsone.costcategory.dto.CostCategoryRequestDto;
import com.eps.module.api.epsone.costcategory.dto.CostCategoryResponseDto;
import com.eps.module.api.epsone.costcategory.mapper.CostCategoryMapper;
import com.eps.module.api.epsone.costcategory.repository.CostCategoryRepository;
import com.eps.module.cost.CostCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CostCategoryServiceImpl implements CostCategoryService {
    
    private final CostCategoryRepository costCategoryRepository;
    private final CostCategoryMapper costCategoryMapper;
    
    @Override
    @Transactional
    public CostCategoryResponseDto createCostCategory(CostCategoryRequestDto requestDto) {
        log.info("Creating cost category: {}", requestDto.getCategoryName());
        
        // Check for duplicate
        if (costCategoryRepository.existsByCategoryNameIgnoreCase(requestDto.getCategoryName())) {
            throw new IllegalArgumentException("Cost category '" + requestDto.getCategoryName() + "' already exists");
        }
        
        CostCategory entity = costCategoryMapper.toEntity(requestDto);
        CostCategory saved = costCategoryRepository.save(entity);
        
        log.info("Cost category created successfully with ID: {}", saved.getId());
        return costCategoryMapper.toResponseDto(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CostCategoryResponseDto> getAllCostCategories(Pageable pageable) {
        log.info("Fetching all cost categories with pagination");
        return costCategoryRepository.findAll(pageable)
                .map(costCategoryMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CostCategoryResponseDto> searchCostCategories(String searchTerm, Pageable pageable) {
        log.info("Searching cost categories with term: {}", searchTerm);
        return costCategoryRepository.searchCostCategories(searchTerm, pageable)
                .map(costCategoryMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CostCategoryResponseDto> getAllCostCategoriesList() {
        log.info("Fetching all cost categories as list");
        return costCategoryRepository.findAll().stream()
                .map(costCategoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CostCategoryResponseDto getCostCategoryById(Long id) {
        log.info("Fetching cost category by ID: {}", id);
        CostCategory entity = costCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cost category not found with id: " + id));
        return costCategoryMapper.toResponseDto(entity);
    }
    
    @Override
    @Transactional
    public CostCategoryResponseDto updateCostCategory(Long id, CostCategoryRequestDto requestDto) {
        log.info("Updating cost category with ID: {}", id);
        
        CostCategory existing = costCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cost category not found with id: " + id));
        
        // Check for duplicate (excluding current record)
        if (costCategoryRepository.existsByCategoryNameAndIdNot(requestDto.getCategoryName(), id)) {
            throw new IllegalArgumentException("Cost category '" + requestDto.getCategoryName() + "' already exists");
        }
        
        costCategoryMapper.updateEntityFromDto(requestDto, existing);
        CostCategory updated = costCategoryRepository.save(existing);
        
        log.info("Cost category updated successfully with ID: {}", id);
        return costCategoryMapper.toResponseDto(updated);
    }
    
    @Override
    @Transactional
    public void deleteCostCategory(Long id) {
        log.info("Deleting cost category with ID: {}", id);
        
        if (!costCategoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Cost category not found with id: " + id);
        }
        
        costCategoryRepository.deleteById(id);
        log.info("Cost category deleted successfully with ID: {}", id);
    }
}
