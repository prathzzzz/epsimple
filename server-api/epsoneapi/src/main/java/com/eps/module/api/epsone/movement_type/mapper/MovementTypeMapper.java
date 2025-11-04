package com.eps.module.api.epsone.movement_type.mapper;

import com.eps.module.api.epsone.movement_type.dto.MovementTypeRequestDto;
import com.eps.module.api.epsone.movement_type.dto.MovementTypeResponseDto;
import com.eps.module.asset.AssetMovementType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MovementTypeMapper {
    
    MovementTypeResponseDto toResponseDto(AssetMovementType entity);
    
    AssetMovementType toEntity(MovementTypeRequestDto requestDto);
    
    void updateEntityFromDto(MovementTypeRequestDto requestDto, @MappingTarget AssetMovementType entity);
}
