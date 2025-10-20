package com.eps.module.api.epsone.persontype.service;

import com.eps.module.api.epsone.persontype.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.persontype.dto.PersonTypeResponseDto;
import com.eps.module.api.epsone.persontype.mapper.PersonTypeMapper;
import com.eps.module.api.epsone.persontype.repository.PersonTypeRepository;
import com.eps.module.person.PersonType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonTypeServiceImpl implements PersonTypeService {

    private final PersonTypeRepository personTypeRepository;
    private final PersonTypeMapper personTypeMapper;

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
        if (!personTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("Person type not found with id: " + id);
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
}
