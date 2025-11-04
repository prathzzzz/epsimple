package com.eps.module.api.epsone.vendor_category.service;

import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryRequestDto;
import com.eps.module.api.epsone.vendor_category.dto.VendorCategoryResponseDto;
import com.eps.module.api.epsone.vendor_category.mapper.VendorCategoryMapper;
import com.eps.module.api.epsone.vendor_category.repository.VendorCategoryRepository;
import com.eps.module.api.epsone.vendor_type.repository.VendorTypeRepository;
import com.eps.module.common.exception.ForeignKeyConstraintException;
import com.eps.module.vendor.VendorCategory;
import com.eps.module.vendor.VendorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorCategoryServiceImpl implements VendorCategoryService {

    private final VendorCategoryRepository vendorCategoryRepository;
    private final VendorCategoryMapper vendorCategoryMapper;
    private final VendorTypeRepository vendorTypeRepository;

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
            .orElseThrow(() -> new IllegalArgumentException("Vendor category not found with id: " + id));
        
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
            .orElseThrow(() -> new IllegalArgumentException("Vendor category not found with id: " + id));
        
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
            .orElseThrow(() -> new IllegalArgumentException("Vendor category not found with id: " + id));
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
}
