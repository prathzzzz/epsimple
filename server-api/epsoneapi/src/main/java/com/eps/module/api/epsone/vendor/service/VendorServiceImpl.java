package com.eps.module.api.epsone.vendor.service;


import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.vendor.constant.VendorErrorMessages;
import com.eps.module.api.epsone.vendor.dto.VendorBulkUploadDto;
import com.eps.module.api.epsone.vendor.dto.VendorErrorReportDto;
import com.eps.module.api.epsone.vendor.dto.VendorRequestDto;
import com.eps.module.api.epsone.vendor.dto.VendorResponseDto;
import com.eps.module.api.epsone.vendor.mapper.VendorMapper;
import com.eps.module.api.epsone.vendor.processor.VendorBulkUploadProcessor;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.api.epsone.vendor_type.repository.VendorTypeRepository;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.BadRequestException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.person.PersonDetails;
import com.eps.module.vendor.Vendor;
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
public class VendorServiceImpl extends BaseBulkUploadService<VendorBulkUploadDto, Vendor> implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorTypeRepository vendorTypeRepository;
    private final PersonDetailsRepository personDetailsRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final PayeeRepository payeeRepository;
    private final VendorMapper vendorMapper;
    private final VendorBulkUploadProcessor vendorBulkUploadProcessor;

    @Override
    @Transactional
    public VendorResponseDto createVendor(VendorRequestDto requestDto) {
        // Validate vendor type exists
        VendorType vendorType = vendorTypeRepository.findById(requestDto.getVendorTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        VendorErrorMessages.VENDOR_TYPE_NOT_FOUND + requestDto.getVendorTypeId()));

        // Validate person details exists
        PersonDetails personDetails = personDetailsRepository.findById(requestDto.getVendorDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        VendorErrorMessages.PERSON_DETAILS_NOT_FOUND + requestDto.getVendorDetailsId()));

        // Check if person details is already used by another vendor
        if (vendorRepository.findByVendorDetailsId(requestDto.getVendorDetailsId()).isPresent()) {
            throw new BadRequestException(String.format(
                    VendorErrorMessages.PERSON_DETAILS_ALREADY_ASSOCIATED, requestDto.getVendorDetailsId()));
        }

        // Validate vendor code uniqueness (if provided)
        if (requestDto.getVendorCodeAlt() != null && !requestDto.getVendorCodeAlt().isEmpty()) {
            if (vendorRepository.existsByVendorCodeAlt(requestDto.getVendorCodeAlt())) {
                throw new BadRequestException(String.format(
                        VendorErrorMessages.VENDOR_CODE_EXISTS, requestDto.getVendorCodeAlt()));
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
            throw new ResourceNotFoundException(VendorErrorMessages.VENDOR_TYPE_NOT_FOUND + vendorTypeId);
        }

        return vendorRepository.findByVendorTypeId(vendorTypeId, pageable)
                .map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public VendorResponseDto getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VendorErrorMessages.VENDOR_NOT_FOUND_ID + id));
        return vendorMapper.toDto(vendor);
    }

    @Override
    @Transactional
    public VendorResponseDto updateVendor(Long id, VendorRequestDto requestDto) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VendorErrorMessages.VENDOR_NOT_FOUND_ID + id));

        // Validate vendor type exists
        VendorType vendorType = vendorTypeRepository.findById(requestDto.getVendorTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        VendorErrorMessages.VENDOR_TYPE_NOT_FOUND + requestDto.getVendorTypeId()));

        // Validate person details exists
        PersonDetails personDetails = personDetailsRepository.findById(requestDto.getVendorDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        VendorErrorMessages.PERSON_DETAILS_NOT_FOUND + requestDto.getVendorDetailsId()));

        // Check if person details is already used by another vendor (excluding current)
        vendorRepository.findByVendorDetailsId(requestDto.getVendorDetailsId())
                .ifPresent(existingVendor -> {
                    if (!existingVendor.getId().equals(id)) {
                        throw new BadRequestException(String.format(
                                VendorErrorMessages.PERSON_DETAILS_ALREADY_ASSOCIATED, requestDto.getVendorDetailsId()));
                    }
                });

        // Validate vendor code uniqueness (if provided and changed)
        if (requestDto.getVendorCodeAlt() != null && !requestDto.getVendorCodeAlt().isEmpty()) {
            if (vendorRepository.existsByVendorCodeAltAndIdNot(requestDto.getVendorCodeAlt(), id)) {
                throw new BadRequestException(String.format(
                        VendorErrorMessages.VENDOR_CODE_EXISTS, requestDto.getVendorCodeAlt()));
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
            throw new ResourceNotFoundException(VendorErrorMessages.VENDOR_NOT_FOUND_ID + id);
        }

        // Check for dependencies - activity work orders
        long activityWorkCount = activityWorkRepository.countByVendorId(id);
        if (activityWorkCount > 0) {
            throw new BadRequestException(String.format(VendorErrorMessages.CANNOT_DELETE_VENDOR_ACTIVITY_WORK, activityWorkCount));
        }

        // Check for dependencies - payees
        long payeeCount = payeeRepository.countByVendorId(id);
        if (payeeCount > 0) {
            throw new BadRequestException(String.format(VendorErrorMessages.CANNOT_DELETE_VENDOR_PAYEES, payeeCount));
        }

        vendorRepository.deleteById(id);
    }

    // Bulk Upload Methods
    @Override
    protected BulkUploadProcessor<VendorBulkUploadDto, Vendor> getProcessor() {
        return vendorBulkUploadProcessor;
    }

    @Override
    public Class<VendorBulkUploadDto> getBulkUploadDtoClass() {
        return VendorBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Vendor";
    }

    @Override
    public List<Vendor> getAllEntitiesForExport() {
        return vendorRepository.findAllForExport();
    }

    @Override
    public Function<Vendor, VendorBulkUploadDto> getEntityToDtoMapper() {
        return entity -> VendorBulkUploadDto.builder()
                .contactNumber(entity.getVendorDetails() != null ? entity.getVendorDetails().getContactNumber() : "")
                .vendorTypeName(entity.getVendorType() != null ? entity.getVendorType().getTypeName() : "")
                .vendorCodeAlt(entity.getVendorCodeAlt())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        VendorErrorReportDto.VendorErrorReportDtoBuilder builder =
                VendorErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.contactNumber((String) error.getRowData().get("contactNumber"))
                    .vendorTypeName((String) error.getRowData().get("vendorTypeName"))
                    .vendorCodeAlt((String) error.getRowData().get("vendorCodeAlt"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return VendorErrorReportDto.class;
    }
}
