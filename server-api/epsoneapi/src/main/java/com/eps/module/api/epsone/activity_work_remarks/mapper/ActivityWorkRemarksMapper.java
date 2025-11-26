package com.eps.module.api.epsone.activity_work_remarks.mapper;

import com.eps.module.activity.ActivityWork;
import com.eps.module.activity.ActivityWorkRemarks;
import com.eps.module.api.epsone.activity_work_remarks.dto.ActivityWorkRemarksRequestDto;
import com.eps.module.api.epsone.activity_work_remarks.dto.ActivityWorkRemarksResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface ActivityWorkRemarksMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activityWork", source = "activityWork")
    @Mapping(target = "comment", source = "dto.comment")
    @Mapping(target = "commentedBy", source = "dto.commentedBy")
    @Mapping(target = "commentedOn", expression = "java(java.time.LocalDateTime.now())")
    ActivityWorkRemarks toEntity(ActivityWorkRemarksRequestDto dto, ActivityWork activityWork);

    @Mapping(target = "activityWorkId", source = "activityWork.id")
    @Mapping(target = "commentedByName", ignore = true) // For future user integration
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    ActivityWorkRemarksResponseDto toDto(ActivityWorkRemarks entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activityWork", ignore = true)
    @Mapping(target = "commentedOn", ignore = true)
    void updateEntityFromDto(ActivityWorkRemarksRequestDto dto, @MappingTarget ActivityWorkRemarks entity);
}
