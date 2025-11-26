package com.eps.module.api.epsone.cost_item.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.cost_item.dto.CostItemRequestDto;
import com.eps.module.api.epsone.cost_item.dto.CostItemResponseDto;
import com.eps.module.cost.CostItem;
import com.eps.module.cost.CostType;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface CostItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costType", source = "costType")
    @Mapping(target = "costItemFor", source = "requestDto.costItemFor")
    @Mapping(target = "itemDescription", source = "requestDto.itemDescription")
    CostItem toEntity(CostItemRequestDto requestDto, CostType costType);

    @Mapping(target = "costTypeId", source = "costType.id")
    @Mapping(target = "costTypeName", source = "costType.typeName")
    @Mapping(target = "costCategoryName", source = "costType.costCategory.categoryName")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    CostItemResponseDto toDto(CostItem costItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costType", source = "costType")
    void updateEntity(CostItemRequestDto requestDto, CostType costType, @MappingTarget CostItem costItem);
}
