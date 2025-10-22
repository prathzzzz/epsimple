package com.eps.module.api.epsone.activity.mapper;

import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activity.dto.ActivityRequestDto;
import com.eps.module.api.epsone.activity.dto.ActivityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityResponseDto toResponseDto(Activity activity);

    Activity toEntity(ActivityRequestDto activityRequestDto);

    void updateEntityFromDto(ActivityRequestDto activityRequestDto, @MappingTarget Activity activity);
}
