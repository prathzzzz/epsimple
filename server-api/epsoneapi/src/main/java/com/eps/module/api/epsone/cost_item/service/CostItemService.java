package com.eps.module.api.epsone.cost_item.service;

import com.eps.module.api.epsone.cost_item.dto.CostItemRequestDto;
import com.eps.module.api.epsone.cost_item.dto.CostItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CostItemService {

    CostItemResponseDto createCostItem(CostItemRequestDto requestDto);

    Page<CostItemResponseDto> getAllCostItems(Pageable pageable);

    Page<CostItemResponseDto> searchCostItems(String searchTerm, Pageable pageable);

    List<CostItemResponseDto> getAllCostItemsList();

    CostItemResponseDto getCostItemById(Long id);

    CostItemResponseDto updateCostItem(Long id, CostItemRequestDto requestDto);

    void deleteCostItem(Long id);
}
