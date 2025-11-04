package com.eps.module.api.epsone.asset_type.mapper;

import com.eps.module.api.epsone.asset_type.dto.AssetTypeRequestDto;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeResponseDto;
import com.eps.module.asset.AssetType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssetTypeMapper {
    
    AssetType toEntity(AssetTypeRequestDto requestDto);
    
    AssetTypeResponseDto toResponseDto(AssetType assetType);
    
    List<AssetTypeResponseDto> toResponseDtoList(List<AssetType> assetTypes);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AssetTypeRequestDto requestDto, @MappingTarget AssetType assetType);
}
