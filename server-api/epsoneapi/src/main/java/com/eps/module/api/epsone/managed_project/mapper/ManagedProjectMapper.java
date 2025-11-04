package com.eps.module.api.epsone.managed_project.mapper;

import com.eps.module.api.epsone.managed_project.dto.ManagedProjectRequestDto;
import com.eps.module.api.epsone.managed_project.dto.ManagedProjectResponseDto;
import com.eps.module.bank.ManagedProject;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ManagedProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bank", ignore = true)
    ManagedProject toEntity(ManagedProjectRequestDto dto);

    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "bank.bankName", target = "bankName")
    ManagedProjectResponseDto toResponseDto(ManagedProject entity);

    List<ManagedProjectResponseDto> toResponseDtoList(List<ManagedProject> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bank", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ManagedProjectRequestDto dto, @MappingTarget ManagedProject entity);
}
