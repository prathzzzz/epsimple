package com.eps.module.api.epsone.city.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.city.dto.CityBulkUploadDto;
import com.eps.module.api.epsone.city.dto.CityRequestDto;
import com.eps.module.api.epsone.city.dto.CityResponseDto;
import com.eps.module.location.City;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface CityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    City toEntity(CityRequestDto requestDto);
    
    /**
     * Hook to process city data before mapping from request DTO
     */
    @AfterMapping
    default void processCityFromRequest(@MappingTarget City city, CityRequestDto dto) {
        if (dto.getCityName() != null) {
            city.setCityName(capitalizeFirstLetter(dto.getCityName()));
        }
        if (dto.getCityCode() != null && !dto.getCityCode().trim().isEmpty()) {
            city.setCityCode(transformToUpperCase(dto.getCityCode()));
        }
    }

    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.stateName", target = "stateName")
    @Mapping(source = "state.stateCode", target = "stateCode")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    CityResponseDto toResponseDto(City city);

    List<CityResponseDto> toResponseDtoList(List<City> cities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CityRequestDto requestDto, @MappingTarget City city);

    // Bulk upload mapping
    @Mapping(source = "state.stateCode", target = "stateCode")
    @AfterMapping
    default void processCity(@MappingTarget CityBulkUploadDto dto, City city) {
        if (city.getCityName() != null) {
            dto.setCityName(capitalizeFirstLetter(city.getCityName()));
        }
        if (city.getCityCode() != null) {
            dto.setCityCode(transformToUpperCase(city.getCityCode()));
        }
        if (city.getState() != null && city.getState().getStateCode() != null) {
            dto.setStateCode(transformToUpperCase(city.getState().getStateCode()));
        }
    }

    CityBulkUploadDto toBulkUploadDto(City city);

    @Named("capitalizeFirstLetter")
    static String capitalizeFirstLetter(String value) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }
        
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        
        // Split by spaces and capitalize each word
        String[] words = trimmed.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            String word = words[i];
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase())
                      .append(word.substring(1).toLowerCase());
            }
        }
        
        return result.toString();
    }

    @Named("transformToUpperCase")
    static String transformToUpperCase(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toUpperCase();
    }
}

