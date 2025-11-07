package com.eps.module.api.epsone.state.service;

import com.eps.module.api.epsone.state.dto.StateBulkUploadDto;
import com.eps.module.api.epsone.state.dto.StateRequestDto;
import com.eps.module.api.epsone.state.dto.StateResponseDto;
import com.eps.module.api.epsone.state.mapper.StateMapper;
import com.eps.module.api.epsone.state.processor.StateBulkUploadProcessor;
import com.eps.module.api.epsone.state.repository.StateRepository;
import com.eps.module.api.epsone.city.repository.CityRepository;
import com.eps.module.common.bulk.excel.ExcelExportUtil;
import com.eps.module.common.bulk.excel.ExcelImportUtil;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.exception.CustomException;
import com.eps.module.location.State;
import com.eps.module.location.City;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final StateMapper stateMapper;
    private final StateBulkUploadProcessor bulkUploadProcessor;
    private final ExcelImportUtil excelImportUtil;
    private final ExcelExportUtil excelExportUtil;

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

        // Check if state is being used by any cities (fetch only first 6 for efficiency)
        Page<City> citiesPage = cityRepository.findByStateId(id, PageRequest.of(0, 6));

        if (citiesPage.hasContent()) {
            List<City> citiesList = citiesPage.getContent();
            long totalCount = citiesPage.getTotalElements();
            
            // Get first 5 city names
            String cityNames = citiesList.stream()
                    .limit(5)
                    .map(City::getCityName)
                    .collect(Collectors.joining(", "));
            
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Cannot delete '").append(state.getStateName())
                       .append("' state because it is being used by ")
                       .append(totalCount).append(totalCount > 1 ? " cities: " : " city: ")
                       .append(cityNames);
            
            if (totalCount > 5) {
                errorMessage.append(" and ").append(totalCount - 5).append(" more");
            }
            
            errorMessage.append(". Please delete or reassign these cities first.");
            
            throw new CustomException(errorMessage.toString());
        }

        stateRepository.delete(state);
        log.info("State deleted successfully with ID: {}", id);
    }
    
    @Override
    public SseEmitter bulkUpload(MultipartFile file) throws IOException {
        log.info("Starting bulk upload for states from file: {}", file.getOriginalFilename());
        
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Only .xlsx files are supported");
        }
        
        // Parse Excel file
        List<StateBulkUploadDto> uploadData = excelImportUtil.parseExcelFile(file, StateBulkUploadDto.class);
        
        if (uploadData.isEmpty()) {
            throw new IllegalArgumentException("No data found in Excel file");
        }
        
        // Create SSE emitter
        SseEmitter emitter = BulkUploadProcessor.createEmitter();
        
        // Process asynchronously using virtual threads via @Async
        bulkUploadProcessor.processBulkUpload(uploadData, emitter);
        
        return emitter;
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] exportToExcel() throws IOException {
        log.info("Exporting all states to Excel");
        
        List<State> states = stateRepository.findAll();
        
        // Convert to export DTOs
        List<StateBulkUploadDto> exportData = states.stream()
                .map(state -> StateBulkUploadDto.builder()
                        .stateName(state.getStateName())
                        .stateCode(state.getStateCode())
                        .stateCodeAlt(state.getStateCodeAlt())
                        .build())
                .collect(Collectors.toList());
        
        return excelExportUtil.exportToExcel(exportData, StateBulkUploadDto.class, "States");
    }
    
    @Override
    public byte[] downloadTemplate() throws IOException {
        log.info("Generating template for state bulk upload");
        return excelExportUtil.generateTemplate(StateBulkUploadDto.class, "States Template");
    }
}
