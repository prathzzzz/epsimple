package com.eps.module.api.epsone.activities.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.activity.Activities;
import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activities.dto.ActivitiesRequestDto;
import com.eps.module.api.epsone.activities.dto.ActivitiesResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface ActivitiesMapper {

    @Mapping(source = "activity.id", target = "activityId")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    ActivitiesResponseDto toResponseDto(Activities activities);

    @Mapping(target = "activity", ignore = true)
    Activities toEntity(ActivitiesRequestDto activitiesRequestDto);

    @Mapping(target = "activity", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ActivitiesRequestDto activitiesRequestDto, @MappingTarget Activities activities);

    default Activities toEntityWithActivity(ActivitiesRequestDto dto, Activity activity) {
        Activities activities = toEntity(dto);
        activities.setActivity(activity);
        return activities;
    }
}
