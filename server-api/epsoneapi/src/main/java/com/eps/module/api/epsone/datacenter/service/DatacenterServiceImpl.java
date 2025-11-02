package com.eps.module.api.epsone.datacenter.service;

import com.eps.module.api.epsone.datacenter.dto.DatacenterRequestDto;
import com.eps.module.api.epsone.datacenter.dto.DatacenterResponseDto;
import com.eps.module.api.epsone.datacenter.mapper.DatacenterMapper;
import com.eps.module.api.epsone.datacenter.repository.DatacenterRepository;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.assetplacement.repository.AssetsOnDatacenterRepository;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.location.Location;
import com.eps.module.warehouse.Datacenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatacenterServiceImpl implements DatacenterService {

    private static final String DATACENTER_NOT_FOUND_WITH_ID_MSG = "Datacenter not found with id: ";

    private final DatacenterRepository datacenterRepository;
    private final LocationRepository locationRepository;
    private final DatacenterMapper datacenterMapper;
    private final AssetsOnDatacenterRepository assetsOnDatacenterRepository;

    @Override
    @Transactional
    public DatacenterResponseDto createDatacenter(DatacenterRequestDto requestDto) {
        log.info("Creating new datacenter: {}", requestDto.getDatacenterName());
        
        // Validate location exists
        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Location not found with id: " + requestDto.getLocationId()));

        // Validate unique datacenter code if provided
        if (requestDto.getDatacenterCode() != null && !requestDto.getDatacenterCode().isEmpty() &&
            datacenterRepository.existsByDatacenterCode(requestDto.getDatacenterCode())) {
            throw new IllegalArgumentException(
                    "Datacenter code '" + requestDto.getDatacenterCode() + "' already exists");
        }

        Datacenter datacenter = datacenterMapper.toEntity(requestDto, location);
        Datacenter savedDatacenter = datacenterRepository.save(datacenter);

        // Fetch with details for response
        Datacenter datacenterWithDetails = datacenterRepository.findByIdWithDetails(savedDatacenter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Datacenter not found after save"));

        log.info("Datacenter created successfully with ID: {}", savedDatacenter.getId());
        return datacenterMapper.toDto(datacenterWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DatacenterResponseDto> getAllDatacenters(int page, int size, String sortBy, String sortOrder) {
        log.info("Fetching all datacenters with pagination: page={}, size={}", page, size);
        Sort sort = sortOrder.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Datacenter> datacenterPage = datacenterRepository.findAllWithDetails(pageable);
        return datacenterPage.map(datacenterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DatacenterResponseDto> searchDatacenters(String searchTerm, int page, int size, String sortBy, String sortOrder) {
        log.info("Searching datacenters with keyword: {}", searchTerm);
        Sort sort = sortOrder.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Datacenter> datacenterPage = datacenterRepository.searchDatacenters(searchTerm, pageable);
        return datacenterPage.map(datacenterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DatacenterResponseDto> getAllDatacentersList() {
        log.info("Fetching all datacenters as list");
        List<Datacenter> datacenters = datacenterRepository.findAll(Sort.by("datacenterName").ascending());
        return datacenters.stream()
                .map(datacenterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DatacenterResponseDto getDatacenterById(Long id) {
        log.info("Fetching datacenter with ID: {}", id);
        Datacenter datacenter = datacenterRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(DATACENTER_NOT_FOUND_WITH_ID_MSG + id));
        return datacenterMapper.toDto(datacenter);
    }

    @Override
    @Transactional
    public DatacenterResponseDto updateDatacenter(Long id, DatacenterRequestDto requestDto) {
        log.info("Updating datacenter with ID: {}", id);
        
        Datacenter datacenter = datacenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DATACENTER_NOT_FOUND_WITH_ID_MSG + id));

        // Validate location exists
        Location location = locationRepository.findById(requestDto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Location not found with id: " + requestDto.getLocationId()));

        // Validate unique datacenter code if provided and changed
        if (requestDto.getDatacenterCode() != null && !requestDto.getDatacenterCode().isEmpty() &&
            datacenterRepository.existsByDatacenterCodeAndIdNot(requestDto.getDatacenterCode(), id)) {
            throw new IllegalArgumentException(
                    "Datacenter code '" + requestDto.getDatacenterCode() + "' already exists");
        }

        datacenterMapper.updateEntity(datacenter, requestDto, location);
        Datacenter updatedDatacenter = datacenterRepository.save(datacenter);

        // Fetch with details for response
        Datacenter datacenterWithDetails = datacenterRepository.findByIdWithDetails(updatedDatacenter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Datacenter not found after update"));

        log.info("Datacenter updated successfully with ID: {}", id);
        return datacenterMapper.toDto(datacenterWithDetails);
    }

    @Override
    @Transactional
    public void deleteDatacenter(Long id) {
        log.info("Deleting datacenter with ID: {}", id);
        
        Datacenter datacenter = datacenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DATACENTER_NOT_FOUND_WITH_ID_MSG + id));

        // Check if datacenter has assets
        long assetCount = assetsOnDatacenterRepository.countByDatacenterId(id);
        if (assetCount > 0) {
            throw new IllegalStateException(String.format(
                "Cannot delete datacenter '%s' because it has %d asset%s. Please remove the assets from this datacenter first.",
                datacenter.getDatacenterName(), assetCount, assetCount > 1 ? "s" : ""
            ));
        }

        datacenterRepository.delete(datacenter);
        log.info("Datacenter deleted successfully with ID: {}", id);
    }
}
