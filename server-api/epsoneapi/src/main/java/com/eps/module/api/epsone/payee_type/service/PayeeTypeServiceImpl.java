package com.eps.module.api.epsone.payee_type.service;

import com.eps.module.api.epsone.payee_type.dto.PayeeTypeBulkUploadDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeErrorReportDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeRequestDto;
import com.eps.module.api.epsone.payee_type.dto.PayeeTypeResponseDto;
import com.eps.module.api.epsone.payee_type.mapper.PayeeTypeMapper;
import com.eps.module.api.epsone.payee_type.processor.PayeeTypeBulkUploadProcessor;
import com.eps.module.api.epsone.payee_type.repository.PayeeTypeRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.payment.PayeeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayeeTypeServiceImpl extends BaseBulkUploadService<PayeeTypeBulkUploadDto, PayeeType>
        implements PayeeTypeService {

    private final PayeeTypeRepository payeeTypeRepository;
    private final PayeeRepository payeeRepository;
    private final PayeeTypeMapper payeeTypeMapper;
    private final PayeeTypeBulkUploadProcessor payeeTypeBulkUploadProcessor;

    @Override
    @Transactional
    public PayeeTypeResponseDto createPayeeType(PayeeTypeRequestDto requestDto) {
        log.info("Creating payee type: {}", requestDto.getPayeeType());

        if (payeeTypeRepository.existsByPayeeTypeIgnoreCase(requestDto.getPayeeType())) {
            throw new IllegalArgumentException("Payee type '" + requestDto.getPayeeType() + "' already exists");
        }

        PayeeType payeeType = payeeTypeMapper.toEntity(requestDto);
        PayeeType savedPayeeType = payeeTypeRepository.save(payeeType);

        log.info("Payee type created successfully with ID: {}", savedPayeeType.getId());
        return payeeTypeMapper.toResponseDto(savedPayeeType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeTypeResponseDto> getAllPayeeTypes(Pageable pageable) {
        log.info("Fetching all payee types with pagination: {}", pageable);
        Page<PayeeType> payeeTypes = payeeTypeRepository.findAll(pageable);
        return payeeTypes.map(payeeTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeTypeResponseDto> searchPayeeTypes(String searchTerm, Pageable pageable) {
        log.info("Searching payee types with term: {}", searchTerm);
        Page<PayeeType> payeeTypes = payeeTypeRepository.searchPayeeTypes(searchTerm, pageable);
        return payeeTypes.map(payeeTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayeeTypeResponseDto> getPayeeTypesList() {
        log.info("Fetching all payee types as list");
        return payeeTypeRepository.findAll().stream()
                .map(payeeTypeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PayeeTypeResponseDto getPayeeTypeById(Long id) {
        log.info("Fetching payee type with ID: {}", id);
        PayeeType payeeType = payeeTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payee type not found with ID: " + id));
        return payeeTypeMapper.toResponseDto(payeeType);
    }

    @Override
    @Transactional
    public PayeeTypeResponseDto updatePayeeType(Long id, PayeeTypeRequestDto requestDto) {
        log.info("Updating payee type with ID: {}", id);

        PayeeType payeeType = payeeTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payee type not found with ID: " + id));

        if (payeeTypeRepository.existsByPayeeTypeAndIdNot(requestDto.getPayeeType(), id)) {
            throw new IllegalArgumentException("Payee type '" + requestDto.getPayeeType() + "' already exists");
        }

        payeeTypeMapper.updateEntityFromDto(requestDto, payeeType);
        PayeeType updatedPayeeType = payeeTypeRepository.save(payeeType);

        log.info("Payee type updated successfully with ID: {}", id);
        return payeeTypeMapper.toResponseDto(updatedPayeeType);
    }

    @Override
    @Transactional
    public void deletePayeeType(Long id) {
        log.info("Deleting payee type with ID: {}", id);

        if (!payeeTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("Payee type not found with ID: " + id);
        }

        // Check for dependencies - payees
        long payeeCount = payeeRepository.countByPayeeTypeId(id);
        if (payeeCount > 0) {
            throw new IllegalStateException("Cannot delete payee type as it has " + payeeCount + " associated payees");
        }

        payeeTypeRepository.deleteById(id);
        log.info("Payee type deleted successfully with ID: {}", id);
    }

    // Bulk upload methods
    @Override
    protected BulkUploadProcessor<PayeeTypeBulkUploadDto, PayeeType> getProcessor() {
        return payeeTypeBulkUploadProcessor;
    }

    @Override
    public Class<PayeeTypeBulkUploadDto> getBulkUploadDtoClass() {
        return PayeeTypeBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "PayeeType";
    }

    @Override
    public List<PayeeType> getAllEntitiesForExport() {
        return payeeTypeRepository.findAllForExport();
    }

    @Override
    public Function<PayeeType, PayeeTypeBulkUploadDto> getEntityToDtoMapper() {
        return entity -> PayeeTypeBulkUploadDto.builder()
                .payeeType(entity.getPayeeType())
                .payeeCategory(entity.getPayeeCategory())
                .description(entity.getDescription())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        PayeeTypeErrorReportDto.PayeeTypeErrorReportDtoBuilder builder =
                PayeeTypeErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorType(error.getErrorType())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.payeeType((String) error.getRowData().get("Payee Type"))
                    .payeeCategory((String) error.getRowData().get("Payee Category"))
                    .description((String) error.getRowData().get("Description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return PayeeTypeErrorReportDto.class;
    }
}
