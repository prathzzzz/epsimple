package com.eps.module.api.epsone.vendor.service;

import com.eps.module.api.epsone.persondetails.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.vendor.dto.VendorRequestDto;
import com.eps.module.api.epsone.vendor.dto.VendorResponseDto;
import com.eps.module.api.epsone.vendor.mapper.VendorMapper;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.api.epsone.vendortype.repository.VendorTypeRepository;
import com.eps.module.api.epsone.activitywork.repository.ActivityWorkRepository;
import com.eps.module.person.PersonDetails;
import com.eps.module.vendor.Vendor;
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
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorTypeRepository vendorTypeRepository;
    private final PersonDetailsRepository personDetailsRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final VendorMapper vendorMapper;

    @Override
    @Transactional
    public VendorResponseDto createVendor(VendorRequestDto requestDto) {
        // Validate vendor type exists
        VendorType vendorType = vendorTypeRepository.findById(requestDto.getVendorTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vendor type not found with id: " + requestDto.getVendorTypeId()));

        // Validate person details exists
        PersonDetails personDetails = personDetailsRepository.findById(requestDto.getVendorDetailsId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person details not found with id: " + requestDto.getVendorDetailsId()));

        // Check if person details is already used by another vendor
        if (vendorRepository.findByVendorDetailsId(requestDto.getVendorDetailsId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Person details with id " + requestDto.getVendorDetailsId() + 
                    " is already associated with another vendor");
        }

        // Validate vendor code uniqueness (if provided)
        if (requestDto.getVendorCodeAlt() != null && !requestDto.getVendorCodeAlt().isEmpty()) {
            if (vendorRepository.existsByVendorCodeAlt(requestDto.getVendorCodeAlt())) {
                throw new IllegalArgumentException(
                        "Vendor code '" + requestDto.getVendorCodeAlt() + "' already exists");
            }
        }

        Vendor vendor = vendorMapper.toEntity(requestDto);
        vendor.setVendorType(vendorType);
        vendor.setVendorDetails(personDetails);

        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(savedVendor);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorResponseDto> getAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable)
                .map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorResponseDto> searchVendors(String searchTerm, Pageable pageable) {
        return vendorRepository.searchVendors(searchTerm, pageable)
                .map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorResponseDto> getAllVendorsList() {
        return vendorRepository.findAllList().stream()
                .map(vendorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendorResponseDto> getVendorsByType(Long vendorTypeId, Pageable pageable) {
        // Validate vendor type exists
        if (!vendorTypeRepository.existsById(vendorTypeId)) {
            throw new IllegalArgumentException("Vendor type not found with id: " + vendorTypeId);
        }

        return vendorRepository.findByVendorTypeId(vendorTypeId, pageable)
                .map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponseDto getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + id));
        return vendorMapper.toDto(vendor);
    }

    @Override
    @Transactional
    public VendorResponseDto updateVendor(Long id, VendorRequestDto requestDto) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + id));

        // Validate vendor type exists
        VendorType vendorType = vendorTypeRepository.findById(requestDto.getVendorTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Vendor type not found with id: " + requestDto.getVendorTypeId()));

        // Validate person details exists
        PersonDetails personDetails = personDetailsRepository.findById(requestDto.getVendorDetailsId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person details not found with id: " + requestDto.getVendorDetailsId()));

        // Check if person details is already used by another vendor (excluding current)
        vendorRepository.findByVendorDetailsId(requestDto.getVendorDetailsId())
                .ifPresent(existingVendor -> {
                    if (!existingVendor.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Person details with id " + requestDto.getVendorDetailsId() + 
                                " is already associated with another vendor");
                    }
                });

        // Validate vendor code uniqueness (if provided and changed)
        if (requestDto.getVendorCodeAlt() != null && !requestDto.getVendorCodeAlt().isEmpty()) {
            if (vendorRepository.existsByVendorCodeAltAndIdNot(requestDto.getVendorCodeAlt(), id)) {
                throw new IllegalArgumentException(
                        "Vendor code '" + requestDto.getVendorCodeAlt() + "' already exists");
            }
        }

        vendorMapper.updateEntityFromDto(requestDto, vendor);
        vendor.setVendorType(vendorType);
        vendor.setVendorDetails(personDetails);

        Vendor updatedVendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(updatedVendor);
    }

    @Override
    @Transactional
    public void deleteVendor(Long id) {
        if (!vendorRepository.existsById(id)) {
            throw new IllegalArgumentException("Vendor not found with id: " + id);
        }

        // Check for dependencies - activity work orders
        long activityWorkCount = activityWorkRepository.countByVendorId(id);
        if (activityWorkCount > 0) {
            throw new IllegalStateException("Cannot delete vendor as it has " + activityWorkCount + " associated activity work orders");
        }

        vendorRepository.deleteById(id);
    }
}
