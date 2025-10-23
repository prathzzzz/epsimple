package com.eps.module.api.epsone.genericstatustype.service;

import com.eps.module.api.epsone.genericstatustype.dto.GenericStatusTypeRequestDto;
import com.eps.module.api.epsone.genericstatustype.dto.GenericStatusTypeResponseDto;
import com.eps.module.api.epsone.genericstatustype.mapper.GenericStatusTypeMapper;
import com.eps.module.api.epsone.genericstatustype.repository.GenericStatusTypeRepository;
import com.eps.module.status.GenericStatusType;
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
public class GenericStatusTypeServiceImpl implements GenericStatusTypeService {

    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final GenericStatusTypeMapper genericStatusTypeMapper;

    @Override
    @Transactional
    public GenericStatusTypeResponseDto createGenericStatusType(GenericStatusTypeRequestDto requestDto) {
        log.info("Creating new generic status type: {}", requestDto.getStatusName());

        if (genericStatusTypeRepository.existsByStatusNameIgnoreCase(requestDto.getStatusName())) {
            throw new IllegalArgumentException("Status type with name '" + requestDto.getStatusName() + "' already exists");
        }

        if (requestDto.getStatusCode() != null && !requestDto.getStatusCode().isEmpty() &&
            genericStatusTypeRepository.existsByStatusCodeIgnoreCase(requestDto.getStatusCode())) {
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

        // TODO: Add dependency checks when other entities reference this status type
        // Example: Check if status type is being used by sites, assets, etc.

        genericStatusTypeRepository.deleteById(id);
        log.info("Generic status type deleted successfully with ID: {}", id);
    }
}
