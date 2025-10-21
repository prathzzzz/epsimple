package com.eps.module.api.epsone.costcategory.service;

import com.eps.module.api.epsone.costcategory.dto.CostCategoryRequestDto;
import com.eps.module.api.epsone.costcategory.dto.CostCategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CostCategoryService {
    
    CostCategoryResponseDto createCostCategory(CostCategoryRequestDto requestDto);
    
    Page<CostCategoryResponseDto> getAllCostCategories(Pageable pageable);
    
    Page<CostCategoryResponseDto> searchCostCategories(String searchTerm, Pageable pageable);
    
    List<CostCategoryResponseDto> getAllCostCategoriesList();
    
    CostCategoryResponseDto getCostCategoryById(Long id);
    
    CostCategoryResponseDto updateCostCategory(Long id, CostCategoryRequestDto requestDto);
    
    void deleteCostCategory(Long id);
}
