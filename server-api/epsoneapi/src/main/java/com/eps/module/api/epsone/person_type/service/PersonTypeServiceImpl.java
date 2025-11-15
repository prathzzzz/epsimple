package com.eps.module.api.epsone.person_type.service;

import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.person_type.dto.PersonTypeBulkUploadDto;
import com.eps.module.api.epsone.person_type.dto.PersonTypeErrorReportDto;
import com.eps.module.api.epsone.person_type.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.person_type.dto.PersonTypeResponseDto;
import com.eps.module.api.epsone.person_type.mapper.PersonTypeMapper;
import com.eps.module.api.epsone.person_type.processor.PersonTypeBulkUploadProcessor;
import com.eps.module.api.epsone.person_type.repository.PersonTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.person.PersonDetails;
import com.eps.module.person.PersonType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonTypeServiceImpl extends BaseBulkUploadService<PersonTypeBulkUploadDto, PersonType> implements PersonTypeService {

    private final PersonTypeRepository personTypeRepository;
    private final PersonTypeMapper personTypeMapper;
    private final PersonDetailsRepository personDetailsRepository;
    private final PersonTypeBulkUploadProcessor personTypeBulkUploadProcessor;

    @Override
    @Transactional
    public PersonTypeResponseDto createPersonType(PersonTypeRequestDto requestDto) {
        // Check if person type name already exists
        if (personTypeRepository.existsByTypeNameIgnoreCase(requestDto.getTypeName())) {
            throw new IllegalArgumentException(
                ValidationUtils.formatAlreadyExistsError("Person type with name", requestDto.getTypeName())
            );
        }
        
        PersonType personType = personTypeMapper.toEntity(requestDto);
        PersonType savedPersonType = personTypeRepository.save(personType);
        return personTypeMapper.toResponseDto(savedPersonType);
    }

    @Override
    @Transactional
    public PersonTypeResponseDto updatePersonType(Long id, PersonTypeRequestDto requestDto) {
        PersonType existingPersonType = personTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format(ErrorMessages.PERSON_TYPE_NOT_FOUND, id)
            ));
        
        // Check if person type name already exists for another person type
        if (personTypeRepository.existsByTypeNameAndIdNot(requestDto.getTypeName(), id)) {
            throw new IllegalArgumentException(
                ValidationUtils.formatAlreadyExistsError("Person type with name", requestDto.getTypeName())
            );
        }
        
        personTypeMapper.updateEntityFromDto(requestDto, existingPersonType);
        PersonType updatedPersonType = personTypeRepository.save(existingPersonType);
        return personTypeMapper.toResponseDto(updatedPersonType);
    }

    @Override
    @Transactional
    public void deletePersonType(Long id) {
        PersonType personType = personTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format(ErrorMessages.PERSON_TYPE_NOT_FOUND, id)
            ));
        
        // Check if this person type is being used by any person details
        Page<PersonDetails> personDetailsPage = personDetailsRepository.findByPersonTypeId(id, PageRequest.of(0, 6));
        
        if (personDetailsPage.hasContent()) {
            List<PersonDetails> personDetailsList = personDetailsPage.getContent();
            long totalCount = personDetailsPage.getTotalElements();
            
            // Build full names for the first 5 person details
            List<String> personNames = personDetailsList.stream()
                .limit(5)
                .map(pd -> ValidationUtils.buildFullName(
                    pd.getFirstName(), 
                    pd.getMiddleName(), 
                    pd.getLastName()
                ))
                .collect(Collectors.toList());
            
            String errorMessage = ValidationUtils.formatCannotDeleteWithNamesError(
                "person type",
                personType.getTypeName(),
                totalCount,
                personNames,
                "person detail"
            );
            
            throw new IllegalStateException(errorMessage);
        }
        
        personTypeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonTypeResponseDto getPersonTypeById(Long id) {
        PersonType personType = personTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Person type not found with id: " + id));
        return personTypeMapper.toResponseDto(personType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonTypeResponseDto> getAllPersonTypes(Pageable pageable) {
        Page<PersonType> personTypes = personTypeRepository.findAll(pageable);
        return personTypes.map(personTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonTypeResponseDto> searchPersonTypes(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getAllPersonTypes(pageable);
        }
        Page<PersonType> personTypes = personTypeRepository.searchPersonTypes(search.trim(), pageable);
        return personTypes.map(personTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonTypeResponseDto> getAllPersonTypesList() {
        List<PersonType> personTypes = personTypeRepository.findAll();
        return personTypeMapper.toResponseDtoList(personTypes);
    }

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<PersonTypeBulkUploadDto, PersonType> getProcessor() {
        return personTypeBulkUploadProcessor;
    }

    @Override
    public Class<PersonTypeBulkUploadDto> getBulkUploadDtoClass() {
        return PersonTypeBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "PersonType";
    }

    @Override
    public List<PersonType> getAllEntitiesForExport() {
        return personTypeRepository.findAll();
    }

    @Override
    public Function<PersonType, PersonTypeBulkUploadDto> getEntityToDtoMapper() {
        return entity -> PersonTypeBulkUploadDto.builder()
                .typeName(entity.getTypeName())
                .description(entity.getDescription())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        PersonTypeErrorReportDto.PersonTypeErrorReportDtoBuilder builder =
                PersonTypeErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorType(error.getErrorType())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.typeName((String) error.getRowData().get("typeName"))
                    .description((String) error.getRowData().get("description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return PersonTypeErrorReportDto.class;
    }
}
