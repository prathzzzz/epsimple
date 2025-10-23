package com.eps.module.api.epsone.persontype.service;

import com.eps.module.api.epsone.persondetails.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.persontype.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.persontype.dto.PersonTypeResponseDto;
import com.eps.module.api.epsone.persontype.mapper.PersonTypeMapper;
import com.eps.module.api.epsone.persontype.repository.PersonTypeRepository;
import com.eps.module.person.PersonDetails;
import com.eps.module.person.PersonType;
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
public class PersonTypeServiceImpl implements PersonTypeService {

    private final PersonTypeRepository personTypeRepository;
    private final PersonTypeMapper personTypeMapper;
    private final PersonDetailsRepository personDetailsRepository;

    @Override
    @Transactional
    public PersonTypeResponseDto createPersonType(PersonTypeRequestDto requestDto) {
        // Check if person type name already exists
        if (personTypeRepository.existsByTypeNameIgnoreCase(requestDto.getTypeName())) {
            throw new IllegalArgumentException("Person type with name '" + requestDto.getTypeName() + "' already exists");
        }
        
        PersonType personType = personTypeMapper.toEntity(requestDto);
        PersonType savedPersonType = personTypeRepository.save(personType);
        return personTypeMapper.toResponseDto(savedPersonType);
    }

    @Override
    @Transactional
    public PersonTypeResponseDto updatePersonType(Long id, PersonTypeRequestDto requestDto) {
        PersonType existingPersonType = personTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Person type not found with id: " + id));
        
        // Check if person type name already exists for another person type
        if (personTypeRepository.existsByTypeNameAndIdNot(requestDto.getTypeName(), id)) {
            throw new IllegalArgumentException("Person type with name '" + requestDto.getTypeName() + "' already exists");
        }
        
        personTypeMapper.updateEntityFromDto(requestDto, existingPersonType);
        PersonType updatedPersonType = personTypeRepository.save(existingPersonType);
        return personTypeMapper.toResponseDto(updatedPersonType);
    }

    @Override
    @Transactional
    public void deletePersonType(Long id) {
        PersonType personType = personTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Person type not found with id: " + id));
        
        // Check if this person type is being used by any person details
        Page<PersonDetails> personDetailsPage = personDetailsRepository.findByPersonTypeId(id, PageRequest.of(0, 6));
        
        if (personDetailsPage.hasContent()) {
            List<PersonDetails> personDetailsList = personDetailsPage.getContent();
            long totalCount = personDetailsPage.getTotalElements();
            
            // Build full names for the first 5 person details
            List<String> personNames = personDetailsList.stream()
                .limit(5)
                .map(this::buildFullName)
                .collect(Collectors.toList());
            
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Cannot delete '").append(personType.getTypeName())
                       .append("' person type because it is being used by ")
                       .append(totalCount).append(" person detail")
                       .append(totalCount > 1 ? "s" : "").append(": ")
                       .append(String.join(", ", personNames));
            
            if (totalCount > 5) {
                errorMessage.append(" and ").append(totalCount - 5).append(" more");
            }
            
            errorMessage.append(". Please delete or reassign these person details first.");
            
            throw new IllegalStateException(errorMessage.toString());
        }
        
        personTypeRepository.deleteById(id);
    }
    
    private String buildFullName(PersonDetails personDetails) {
        StringBuilder fullName = new StringBuilder();
        
        if (personDetails.getFirstName() != null && !personDetails.getFirstName().trim().isEmpty()) {
            fullName.append(personDetails.getFirstName().trim());
        }
        
        if (personDetails.getMiddleName() != null && !personDetails.getMiddleName().trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(personDetails.getMiddleName().trim());
        }
        
        if (personDetails.getLastName() != null && !personDetails.getLastName().trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(personDetails.getLastName().trim());
        }
        
        return fullName.length() > 0 ? fullName.toString() : "Unknown";
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
}
