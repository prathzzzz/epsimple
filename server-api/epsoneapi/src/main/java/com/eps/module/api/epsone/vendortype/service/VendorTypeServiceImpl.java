package com.eps.module.api.epsone.vendortype.service;

import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.api.epsone.vendorcategory.repository.VendorCategoryRepository;
import com.eps.module.api.epsone.vendortype.dto.VendorTypeRequestDto;
import com.eps.module.api.epsone.vendortype.dto.VendorTypeResponseDto;
import com.eps.module.api.epsone.vendortype.mapper.VendorTypeMapper;
import com.eps.module.api.epsone.vendortype.repository.VendorTypeRepository;
import com.eps.module.person.PersonDetails;
import com.eps.module.vendor.Vendor;
import com.eps.module.vendor.VendorCategory;
import com.eps.module.vendor.VendorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorTypeServiceImpl implements VendorTypeService {

    private final VendorTypeRepository vendorTypeRepository;
    private final VendorCategoryRepository vendorCategoryRepository;
    private final VendorRepository vendorRepository;
    private final VendorTypeMapper vendorTypeMapper;

    @Override
    @Transactional
    public VendorTypeResponseDto createVendorType(VendorTypeRequestDto requestDto) {
        // Check if vendor type name already exists
        if (vendorTypeRepository.existsByTypeNameIgnoreCase(requestDto.getTypeName())) {
            throw new IllegalArgumentException("Vendor type with name '" + requestDto.getTypeName() + "' already exists");
        }
        
        // Validate and fetch vendor category
        VendorCategory vendorCategory = vendorCategoryRepository.findById(requestDto.getVendorCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Vendor category not found with id: " + requestDto.getVendorCategoryId()));
        
        VendorType vendorType = vendorTypeMapper.toEntity(requestDto);
        vendorType.setVendorCategory(vendorCategory);
        VendorType savedVendorType = vendorTypeRepository.save(vendorType);
        return vendorTypeMapper.toResponseDto(savedVendorType);
    }

    @Override
    @Transactional
    public VendorTypeResponseDto updateVendorType(Long id, VendorTypeRequestDto requestDto) {
        VendorType existingVendorType = vendorTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Vendor type not found with id: " + id));
        
        // Check if vendor type name already exists for another vendor type
        if (vendorTypeRepository.existsByTypeNameAndIdNot(requestDto.getTypeName(), id)) {
            throw new IllegalArgumentException("Vendor type with name '" + requestDto.getTypeName() + "' already exists");
        }
        
        // Validate and fetch vendor category
        VendorCategory vendorCategory = vendorCategoryRepository.findById(requestDto.getVendorCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Vendor category not found with id: " + requestDto.getVendorCategoryId()));
        
        vendorTypeMapper.updateEntityFromDto(requestDto, existingVendorType);
        existingVendorType.setVendorCategory(vendorCategory);
        VendorType updatedVendorType = vendorTypeRepository.save(existingVendorType);
        return vendorTypeMapper.toResponseDto(updatedVendorType);
    }

    @Override
    @Transactional
    public void deleteVendorType(Long id) {
        VendorType vendorType = vendorTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vendor type not found with id: " + id));

        // Check for dependent vendors
        long vendorCount = vendorRepository.countByVendorTypeId(id);
        if (vendorCount > 0) {
            // Fetch up to 6 vendors (show first 5 + determine if more)
            Page<Vendor> vendorsPage = vendorRepository.findByVendorTypeId(
                    id, PageRequest.of(0, 6));
            List<Vendor> vendors = vendorsPage.getContent();

            // Build list of vendor names (up to 5)
            String vendorNames = vendors.stream()
                    .limit(5)
                    .map(this::buildVendorName)
                    .collect(Collectors.joining(", "));

            // Add "and X more" if there are more than 5
            String moreText = vendorCount > 5 ? " and " + (vendorCount - 5) + " more" : "";

            throw new IllegalArgumentException(
                    "Cannot delete '" + vendorType.getTypeName() + "' vendor type because it is being used by " + 
                    vendorCount + " vendor(s): " + vendorNames + moreText + ". " +
                    "Please delete or reassign these vendors first.");
        }

        vendorTypeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorTypeResponseDto getVendorTypeById(Long id) {
        VendorType vendorType = vendorTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Vendor type not found with id: " + id));
        return vendorTypeMapper.toResponseDto(vendorType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorTypeResponseDto> getAllVendorTypes(Pageable pageable) {
        Page<VendorType> vendorTypes = vendorTypeRepository.findAll(pageable);
        return vendorTypes.map(vendorTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorTypeResponseDto> searchVendorTypes(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getAllVendorTypes(pageable);
        }
        Page<VendorType> vendorTypes = vendorTypeRepository.searchVendorTypes(search.trim(), pageable);
        return vendorTypes.map(vendorTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorTypeResponseDto> getAllVendorTypesList() {
        List<VendorType> vendorTypes = vendorTypeRepository.findAll();
        return vendorTypeMapper.toResponseDtoList(vendorTypes);
    }

    /**
     * Build vendor name from person details
     */
    private String buildVendorName(Vendor vendor) {
        PersonDetails details = vendor.getVendorDetails();
        if (details == null) {
            return "Unknown Vendor";
        }
        
        StringBuilder fullName = new StringBuilder();
        if (details.getFirstName() != null && !details.getFirstName().isEmpty()) {
            fullName.append(details.getFirstName());
        }
        if (details.getMiddleName() != null && !details.getMiddleName().isEmpty()) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(details.getMiddleName());
        }
        if (details.getLastName() != null && !details.getLastName().isEmpty()) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(details.getLastName());
        }
        
        return !fullName.isEmpty() ? fullName.toString() : "Unknown Vendor";
    }
}
