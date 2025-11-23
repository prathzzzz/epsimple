package com.eps.module.api.epsone.cost_type.service;

import com.eps.module.api.epsone.cost_category.repository.CostCategoryRepository;
import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.cost_type.constant.CostTypeErrorMessages;
import com.eps.module.api.epsone.cost_type.dto.CostTypeBulkUploadDto;
import com.eps.module.api.epsone.cost_type.dto.CostTypeErrorReportDto;
import com.eps.module.api.epsone.cost_type.dto.CostTypeRequestDto;
import com.eps.module.api.epsone.cost_type.dto.CostTypeResponseDto;
import com.eps.module.api.epsone.cost_type.mapper.CostTypeMapper;
import com.eps.module.api.epsone.cost_type.processor.CostTypeBulkUploadProcessor;
import com.eps.module.api.epsone.cost_type.repository.CostTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.CostCategory;
import com.eps.module.cost.CostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CostTypeServiceImpl extends BaseBulkUploadService<CostTypeBulkUploadDto, CostType> implements CostTypeService {
    
    private final CostTypeRepository costTypeRepository;
    private final CostCategoryRepository costCategoryRepository;
    private final CostItemRepository costItemRepository;
    private final CostTypeMapper costTypeMapper;
    private final CostTypeBulkUploadProcessor bulkUploadProcessor;
    
    @Override
    @Transactional
    public CostTypeResponseDto createCostType(CostTypeRequestDto requestDto) {
        log.info("Creating cost type: {}", requestDto.getTypeName());
        
        CostCategory costCategory = costCategoryRepository.findById(requestDto.getCostCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CostTypeErrorMessages.COST_CATEGORY_NOT_FOUND_ID + requestDto.getCostCategoryId()));
        
        CostType entity = CostType.builder()
                .typeName(requestDto.getTypeName())
                .typeDescription(requestDto.getTypeDescription())
                .costCategory(costCategory)
                .build();
        
        CostType saved = costTypeRepository.save(entity);
        
        log.info("Cost type created successfully with ID: {}", saved.getId());
        return costTypeMapper.toResponseDto(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CostTypeResponseDto> getAllCostTypes(Pageable pageable) {
        log.info("Fetching all cost types with pagination");
        return costTypeRepository.findAll(pageable)
                .map(costTypeMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CostTypeResponseDto> searchCostTypes(String searchTerm, Pageable pageable) {
        log.info("Searching cost types with term: {}", searchTerm);
        return costTypeRepository.searchCostTypes(searchTerm, pageable)
                .map(costTypeMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CostTypeResponseDto> getAllCostTypesList() {
        log.info("Fetching all cost types as list");
        return costTypeRepository.findAll().stream()
                .map(costTypeMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CostTypeResponseDto getCostTypeById(Long id) {
        log.info("Fetching cost type by ID: {}", id);
        CostType entity = costTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CostTypeErrorMessages.COST_TYPE_NOT_FOUND_ID + id));
        return costTypeMapper.toResponseDto(entity);
    }
    
    @Override
    @Transactional
    public CostTypeResponseDto updateCostType(Long id, CostTypeRequestDto requestDto) {
        log.info("Updating cost type with ID: {}", id);
        
        CostType existing = costTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CostTypeErrorMessages.COST_TYPE_NOT_FOUND_ID + id));
        
        CostCategory costCategory = costCategoryRepository.findById(requestDto.getCostCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(CostTypeErrorMessages.COST_CATEGORY_NOT_FOUND_ID + requestDto.getCostCategoryId()));
        
        existing.setTypeName(requestDto.getTypeName());
        existing.setTypeDescription(requestDto.getTypeDescription());
        existing.setCostCategory(costCategory);
        
        CostType updated = costTypeRepository.save(existing);
        
        log.info("Cost type updated successfully with ID: {}", id);
        return costTypeMapper.toResponseDto(updated);
    }
    
    @Override
    @Transactional
    public void deleteCostType(Long id) {
        log.info("Deleting cost type with ID: {}", id);
        
        CostType costType = costTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CostTypeErrorMessages.COST_TYPE_NOT_FOUND_ID + id));
        
        // Check if cost type is being used by any cost items
        long costItemCount = costItemRepository.countByCostTypeId(id);
        if (costItemCount > 0) {
            throw new ConflictException(
                    String.format(CostTypeErrorMessages.CANNOT_DELETE_COST_TYPE_IN_USE,
                            costType.getTypeName(), costItemCount));
        }
        
        costTypeRepository.deleteById(id);
        log.info("Cost type deleted successfully with ID: {}", id);
    }

    // =====================
    // Bulk Upload Methods
    // =====================

    @Override
    protected BulkUploadProcessor<CostTypeBulkUploadDto, CostType> getProcessor() {
        return bulkUploadProcessor;
    }

    @Override
    public Class<CostTypeBulkUploadDto> getBulkUploadDtoClass() {
        return CostTypeBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "CostType";
    }

    @Override
    public List<CostType> getAllEntitiesForExport() {
        return costTypeRepository.findAllForExport();
    }

    @Override
    public Function<CostType, CostTypeBulkUploadDto> getEntityToDtoMapper() {
        return ct -> CostTypeBulkUploadDto.builder()
                .typeName(ct.getTypeName())
                .typeDescription(ct.getTypeDescription())
                .costCategoryName(ct.getCostCategory() != null ? ct.getCostCategory().getCategoryName() : null)
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        CostTypeErrorReportDto.CostTypeErrorReportDtoBuilder builder =
                CostTypeErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorType(error.getErrorType())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.typeName((String) error.getRowData().get("typeName"))
                    .typeDescription((String) error.getRowData().get("typeDescription"))
                    .costCategoryName((String) error.getRowData().get("costCategoryName"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return CostTypeErrorReportDto.class;
    }
}
