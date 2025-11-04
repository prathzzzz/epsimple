package com.eps.module.api.epsone.asset_category.mapper;

import com.eps.module.api.epsone.asset_category.dto.AssetCategoryRequestDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryResponseDto;
import com.eps.module.asset.AssetCategory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssetCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assetType", ignore = true)
    AssetCategory toEntity(AssetCategoryRequestDto requestDto);

    @Mapping(source = "assetType.id", target = "assetTypeId")
    @Mapping(source = "assetType.typeName", target = "assetTypeName")
    AssetCategoryResponseDto toResponseDto(AssetCategory assetCategory);

    List<AssetCategoryResponseDto> toResponseDtoList(List<AssetCategory> assetCategories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assetType", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AssetCategoryRequestDto requestDto, @MappingTarget AssetCategory assetCategory);
}
