package com.eps.module.api.epsone.assettype.mapper;

import com.eps.module.api.epsone.assettype.dto.AssetTypeRequestDto;
import com.eps.module.api.epsone.assettype.dto.AssetTypeResponseDto;
import com.eps.module.asset.AssetType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetTypeMapper {
    
    AssetType toEntity(AssetTypeRequestDto requestDto);
    
    AssetTypeResponseDto toResponseDto(AssetType assetType);
    
    List<AssetTypeResponseDto> toResponseDtoList(List<AssetType> assetTypes);
    
    void updateEntityFromDto(AssetTypeRequestDto requestDto, @MappingTarget AssetType assetType);
}
