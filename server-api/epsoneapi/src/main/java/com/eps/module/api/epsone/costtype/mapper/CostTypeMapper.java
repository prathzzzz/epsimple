package com.eps.module.api.epsone.costtype.mapper;

import com.eps.module.api.epsone.costtype.dto.CostTypeRequestDto;
import com.eps.module.api.epsone.costtype.dto.CostTypeResponseDto;
import com.eps.module.cost.CostType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CostTypeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costCategory", ignore = true)
    CostType toEntity(CostTypeRequestDto dto);

    @Mapping(source = "costCategory.id", target = "costCategoryId")
    @Mapping(source = "costCategory.categoryName", target = "costCategoryName")
    CostTypeResponseDto toResponseDto(CostType entity);

    List<CostTypeResponseDto> toResponseDtoList(List<CostType> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "costCategory", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CostTypeRequestDto dto, @MappingTarget CostType entity);
}
