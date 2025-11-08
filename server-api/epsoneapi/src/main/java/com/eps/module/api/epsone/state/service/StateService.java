package com.eps.module.api.epsone.state.service;

import com.eps.module.api.epsone.state.dto.StateBulkUploadDto;
import com.eps.module.api.epsone.state.dto.StateRequestDto;
import com.eps.module.api.epsone.state.dto.StateResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.location.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StateService extends BulkUploadService<StateBulkUploadDto, State> {

    StateResponseDto createState(StateRequestDto stateRequestDto);

    StateResponseDto getStateById(Long id);

    Page<StateResponseDto> getAllStates(Pageable pageable);

    Page<StateResponseDto> searchStates(String search, Pageable pageable);

    List<StateResponseDto> getAllStatesList();

    StateResponseDto updateState(Long id, StateRequestDto stateRequestDto);

    void deleteState(Long id);
}
