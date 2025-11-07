package com.eps.module.api.epsone.state.service;

import com.eps.module.api.epsone.state.dto.StateRequestDto;
import com.eps.module.api.epsone.state.dto.StateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

public interface StateService {

    StateResponseDto createState(StateRequestDto stateRequestDto);

    StateResponseDto getStateById(Long id);

    Page<StateResponseDto> getAllStates(Pageable pageable);

    Page<StateResponseDto> searchStates(String search, Pageable pageable);

    List<StateResponseDto> getAllStatesList();

    StateResponseDto updateState(Long id, StateRequestDto stateRequestDto);

    void deleteState(Long id);
    
    // Bulk operations
    SseEmitter bulkUpload(MultipartFile file) throws IOException;
    
    byte[] exportToExcel() throws IOException;
    
    byte[] downloadTemplate() throws IOException;
}
