package com.eps.module.api.epsone.landlord.service;

import com.eps.module.api.epsone.landlord.constant.LandlordErrorMessages;
import com.eps.module.api.epsone.landlord.dto.LandlordBulkUploadDto;
import com.eps.module.api.epsone.landlord.dto.LandlordErrorReportDto;
import com.eps.module.api.epsone.landlord.dto.LandlordRequestDto;
import com.eps.module.api.epsone.landlord.dto.LandlordResponseDto;
import com.eps.module.api.epsone.landlord.mapper.LandlordMapper;
import com.eps.module.api.epsone.landlord.processor.LandlordBulkUploadProcessor;
import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.person.PersonDetails;
import com.eps.module.vendor.Landlord;
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
public class LandlordServiceImpl extends BaseBulkUploadService<LandlordBulkUploadDto, Landlord> implements LandlordService {

    private final LandlordRepository landlordRepository;
    private final PersonDetailsRepository personDetailsRepository;
    private final PayeeRepository payeeRepository;
    private final LandlordMapper landlordMapper;
    private final LandlordBulkUploadProcessor landlordBulkUploadProcessor;

    @Override
    @Transactional
    public LandlordResponseDto createLandlord(LandlordRequestDto requestDto) {
        // Validate person details exists
        PersonDetails personDetails = personDetailsRepository.findById(requestDto.getLandlordDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        LandlordErrorMessages.PERSON_DETAILS_NOT_FOUND_ID + requestDto.getLandlordDetailsId()));

        // Check if person details is already used by another landlord
        if (landlordRepository.findByLandlordDetailsId(requestDto.getLandlordDetailsId()).isPresent()) {
            throw new ConflictException(String.format(
                    LandlordErrorMessages.PERSON_DETAILS_ALREADY_ASSOCIATED, requestDto.getLandlordDetailsId()));
        }

        Landlord landlord = landlordMapper.toEntity(requestDto);
        landlord.setLandlordDetails(personDetails);

        Landlord savedLandlord = landlordRepository.save(landlord);
        return landlordMapper.toDto(savedLandlord);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LandlordResponseDto> getAllLandlords(Pageable pageable) {
        return landlordRepository.findAll(pageable)
                .map(landlordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LandlordResponseDto> searchLandlords(String searchTerm, Pageable pageable) {
        return landlordRepository.searchLandlords(searchTerm, pageable)
                .map(landlordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LandlordResponseDto> getAllLandlordsList() {
        return landlordRepository.findAllList().stream()
                .map(landlordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LandlordResponseDto getLandlordById(Long id) {
        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LandlordErrorMessages.LANDLORD_NOT_FOUND_ID + id));
        return landlordMapper.toDto(landlord);
    }

    @Override
    @Transactional
    public LandlordResponseDto updateLandlord(Long id, LandlordRequestDto requestDto) {
        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LandlordErrorMessages.LANDLORD_NOT_FOUND_ID + id));

        // Validate person details exists
        PersonDetails personDetails = personDetailsRepository.findById(requestDto.getLandlordDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        LandlordErrorMessages.PERSON_DETAILS_NOT_FOUND_ID + requestDto.getLandlordDetailsId()));

        // Check if person details is already used by another landlord (excluding current)
        landlordRepository.findByLandlordDetailsId(requestDto.getLandlordDetailsId())
                .ifPresent(existingLandlord -> {
                    if (!existingLandlord.getId().equals(id)) {
                        throw new ConflictException(String.format(
                                LandlordErrorMessages.PERSON_DETAILS_ALREADY_ASSOCIATED, requestDto.getLandlordDetailsId()));
                    }
                });

        landlordMapper.updateEntityFromDto(requestDto, landlord);
        landlord.setLandlordDetails(personDetails);

        Landlord updatedLandlord = landlordRepository.save(landlord);
        return landlordMapper.toDto(updatedLandlord);
    }

    @Override
    @Transactional
    public void deleteLandlord(Long id) {
        if (!landlordRepository.existsById(id)) {
            throw new ResourceNotFoundException(LandlordErrorMessages.LANDLORD_NOT_FOUND_ID + id);
        }

        // Check for dependencies - payees
        long payeeCount = payeeRepository.countByLandlordId(id);
        if (payeeCount > 0) {
            throw new ConflictException(String.format(LandlordErrorMessages.CANNOT_DELETE_LANDLORD_PAYEES, payeeCount));
        }

        landlordRepository.deleteById(id);
    }

    // Bulk Upload Methods
    @Override
    protected BulkUploadProcessor<LandlordBulkUploadDto, Landlord> getProcessor() {
        return landlordBulkUploadProcessor;
    }

    @Override
    public Class<LandlordBulkUploadDto> getBulkUploadDtoClass() {
        return LandlordBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Landlord";
    }

    @Override
    public List<Landlord> getAllEntitiesForExport() {
        return landlordRepository.findAllForExport();
    }

    @Override
    public Function<Landlord, LandlordBulkUploadDto> getEntityToDtoMapper() {
        return entity -> LandlordBulkUploadDto.builder()
                .contactNumber(entity.getLandlordDetails() != null ? entity.getLandlordDetails().getContactNumber() : "")
                .rentSharePercentage(entity.getRentSharePercentage())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        LandlordErrorReportDto.LandlordErrorReportDtoBuilder builder =
                LandlordErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.contactNumber((String) error.getRowData().get("contactNumber"));
            
            // Handle rentSharePercentage - might be Integer, BigDecimal, or other Number type
            Object rentShareObj = error.getRowData().get("rentSharePercentage");
            if (rentShareObj instanceof Number) {
                builder.rentSharePercentage(new java.math.BigDecimal(rentShareObj.toString()));
            } else if (rentShareObj != null) {
                builder.rentSharePercentage(java.math.BigDecimal.valueOf(Double.parseDouble(rentShareObj.toString())));
            }
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return LandlordErrorReportDto.class;
    }
}
