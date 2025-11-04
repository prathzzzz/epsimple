package com.eps.module.api.epsone.cost_item.service;

import com.eps.module.api.epsone.cost_item.dto.CostItemRequestDto;
import com.eps.module.api.epsone.cost_item.dto.CostItemResponseDto;
import com.eps.module.api.epsone.cost_item.mapper.CostItemMapper;
import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.cost_type.repository.CostTypeRepository;
import com.eps.module.cost.CostItem;
import com.eps.module.cost.CostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CostItemServiceImpl implements CostItemService {

    private final CostItemRepository costItemRepository;
    private final CostTypeRepository costTypeRepository;
    private final CostItemMapper costItemMapper;

    @Override
    public CostItemResponseDto createCostItem(CostItemRequestDto requestDto) {
        log.info("Creating cost item for cost type ID: {}", requestDto.getCostTypeId());

        // Validate cost type exists
        CostType costType = costTypeRepository.findById(requestDto.getCostTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cost type not found with ID: " + requestDto.getCostTypeId()));

        // Check if cost item already exists for this cost type
        costItemRepository.findByCostTypeId(requestDto.getCostTypeId())
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "Cost item already exists for cost type: " + costType.getTypeName());
                });

        CostItem costItem = costItemMapper.toEntity(requestDto, costType);
        CostItem savedCostItem = costItemRepository.save(costItem);

        log.info("Cost item created successfully with ID: {}", savedCostItem.getId());
        return costItemMapper.toDto(savedCostItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CostItemResponseDto> getAllCostItems(Pageable pageable) {
        log.info("Fetching all cost items with pagination: {}", pageable);
        Page<CostItem> costItemsPage = costItemRepository.findAll(pageable);
        return costItemsPage.map(costItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CostItemResponseDto> searchCostItems(String searchTerm, Pageable pageable) {
        log.info("Searching cost items with term: {}", searchTerm);
        Page<CostItem> costItemsPage = costItemRepository.searchCostItems(searchTerm, pageable);
        return costItemsPage.map(costItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CostItemResponseDto> getAllCostItemsList() {
        log.info("Fetching all cost items as list");
        List<CostItem> costItems = costItemRepository.findAllList();
        return costItems.stream()
                .map(costItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CostItemResponseDto getCostItemById(Long id) {
        log.info("Fetching cost item by ID: {}", id);
        CostItem costItem = costItemRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Cost item not found with ID: " + id));
        return costItemMapper.toDto(costItem);
    }

    @Override
    public CostItemResponseDto updateCostItem(Long id, CostItemRequestDto requestDto) {
        log.info("Updating cost item with ID: {}", id);

        CostItem existingCostItem = costItemRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Cost item not found with ID: " + id));

        // Validate cost type exists
        CostType costType = costTypeRepository.findById(requestDto.getCostTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cost type not found with ID: " + requestDto.getCostTypeId()));

        // Check if cost type is being changed and if another cost item already uses the new cost type
        if (!existingCostItem.getCostType().getId().equals(requestDto.getCostTypeId())) {
            costItemRepository.findByCostTypeId(requestDto.getCostTypeId())
                    .ifPresent(existing -> {
                        throw new IllegalStateException(
                                "Cost item already exists for cost type: " + costType.getTypeName());
                    });
        }

        costItemMapper.updateEntity(requestDto, costType, existingCostItem);
        CostItem updatedCostItem = costItemRepository.save(existingCostItem);

        log.info("Cost item updated successfully with ID: {}", updatedCostItem.getId());
        return costItemMapper.toDto(updatedCostItem);
    }

    @Override
    public void deleteCostItem(Long id) {
        log.info("Deleting cost item with ID: {}", id);

        CostItem costItem = costItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cost item not found with ID: " + id));

        costItemRepository.delete(costItem);
        log.info("Cost item deleted successfully with ID: {}", id);
    }
}
