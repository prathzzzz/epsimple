package com.eps.module.api.epsone.movement_type.service;

import com.eps.module.api.epsone.movement_type.dto.MovementTypeRequestDto;
import com.eps.module.api.epsone.movement_type.dto.MovementTypeResponseDto;
import com.eps.module.api.epsone.movement_type.mapper.MovementTypeMapper;
import com.eps.module.api.epsone.movement_type.repository.MovementTypeRepository;
import com.eps.module.asset.AssetMovementType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementTypeServiceImpl implements MovementTypeService {
    
    private final MovementTypeRepository movementTypeRepository;
    private final MovementTypeMapper movementTypeMapper;
    
    @Override
    @Transactional
    public MovementTypeResponseDto createMovementType(MovementTypeRequestDto requestDto) {
        log.info("Creating movement type: {}", requestDto.getMovementType());
        
        // Check for duplicate
        if (movementTypeRepository.existsByMovementTypeIgnoreCase(requestDto.getMovementType())) {
            throw new IllegalArgumentException("Movement type '" + requestDto.getMovementType() + "' already exists");
        }
        
        AssetMovementType entity = movementTypeMapper.toEntity(requestDto);
        AssetMovementType saved = movementTypeRepository.save(entity);
        
        log.info("Movement type created successfully with ID: {}", saved.getId());
        return movementTypeMapper.toResponseDto(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MovementTypeResponseDto> getAllMovementTypes(Pageable pageable) {
        log.info("Fetching all movement types with pagination");
        return movementTypeRepository.findAll(pageable)
                .map(movementTypeMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MovementTypeResponseDto> searchMovementTypes(String searchTerm, Pageable pageable) {
        log.info("Searching movement types with term: {}", searchTerm);
        return movementTypeRepository.searchMovementTypes(searchTerm, pageable)
                .map(movementTypeMapper::toResponseDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MovementTypeResponseDto> getAllMovementTypesList() {
        log.info("Fetching all movement types as list");
        return movementTypeRepository.findAll().stream()
                .map(movementTypeMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public MovementTypeResponseDto getMovementTypeById(Long id) {
        log.info("Fetching movement type by ID: {}", id);
        AssetMovementType entity = movementTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movement type not found with id: " + id));
        return movementTypeMapper.toResponseDto(entity);
    }
    
    @Override
    @Transactional
    public MovementTypeResponseDto updateMovementType(Long id, MovementTypeRequestDto requestDto) {
        log.info("Updating movement type with ID: {}", id);
        
        AssetMovementType existing = movementTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movement type not found with id: " + id));
        
        // Check for duplicate (excluding current record)
        if (movementTypeRepository.existsByMovementTypeAndIdNot(requestDto.getMovementType(), id)) {
            throw new IllegalArgumentException("Movement type '" + requestDto.getMovementType() + "' already exists");
        }
        
        movementTypeMapper.updateEntityFromDto(requestDto, existing);
        AssetMovementType updated = movementTypeRepository.save(existing);
        
        log.info("Movement type updated successfully with ID: {}", id);
        return movementTypeMapper.toResponseDto(updated);
    }
    
    @Override
    @Transactional
    public void deleteMovementType(Long id) {
        log.info("Deleting movement type with ID: {}", id);
        
        if (!movementTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("Movement type not found with id: " + id);
        }
        
        movementTypeRepository.deleteById(id);
        log.info("Movement type deleted successfully with ID: {}", id);
    }
}
