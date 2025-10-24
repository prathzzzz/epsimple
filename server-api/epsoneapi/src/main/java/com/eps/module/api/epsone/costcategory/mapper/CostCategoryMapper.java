package com.eps.module.api.epsone.costcategory.mapper;

import com.eps.module.api.epsone.costcategory.dto.CostCategoryRequestDto;
import com.eps.module.api.epsone.costcategory.dto.CostCategoryResponseDto;
import com.eps.module.cost.CostCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CostCategoryMapper {
    
    CostCategoryResponseDto toResponseDto(CostCategory entity);
    
    CostCategory toEntity(CostCategoryRequestDto requestDto);
    
    void updateEntityFromDto(CostCategoryRequestDto requestDto, @MappingTarget CostCategory entity);
}
