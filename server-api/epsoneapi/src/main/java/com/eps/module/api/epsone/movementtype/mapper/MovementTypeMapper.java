package com.eps.module.api.epsone.movementtype.mapper;

import com.eps.module.api.epsone.movementtype.dto.MovementTypeRequestDto;
import com.eps.module.api.epsone.movementtype.dto.MovementTypeResponseDto;
import com.eps.module.asset.AssetMovementType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MovementTypeMapper {
    
    MovementTypeResponseDto toResponseDto(AssetMovementType entity);
    
    AssetMovementType toEntity(MovementTypeRequestDto requestDto);
    
    void updateEntityFromDto(MovementTypeRequestDto requestDto, @MappingTarget AssetMovementType entity);
}
