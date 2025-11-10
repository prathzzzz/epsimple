package com.eps.module.api.epsone.movement_type.service;

import com.eps.module.api.epsone.movement_type.dto.MovementTypeBulkUploadDto;
import com.eps.module.api.epsone.movement_type.dto.MovementTypeRequestDto;
import com.eps.module.api.epsone.movement_type.dto.MovementTypeResponseDto;
import com.eps.module.asset.AssetMovementType;
import com.eps.module.common.bulk.service.BulkUploadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovementTypeService extends BulkUploadService<MovementTypeBulkUploadDto, AssetMovementType> {
    
    MovementTypeResponseDto createMovementType(MovementTypeRequestDto requestDto);
    
    Page<MovementTypeResponseDto> getAllMovementTypes(Pageable pageable);
    
    Page<MovementTypeResponseDto> searchMovementTypes(String searchTerm, Pageable pageable);
    
    List<MovementTypeResponseDto> getAllMovementTypesList();
    
    MovementTypeResponseDto getMovementTypeById(Long id);
    
    MovementTypeResponseDto updateMovementType(Long id, MovementTypeRequestDto requestDto);
    
    void deleteMovementType(Long id);
}
