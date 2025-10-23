package com.eps.module.api.epsone.activity.mapper;

import com.eps.module.activity.Activity;
import com.eps.module.api.epsone.activity.dto.ActivityRequestDto;
import com.eps.module.api.epsone.activity.dto.ActivityResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    ActivityResponseDto toResponseDto(Activity activity);

    Activity toEntity(ActivityRequestDto activityRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ActivityRequestDto activityRequestDto, @MappingTarget Activity activity);
}
