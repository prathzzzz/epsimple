package com.eps.module.api.epsone.cost_category.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.cost_category.dto.CostCategoryRequestDto;
import com.eps.module.api.epsone.cost_category.dto.CostCategoryResponseDto;
import com.eps.module.cost.CostCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface CostCategoryMapper {
    
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    CostCategoryResponseDto toResponseDto(CostCategory entity);
    
    CostCategory toEntity(CostCategoryRequestDto requestDto);
    
    void updateEntityFromDto(CostCategoryRequestDto requestDto, @MappingTarget CostCategory entity);
}
