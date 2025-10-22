package com.eps.module.api.epsone.activities.mapper;

import com.eps.module.activity.Activities;
import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activities.dto.ActivitiesRequestDto;
import com.eps.module.api.epsone.activities.dto.ActivitiesResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivitiesMapper {

    @Mapping(source = "activity.id", target = "activityId")
    ActivitiesResponseDto toResponseDto(Activities activities);

    @Mapping(target = "activity", ignore = true)
    Activities toEntity(ActivitiesRequestDto activitiesRequestDto);

    @Mapping(target = "activity", ignore = true)
    void updateEntityFromDto(ActivitiesRequestDto activitiesRequestDto, @MappingTarget Activities activities);

    default Activities toEntityWithActivity(ActivitiesRequestDto dto, Activity activity) {
        Activities activities = toEntity(dto);
        activities.setActivity(activity);
        return activities;
    }
}
