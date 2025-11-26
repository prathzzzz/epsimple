package com.eps.module.api.epsone.asset_type.mapper;

import com.eps.module.api.epsone.asset_type.dto.AssetTypeRequestDto;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeResponseDto;
import com.eps.module.asset.AssetType;
import com.eps.module.auth.audit.AuditFieldMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface AssetTypeMapper {
    
    AssetType toEntity(AssetTypeRequestDto requestDto);
    
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    AssetTypeResponseDto toResponseDto(AssetType assetType);
    
    List<AssetTypeResponseDto> toResponseDtoList(List<AssetType> assetTypes);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AssetTypeRequestDto requestDto, @MappingTarget AssetType assetType);
}
