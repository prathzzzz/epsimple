package com.eps.module.api.epsone.persondetails.service;

import com.eps.module.api.epsone.persondetails.dto.PersonDetailsRequestDto;
import com.eps.module.api.epsone.persondetails.dto.PersonDetailsResponseDto;
import com.eps.module.api.epsone.persondetails.mapper.PersonDetailsMapper;
import com.eps.module.api.epsone.persondetails.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.persontype.repository.PersonTypeRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonDetailsServiceImpl implements PersonDetailsService {

    private final PersonDetailsRepository personDetailsRepository;
    private final PersonTypeRepository personTypeRepository;
    private final PersonDetailsMapper personDetailsMapper;

    @Override
    @Transactional
    public PersonDetailsResponseDto createPersonDetails(PersonDetailsRequestDto requestDto) {
        log.info("Creating person details with email: {}", requestDto.getEmail());

        // Validate person type exists
        PersonType personType = personTypeRepository.findById(requestDto.getPersonTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Person type not found with ID: " + requestDto.getPersonTypeId()));

        // Check for duplicate email
        if (personDetailsRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + requestDto.getEmail());
        }

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

        // Check for duplicate email if changed
        if (!requestDto.getEmail().equals(personDetails.getEmail())) {
            if (personDetailsRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
                throw new IllegalArgumentException("Email already exists: " + requestDto.getEmail());
            }
        }

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

        // TODO: Add dependency checks when other entities reference this person
        // Example: Check if person is being used by vendors, etc.

        personDetailsRepository.delete(personDetails);
        log.info("Person details deleted successfully with ID: {}", id);
    }
}
