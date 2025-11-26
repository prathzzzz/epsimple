package com.eps.module.api.epsone.asset_category.mapper;

import com.eps.module.api.epsone.asset_category.dto.AssetCategoryRequestDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryResponseDto;
import com.eps.module.asset.AssetCategory;
import com.eps.module.auth.audit.AuditFieldMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface AssetCategoryMapper {

    @Mapping(target = "id", ignore = true)
    AssetCategory toEntity(AssetCategoryRequestDto requestDto);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    AssetCategoryResponseDto toResponseDto(AssetCategory assetCategory);

    List<AssetCategoryResponseDto> toResponseDtoList(List<AssetCategory> assetCategories);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AssetCategoryRequestDto requestDto, @MappingTarget AssetCategory assetCategory);
}
