package com.eps.module.api.epsone.person_details.service;

import com.eps.module.api.epsone.person_details.bulk.PersonDetailsBulkUploadProcessor;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsBulkUploadDto;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsErrorReportDto;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsRequestDto;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsResponseDto;
import com.eps.module.api.epsone.person_details.mapper.PersonDetailsMapper;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.person_type.repository.PersonTypeRepository;
import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.person.PersonDetails;
import com.eps.module.person.PersonType;
import jakarta.persistence.EntityNotFoundException;
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
public class PersonDetailsServiceImpl extends BaseBulkUploadService<PersonDetailsBulkUploadDto, PersonDetails> implements PersonDetailsService {

    private final PersonDetailsRepository personDetailsRepository;
    private final PersonTypeRepository personTypeRepository;
    private final VendorRepository vendorRepository;
    private final LandlordRepository landlordRepository;
    private final PersonDetailsMapper personDetailsMapper;
    private final PersonDetailsBulkUploadProcessor personDetailsBulkUploadProcessor;

    @Override
    @Transactional
    public PersonDetailsResponseDto createPersonDetails(PersonDetailsRequestDto requestDto) {
        log.info("Creating person details for person type ID: {}", requestDto.getPersonTypeId());

        // Validate person type exists
        PersonType personType = personTypeRepository.findById(requestDto.getPersonTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Person type not found with ID: " + requestDto.getPersonTypeId()));

        PersonDetails personDetails = personDetailsMapper.toEntity(requestDto);
        personDetails.setPersonType(personType);

        PersonDetails savedPersonDetails = personDetailsRepository.save(personDetails);
        log.info("Person details created successfully with ID: {}", savedPersonDetails.getId());

        return personDetailsMapper.toResponseDto(savedPersonDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonDetailsResponseDto> getAllPersonDetails(Pageable pageable) {
        log.info("Fetching all person details with pagination");
        Page<PersonDetails> personDetails = personDetailsRepository.findAllWithPersonType(pageable);
        return personDetails.map(personDetailsMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonDetailsResponseDto> searchPersonDetails(String searchTerm, Pageable pageable) {
        log.info("Searching person details with term: {}", searchTerm);
        Page<PersonDetails> personDetails = personDetailsRepository.searchPersonDetails(searchTerm, pageable);
        return personDetails.map(personDetailsMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonDetailsResponseDto> getPersonDetailsByPersonType(Long personTypeId, Pageable pageable) {
        log.info("Fetching person details for person type ID: {}", personTypeId);
        
        // Validate person type exists
        personTypeRepository.findById(personTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Person type not found with ID: " + personTypeId));

        Page<PersonDetails> personDetails = personDetailsRepository.findByPersonTypeId(personTypeId, pageable);
        return personDetails.map(personDetailsMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDetailsResponseDto> getPersonDetailsList() {
        log.info("Fetching all person details as list");
        List<PersonDetails> personDetails = personDetailsRepository.findAllPersonDetailsList();
        return personDetails.stream()
                .map(personDetailsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDetailsResponseDto getPersonDetailsById(Long id) {
        log.info("Fetching person details with ID: {}", id);
        PersonDetails personDetails = personDetailsRepository.findByIdWithPersonType(id)
                .orElseThrow(() -> new EntityNotFoundException("Person details not found with ID: " + id));
        return personDetailsMapper.toResponseDto(personDetails);
    }

    @Override
    @Transactional
    public PersonDetailsResponseDto updatePersonDetails(Long id, PersonDetailsRequestDto requestDto) {
        log.info("Updating person details with ID: {}", id);

        PersonDetails personDetails = personDetailsRepository.findByIdWithPersonType(id)
                .orElseThrow(() -> new EntityNotFoundException("Person details not found with ID: " + id));

        // Validate person type exists
        PersonType personType = personTypeRepository.findById(requestDto.getPersonTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Person type not found with ID: " + requestDto.getPersonTypeId()));

        personDetailsMapper.updateEntityFromDto(requestDto, personDetails);
        personDetails.setPersonType(personType);

        PersonDetails updatedPersonDetails = personDetailsRepository.save(personDetails);
        log.info("Person details updated successfully with ID: {}", updatedPersonDetails.getId());

        return personDetailsMapper.toResponseDto(updatedPersonDetails);
    }

    @Override
    @Transactional
    public void deletePersonDetails(Long id) {
        log.info("Deleting person details with ID: {}", id);

        PersonDetails personDetails = personDetailsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person details not found with ID: " + id));

        // Check if person details is being used by a vendor
        if (vendorRepository.countByVendorDetailsId(id) > 0) {
            String personName = buildPersonName(personDetails);
            throw new IllegalArgumentException(
                    "Cannot delete person details for '" + personName + 
                    "' because it is being used by a vendor. Please delete the vendor first.");
        }

        // Check if person details is being used by a landlord
        if (landlordRepository.countByLandlordDetailsId(id) > 0) {
            String personName = buildPersonName(personDetails);
            throw new IllegalArgumentException(
                    "Cannot delete person details for '" + personName + 
                    "' because it is being used by a landlord. Please delete the landlord first.");
        }

        personDetailsRepository.delete(personDetails);
        log.info("Person details deleted successfully with ID: {}", id);
    }

    private String buildPersonName(PersonDetails personDetails) {
        StringBuilder name = new StringBuilder();
        if (personDetails.getFirstName() != null) {
            name.append(personDetails.getFirstName());
        }
        if (personDetails.getMiddleName() != null) {
            if (name.length() > 0) name.append(" ");
            name.append(personDetails.getMiddleName());
        }
        if (personDetails.getLastName() != null) {
            if (name.length() > 0) name.append(" ");
            name.append(personDetails.getLastName());
        }
        return name.length() > 0 ? name.toString() : "Unknown";
    }

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<PersonDetailsBulkUploadDto, PersonDetails> getProcessor() {
        return personDetailsBulkUploadProcessor;
    }

    @Override
    public Class<PersonDetailsBulkUploadDto> getBulkUploadDtoClass() {
        return PersonDetailsBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "PersonDetails";
    }

    @Override
    public List<PersonDetails> getAllEntitiesForExport() {
        return personDetailsRepository.findAllForExport();
    }

    @Override
    public Function<PersonDetails, PersonDetailsBulkUploadDto> getEntityToDtoMapper() {
        return entity -> PersonDetailsBulkUploadDto.builder()
                .personTypeName(entity.getPersonType() != null ? entity.getPersonType().getTypeName() : "")
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .lastName(entity.getLastName())
                .contactNumber(entity.getContactNumber())
                .permanentAddress(entity.getPermanentAddress())
                .correspondenceAddress(entity.getCorrespondenceAddress())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        PersonDetailsErrorReportDto.PersonDetailsErrorReportDtoBuilder builder =
                PersonDetailsErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.personTypeName((String) error.getRowData().get("personTypeName"))
                    .firstName((String) error.getRowData().get("firstName"))
                    .middleName((String) error.getRowData().get("middleName"))
                    .lastName((String) error.getRowData().get("lastName"))
                    .contactNumber((String) error.getRowData().get("contactNumber"))
                    .permanentAddress((String) error.getRowData().get("permanentAddress"))
                    .correspondenceAddress((String) error.getRowData().get("correspondenceAddress"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return PersonDetailsErrorReportDto.class;
    }
}
