package com.eps.module.api.epsone.activityworkremarks.mapper;

import com.eps.module.activity.ActivityWork;
import com.eps.module.activity.ActivityWorkRemarks;
import com.eps.module.api.epsone.activityworkremarks.dto.ActivityWorkRemarksRequestDto;
import com.eps.module.api.epsone.activityworkremarks.dto.ActivityWorkRemarksResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityWorkRemarksMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activityWork", source = "activityWork")
    @Mapping(target = "comment", source = "dto.comment")
    @Mapping(target = "commentedBy", source = "dto.commentedBy")
    @Mapping(target = "commentedOn", expression = "java(java.time.LocalDateTime.now())")
    ActivityWorkRemarks toEntity(ActivityWorkRemarksRequestDto dto, ActivityWork activityWork);

    @Mapping(target = "activityWorkId", source = "activityWork.id")
    @Mapping(target = "commentedByName", ignore = true) // For future user integration
    ActivityWorkRemarksResponseDto toDto(ActivityWorkRemarks entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activityWork", ignore = true)
    @Mapping(target = "commentedOn", ignore = true)
    void updateEntityFromDto(ActivityWorkRemarksRequestDto dto, @MappingTarget ActivityWorkRemarks entity);
}
