package com.eps.module.api.epsone.generic_status_type.service;

import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeBulkUploadDto;
import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeErrorReportDto;
import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeRequestDto;
import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeResponseDto;
import com.eps.module.api.epsone.generic_status_type.mapper.GenericStatusTypeMapper;
import com.eps.module.api.epsone.generic_status_type.processor.GenericStatusTypeBulkUploadProcessor;
import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.status.GenericStatusType;
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
public class GenericStatusTypeServiceImpl extends BaseBulkUploadService<GenericStatusTypeBulkUploadDto, GenericStatusType> implements GenericStatusTypeService {

    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final GenericStatusTypeMapper genericStatusTypeMapper;
    private final GenericStatusTypeBulkUploadProcessor genericStatusTypeBulkUploadProcessor;

    @Override
    @Transactional
    public GenericStatusTypeResponseDto createGenericStatusType(GenericStatusTypeRequestDto requestDto) {
        log.info("Creating new generic status type: {}", requestDto.getStatusName());

        if (genericStatusTypeRepository.existsByStatusNameIgnoreCase(requestDto.getStatusName())) {
            throw new IllegalArgumentException("Status type with name '" + requestDto.getStatusName() + "' already exists");
        }

        if (requestDto.getStatusCode() != null && !requestDto.getStatusCode().isEmpty() &&
            genericStatusTypeRepository.existsByStatusCode(requestDto.getStatusCode())) {
            throw new IllegalArgumentException("Status type with code '" + requestDto.getStatusCode() + "' already exists");
        }

        GenericStatusType genericStatusType = genericStatusTypeMapper.toEntity(requestDto);
        GenericStatusType savedGenericStatusType = genericStatusTypeRepository.save(genericStatusType);

        log.info("Generic status type created successfully with ID: {}", savedGenericStatusType.getId());
        return genericStatusTypeMapper.toResponseDto(savedGenericStatusType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenericStatusTypeResponseDto> getAllGenericStatusTypes(Pageable pageable) {
        log.info("Fetching all generic status types with pagination");
        Page<GenericStatusType> genericStatusTypes = genericStatusTypeRepository.findAll(pageable);
        return genericStatusTypes.map(genericStatusTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GenericStatusTypeResponseDto> searchGenericStatusTypes(String searchTerm, Pageable pageable) {
        log.info("Searching generic status types with term: {}", searchTerm);
        Page<GenericStatusType> genericStatusTypes = genericStatusTypeRepository.searchGenericStatusTypes(searchTerm, pageable);
        return genericStatusTypes.map(genericStatusTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenericStatusTypeResponseDto> getGenericStatusTypeList() {
        log.info("Fetching all generic status types as list");
        List<GenericStatusType> genericStatusTypes = genericStatusTypeRepository.findAll();
        return genericStatusTypes.stream()
                .map(genericStatusTypeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GenericStatusTypeResponseDto getGenericStatusTypeById(Long id) {
        log.info("Fetching generic status type by ID: {}", id);
        GenericStatusType genericStatusType = genericStatusTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Generic status type not found with ID: " + id));
        return genericStatusTypeMapper.toResponseDto(genericStatusType);
    }

    @Override
    @Transactional
    public GenericStatusTypeResponseDto updateGenericStatusType(Long id, GenericStatusTypeRequestDto requestDto) {
        log.info("Updating generic status type with ID: {}", id);

        GenericStatusType genericStatusType = genericStatusTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Generic status type not found with ID: " + id));

        if (genericStatusTypeRepository.existsByStatusNameAndIdNot(requestDto.getStatusName(), id)) {
            throw new IllegalArgumentException("Status type with name '" + requestDto.getStatusName() + "' already exists");
        }

        if (requestDto.getStatusCode() != null && !requestDto.getStatusCode().isEmpty() &&
            genericStatusTypeRepository.existsByStatusCodeAndIdNot(requestDto.getStatusCode(), id)) {
            throw new IllegalArgumentException("Status type with code '" + requestDto.getStatusCode() + "' already exists");
        }

        genericStatusTypeMapper.updateEntityFromDto(requestDto, genericStatusType);
        GenericStatusType updatedGenericStatusType = genericStatusTypeRepository.save(genericStatusType);

        log.info("Generic status type updated successfully with ID: {}", updatedGenericStatusType.getId());
        return genericStatusTypeMapper.toResponseDto(updatedGenericStatusType);
    }

    @Override
    @Transactional
    public void deleteGenericStatusType(Long id) {
        log.info("Deleting generic status type with ID: {}", id);

        GenericStatusType genericStatusType = genericStatusTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Generic status type not found with ID: " + id));

        // Check for dependencies - activity work orders
        long activityWorkCount = activityWorkRepository.countByStatusTypeId(id);
        if (activityWorkCount > 0) {
            throw new IllegalStateException("Cannot delete status type as it has " + activityWorkCount + " associated activity work orders");
        }

        genericStatusTypeRepository.deleteById(id);
        log.info("Generic status type deleted successfully with ID: {}", id);
    }

    // Bulk Upload Methods
    @Override
    protected BulkUploadProcessor<GenericStatusTypeBulkUploadDto, GenericStatusType> getProcessor() {
        return genericStatusTypeBulkUploadProcessor;
    }

    @Override
    public Class<GenericStatusTypeBulkUploadDto> getBulkUploadDtoClass() {
        return GenericStatusTypeBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Generic Status Type";
    }

    @Override
    public List<GenericStatusType> getAllEntitiesForExport() {
        return genericStatusTypeRepository.findAllForExport();
    }

    @Override
    public Function<GenericStatusType, GenericStatusTypeBulkUploadDto> getEntityToDtoMapper() {
        return entity -> GenericStatusTypeBulkUploadDto.builder()
                .statusName(entity.getStatusName())
                .statusCode(entity.getStatusCode())
                .description(entity.getDescription())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        GenericStatusTypeErrorReportDto.GenericStatusTypeErrorReportDtoBuilder builder =
                GenericStatusTypeErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.statusName((String) error.getRowData().get("statusName"))
                    .statusCode((String) error.getRowData().get("statusCode"))
                    .description((String) error.getRowData().get("description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return GenericStatusTypeErrorReportDto.class;
    }
}
