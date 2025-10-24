package com.eps.module.api.epsone.costitem.mapper;

import com.eps.module.api.epsone.costitem.dto.CostItemRequestDto;
import com.eps.module.api.epsone.costitem.dto.CostItemResponseDto;
import com.eps.module.cost.CostItem;
import com.eps.module.cost.CostType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CostItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costType", source = "costType")
    @Mapping(target = "costItemFor", source = "requestDto.costItemFor")
    @Mapping(target = "itemDescription", source = "requestDto.itemDescription")
    CostItem toEntity(CostItemRequestDto requestDto, CostType costType);

    @Mapping(target = "costTypeId", source = "costType.id")
    @Mapping(target = "costTypeName", source = "costType.typeName")
    @Mapping(target = "costCategoryName", source = "costType.costCategory.categoryName")
    CostItemResponseDto toDto(CostItem costItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costType", source = "costType")
    void updateEntity(CostItemRequestDto requestDto, CostType costType, @MappingTarget CostItem costItem);
}
