package com.eps.module.api.epsone.cost_type.service;

import com.eps.module.api.epsone.cost_type.dto.CostTypeBulkUploadDto;
import com.eps.module.api.epsone.cost_type.dto.CostTypeRequestDto;
import com.eps.module.api.epsone.cost_type.dto.CostTypeResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.cost.CostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CostTypeService extends BulkUploadService<CostTypeBulkUploadDto, CostType> {
    
    CostTypeResponseDto createCostType(CostTypeRequestDto requestDto);
    
    Page<CostTypeResponseDto> getAllCostTypes(Pageable pageable);
    
    Page<CostTypeResponseDto> searchCostTypes(String searchTerm, Pageable pageable);
    
    List<CostTypeResponseDto> getAllCostTypesList();
    
    CostTypeResponseDto getCostTypeById(Long id);
    
    CostTypeResponseDto updateCostType(Long id, CostTypeRequestDto requestDto);
    
    void deleteCostType(Long id);
}
