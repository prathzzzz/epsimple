package com.eps.module.api.epsone.state.service;

import com.eps.module.api.epsone.state.dto.StateRequestDto;
import com.eps.module.api.epsone.state.dto.StateResponseDto;
import com.eps.module.api.epsone.state.mapper.StateMapper;
import com.eps.module.api.epsone.state.repository.StateRepository;
import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.common.exception.CustomException;
import com.eps.module.location.State;
import com.eps.module.location.City;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final StateMapper stateMapper;

    @Override
    @Transactional
    public StateResponseDto createState(StateRequestDto stateRequestDto) {
        log.info("Creating new state: {}", stateRequestDto.getStateName());

        // Check if state name already exists
        if (stateRepository.existsByStateName(stateRequestDto.getStateName())) {
            throw new CustomException("State with name '" + stateRequestDto.getStateName() + "' already exists");
        }

        // Check if state code already exists
        if (stateRepository.existsByStateCode(stateRequestDto.getStateCode())) {
            throw new CustomException("State with code '" + stateRequestDto.getStateCode() + "' already exists");
        }

        State state = stateMapper.toEntity(stateRequestDto);
        State savedState = stateRepository.save(state);

        log.info("State created successfully with ID: {}", savedState.getId());
        return stateMapper.toResponseDto(savedState);
    }

    @Override
    @Transactional(readOnly = true)
    public StateResponseDto getStateById(Long id) {
        log.info("Fetching state with ID: {}", id);
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new CustomException("State not found with ID: " + id));
        return stateMapper.toResponseDto(state);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StateResponseDto> getAllStates(Pageable pageable) {
        log.info("Fetching all states with pagination");
        Page<State> states = stateRepository.findAll(pageable);
        return states.map(stateMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StateResponseDto> searchStates(String search, Pageable pageable) {
        log.info("Searching states with keyword: {}", search);
        Page<State> states = stateRepository.searchStates(search, pageable);
        return states.map(stateMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDto> getAllStatesList() {
        log.info("Fetching all states as list");
        List<State> states = stateRepository.findAll();
        return states.stream()
                .map(stateMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StateResponseDto updateState(Long id, StateRequestDto stateRequestDto) {
        log.info("Updating state with ID: {}", id);

        State existingState = stateRepository.findById(id)
                .orElseThrow(() -> new CustomException("State not found with ID: " + id));

        // Check if state name is being changed and if it already exists
        if (!existingState.getStateName().equals(stateRequestDto.getStateName())) {
            if (stateRepository.existsByStateName(stateRequestDto.getStateName())) {
                throw new CustomException("State with name '" + stateRequestDto.getStateName() + "' already exists");
            }
        }

        // Check if state code is being changed and if it already exists
        if (!existingState.getStateCode().equals(stateRequestDto.getStateCode())) {
            if (stateRepository.existsByStateCode(stateRequestDto.getStateCode())) {
                throw new CustomException("State with code '" + stateRequestDto.getStateCode() + "' already exists");
            }
        }

        stateMapper.updateEntityFromDto(stateRequestDto, existingState);
        State updatedState = stateRepository.save(existingState);

        log.info("State updated successfully with ID: {}", updatedState.getId());
        return stateMapper.toResponseDto(updatedState);
    }

    @Override
    @Transactional
    public void deleteState(Long id) {
        log.info("Deleting state with ID: {}", id);

        State state = stateRepository.findById(id)
                .orElseThrow(() -> new CustomException("State not found with ID: " + id));

        // Check if state is being used by any cities
        List<City> citiesUsingState = cityRepository.findAll().stream()
                .filter(city -> city.getState() != null && city.getState().getId().equals(id))
                .collect(Collectors.toList());

        if (!citiesUsingState.isEmpty()) {
            String cityNames = citiesUsingState.stream()
                    .limit(5)  // Show maximum 5 cities
                    .map(City::getCityName)
                    .collect(Collectors.joining(", "));
            
            String errorMessage = String.format(
                "Cannot delete '%s' state because it is being used by %d city/cities: %s%s. Please delete or reassign these cities first.",
                state.getStateName(),
                citiesUsingState.size(),
                cityNames,
                citiesUsingState.size() > 5 ? " and " + (citiesUsingState.size() - 5) + " more" : ""
            );
            
            throw new CustomException(errorMessage);
        }

        stateRepository.delete(state);
        log.info("State deleted successfully with ID: {}", id);
    }
}
