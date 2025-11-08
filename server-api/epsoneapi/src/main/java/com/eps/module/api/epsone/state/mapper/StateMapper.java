package com.eps.module.api.epsone.state.mapper;

import com.eps.module.api.epsone.state.dto.StateRequestDto;
import com.eps.module.api.epsone.state.dto.StateResponseDto;
import com.eps.module.location.State;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StateMapper {

    StateResponseDto toResponseDto(State state);

    State toEntity(StateRequestDto stateRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(StateRequestDto stateRequestDto, @MappingTarget State state);
    
    /**
     * Hook to process state before mapping
     */
    @AfterMapping
    default void processState(@MappingTarget State state, StateRequestDto dto) {
        if (dto.getStateName() != null) {
            state.setStateName(capitalizeFirstLetter(dto.getStateName()));
        }
        if (dto.getStateCode() != null) {
            state.setStateCode(transformToUpperCase(dto.getStateCode()));
        }
        if (dto.getStateCodeAlt() != null) {
            state.setStateCodeAlt(transformToUpperCase(dto.getStateCodeAlt()));
        }
    }
    
    /**
     * Capitalizes the first letter of each word in the input string.
     * Example: "maharashtra" -> "Maharashtra"
     * Example: "new delhi" -> "New Delhi"
     * Example: "uttar pradesh" -> "Uttar Pradesh"
     * This method is package-private to avoid MapStruct confusion
     */
    @Named("capitalizeFirstLetter")
    static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        String trimmed = input.trim();
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
    
    /**
     * Transforms string to uppercase and trims whitespace.
     * Example: "mh  " -> "MH"
     * This method is package-private to avoid MapStruct confusion
     */
    @Named("transformToUpperCase")
    static String transformToUpperCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.trim().toUpperCase();
    }
}
