package com.eps.module.api.epsone.managed_project.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectRequestDto;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectResponseDto;
import com.eps.module.bank.ManagedProject;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface ManagedProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bank", ignore = true)
    ManagedProject toEntity(ManagedProjectRequestDto dto);

    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "bank.bankName", target = "bankName")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    ManagedProjectResponseDto toResponseDto(ManagedProject entity);

    List<ManagedProjectResponseDto> toResponseDtoList(List<ManagedProject> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bank", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ManagedProjectRequestDto dto, @MappingTarget ManagedProject entity);
}
