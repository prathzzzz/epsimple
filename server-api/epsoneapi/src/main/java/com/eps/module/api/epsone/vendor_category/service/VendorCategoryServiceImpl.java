package com.eps.module.api.epsone.vendor_category.service;

import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryBulkUploadDto;
import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryErrorReportDto;
import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryRequestDto;
import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryResponseDto;
import com.eps.module.api.epsone.vendor_category.mapper.VendorCategoryMapper;
import com.eps.module.api.epsone.vendor_category.processor.VendorCategoryBulkUploadProcessor;
import com.eps.module.api.epsone.vendor_category.repository.VendorCategoryRepository;
import com.eps.module.api.epsone.vendor_type.repository.VendorTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ForeignKeyConstraintException;
import com.eps.module.vendor.VendorCategory;
import com.eps.module.vendor.VendorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorCategoryServiceImpl extends BaseBulkUploadService<VendorCategoryBulkUploadDto, VendorCategory> implements VendorCategoryService {

    private final VendorCategoryRepository vendorCategoryRepository;
    private final VendorCategoryMapper vendorCategoryMapper;
    private final VendorTypeRepository vendorTypeRepository;
    private final VendorCategoryBulkUploadProcessor vendorCategoryBulkUploadProcessor;

    @Override
    @Transactional
    public VendorCategoryResponseDto createVendorCategory(VendorCategoryRequestDto requestDto) {
        // Check if category name already exists
        if (vendorCategoryRepository.existsByCategoryNameIgnoreCase(requestDto.getCategoryName())) {
            throw new IllegalArgumentException("Vendor category with name '" + requestDto.getCategoryName() + "' already exists");
        }
        
        VendorCategory vendorCategory = vendorCategoryMapper.toEntity(requestDto);
        VendorCategory savedVendorCategory = vendorCategoryRepository.save(vendorCategory);
        return vendorCategoryMapper.toResponseDto(savedVendorCategory);
    }

    @Override
    @Transactional
    public VendorCategoryResponseDto updateVendorCategory(Long id, VendorCategoryRequestDto requestDto) {
        VendorCategory existingVendorCategory = vendorCategoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.ENTITY_NOT_FOUND_SIMPLE, "Vendor category", id)));
        
        // Check if category name already exists for another category
        if (vendorCategoryRepository.existsByCategoryNameAndIdNot(requestDto.getCategoryName(), id)) {
            throw new IllegalArgumentException("Vendor category with name '" + requestDto.getCategoryName() + "' already exists");
        }
        
        vendorCategoryMapper.updateEntityFromDto(requestDto, existingVendorCategory);
        VendorCategory updatedVendorCategory = vendorCategoryRepository.save(existingVendorCategory);
        return vendorCategoryMapper.toResponseDto(updatedVendorCategory);
    }

    @Override
    @Transactional
    public void deleteVendorCategory(Long id) {
        VendorCategory vendorCategory = vendorCategoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.ENTITY_NOT_FOUND_SIMPLE, "Vendor category", id)));
        
        // Check if this category is being used by any vendor types
        List<VendorType> dependentTypes = vendorTypeRepository.findByVendorCategoryId(id);
        if (!dependentTypes.isEmpty()) {
            String typeNames = dependentTypes.stream()
                .limit(3) // Show max 3 names
                .map(VendorType::getTypeName)
                .collect(Collectors.joining(", "));
            
            // If there are more than 3, add "and X more"
            if (dependentTypes.size() > 3) {
                typeNames += " and " + (dependentTypes.size() - 3) + " more";
            }
            
            throw new ForeignKeyConstraintException(
                vendorCategory.getCategoryName(), 
                "Vendor Category", 
                typeNames, 
                null
            );
        }
        
        vendorCategoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorCategoryResponseDto getVendorCategoryById(Long id) {
        VendorCategory vendorCategory = vendorCategoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.ENTITY_NOT_FOUND_SIMPLE, "Vendor category", id)));
        return vendorCategoryMapper.toResponseDto(vendorCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorCategoryResponseDto> getAllVendorCategories(Pageable pageable) {
        Page<VendorCategory> vendorCategories = vendorCategoryRepository.findAll(pageable);
        return vendorCategories.map(vendorCategoryMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorCategoryResponseDto> searchVendorCategories(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getAllVendorCategories(pageable);
        }
        Page<VendorCategory> vendorCategories = vendorCategoryRepository.searchVendorCategories(search.trim(), pageable);
        return vendorCategories.map(vendorCategoryMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorCategoryResponseDto> getAllVendorCategoriesList() {
        List<VendorCategory> vendorCategories = vendorCategoryRepository.findAll();
        return vendorCategoryMapper.toResponseDtoList(vendorCategories);
    }

    @Override
    protected BulkUploadProcessor<VendorCategoryBulkUploadDto, VendorCategory> getProcessor() {
        return vendorCategoryBulkUploadProcessor;
    }

    @Override
    public Class<VendorCategoryBulkUploadDto> getBulkUploadDtoClass() {
        return VendorCategoryBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "VendorCategory";
    }

    @Override
    public List<VendorCategory> getAllEntitiesForExport() {
        return vendorCategoryRepository.findAllForExport();
    }

    @Override
    public Function<VendorCategory, VendorCategoryBulkUploadDto> getEntityToDtoMapper() {
        return entity -> VendorCategoryBulkUploadDto.builder()
                .categoryName(entity.getCategoryName())
                .description(entity.getDescription())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        VendorCategoryErrorReportDto.VendorCategoryErrorReportDtoBuilder builder =
                VendorCategoryErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorType(error.getErrorType())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.categoryName((String) error.getRowData().get("categoryName"))
                    .description((String) error.getRowData().get("description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return VendorCategoryErrorReportDto.class;
    }
}
