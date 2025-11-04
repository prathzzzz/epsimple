package com.eps.module.api.epsone.site_code.service;

import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.site_code.dto.GeneratedSiteCodeDto;
import com.eps.module.api.epsone.site_code.dto.SiteCodeGeneratorRequestDto;
import com.eps.module.api.epsone.site_code.dto.SiteCodeGeneratorResponseDto;
import com.eps.module.api.epsone.site_code.mapper.SiteCodeGeneratorMapper;
import com.eps.module.api.epsone.site_code.repository.SiteCodeGeneratorRepository;
import com.eps.module.api.epsone.state.repository.StateRepository;
import com.eps.module.bank.ManagedProject;
import com.eps.module.location.State;
import com.eps.module.site.SiteCodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteCodeGeneratorServiceImpl implements SiteCodeGeneratorService {

    private final SiteCodeGeneratorRepository siteCodeGeneratorRepository;
    private final ManagedProjectRepository managedProjectRepository;
    private final StateRepository stateRepository;
    private final SiteRepository siteRepository;
    private final SiteCodeGeneratorMapper mapper;

    @Override
    @Transactional
    public SiteCodeGeneratorResponseDto createSiteCodeGenerator(SiteCodeGeneratorRequestDto requestDto) {
        log.info("Creating new site code generator for project: {}, state: {}",
                requestDto.getProjectId(), requestDto.getStateId());

        // Check if combination already exists
        if (siteCodeGeneratorRepository.existsByProjectIdAndStateId(
                requestDto.getProjectId(), requestDto.getStateId())) {
            throw new IllegalArgumentException(
                    "Site code generator already exists for this project and state combination");
        }

        // Validate foreign keys
        ManagedProject project = managedProjectRepository.findById(requestDto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Managed project not found with id: " + requestDto.getProjectId()));

        State state = stateRepository.findById(requestDto.getStateId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "State not found with id: " + requestDto.getStateId()));

        // Create entity
        SiteCodeGenerator generator = mapper.toEntity(requestDto);
        generator.setProject(project);
        generator.setState(state);

        // Set defaults if not provided
        if (generator.getMaxSeqDigit() == null) {
            generator.setMaxSeqDigit(5);
        }
        if (generator.getRunningSeq() == null) {
            generator.setRunningSeq(1);
        }

        SiteCodeGenerator savedGenerator = siteCodeGeneratorRepository.save(generator);
        log.info("Successfully created site code generator with id: {}", savedGenerator.getId());

        return mapper.toDto(savedGenerator);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteCodeGeneratorResponseDto> getAllSiteCodeGenerators(Pageable pageable) {
        log.info("Fetching all site code generators with pagination");
        Page<SiteCodeGenerator> generators = siteCodeGeneratorRepository.findAllWithDetails(pageable);
        return generators.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteCodeGeneratorResponseDto> searchSiteCodeGenerators(String searchTerm, Pageable pageable) {
        log.info("Searching site code generators with term: {}", searchTerm);
        Page<SiteCodeGenerator> generators = siteCodeGeneratorRepository.searchSiteCodeGenerators(searchTerm, pageable);
        return generators.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteCodeGeneratorResponseDto> getListSiteCodeGenerators() {
        log.info("Fetching all site code generators as list");
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<SiteCodeGenerator> generators = siteCodeGeneratorRepository.findAll(sort);
        return generators.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SiteCodeGeneratorResponseDto getSiteCodeGeneratorById(Long id) {
        log.info("Fetching site code generator by id: {}", id);
        SiteCodeGenerator generator = siteCodeGeneratorRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Site code generator not found with id: " + id));
        return mapper.toDto(generator);
    }

    @Override
    @Transactional
    public SiteCodeGeneratorResponseDto updateSiteCodeGenerator(Long id, SiteCodeGeneratorRequestDto requestDto) {
        log.info("Updating site code generator with id: {}", id);

        SiteCodeGenerator existingGenerator = siteCodeGeneratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Site code generator not found with id: " + id));

        // Check if updating to a combination that already exists (excluding current record)
        if (!existingGenerator.getProject().getId().equals(requestDto.getProjectId()) ||
            !existingGenerator.getState().getId().equals(requestDto.getStateId())) {

            if (siteCodeGeneratorRepository.existsByProjectIdAndStateIdAndIdNot(
                    requestDto.getProjectId(), requestDto.getStateId(), id)) {
                throw new IllegalArgumentException(
                        "Site code generator already exists for this project and state combination");
            }
        }

        // Validate and update foreign keys if changed
        if (!existingGenerator.getProject().getId().equals(requestDto.getProjectId())) {
            ManagedProject project = managedProjectRepository.findById(requestDto.getProjectId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Managed project not found with id: " + requestDto.getProjectId()));
            existingGenerator.setProject(project);
        }

        if (!existingGenerator.getState().getId().equals(requestDto.getStateId())) {
            State state = stateRepository.findById(requestDto.getStateId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "State not found with id: " + requestDto.getStateId()));
            existingGenerator.setState(state);
        }

        // Update other fields
        mapper.updateEntityFromDto(requestDto, existingGenerator);

        SiteCodeGenerator updatedGenerator = siteCodeGeneratorRepository.save(existingGenerator);
        log.info("Successfully updated site code generator with id: {}", id);

        return mapper.toDto(updatedGenerator);
    }

    @Override
    @Transactional
    public void deleteSiteCodeGenerator(Long id) {
        log.info("Deleting site code generator with id: {}", id);

        if (!siteCodeGeneratorRepository.existsById(id)) {
            throw new EntityNotFoundException("Site code generator not found with id: " + id);
        }

        siteCodeGeneratorRepository.deleteById(id);
        log.info("Successfully deleted site code generator with id: {}", id);
    }

    @Override
    @Transactional
    public GeneratedSiteCodeDto generateSiteCode(Long projectId, Long stateId) {
        log.info("Generating site code for project: {}, state: {}", projectId, stateId);

        // Find or create generator with pessimistic lock for thread-safety
        SiteCodeGenerator generator = siteCodeGeneratorRepository
                .findByProjectAndStateForUpdate(projectId, stateId)
                .orElseGet(() -> createDefaultGenerator(projectId, stateId));

        // Check existing site codes to find max sequence (handles bulk uploads)
        int nextSequence = findNextAvailableSequence(projectId, stateId, generator);

        // Generate the code using the safe sequence
        String siteCode = mapper.buildSiteCode(
                generator.getProject(),
                generator.getState(),
                nextSequence,
                generator.getMaxSeqDigit()
        );

        // Update generator with new sequence
        generator.setRunningSeq(nextSequence + 1);
        siteCodeGeneratorRepository.save(generator);

        log.info("Successfully generated site code: {}", siteCode);

        GeneratedSiteCodeDto result = mapper.toGeneratedCodeDto(generator, siteCode);
        // Adjust sequences (currentSequence was used, nextSequence is now current)
        result.setCurrentSequence(nextSequence);
        result.setNextSequence(nextSequence + 1);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public GeneratedSiteCodeDto previewSiteCode(Long projectId, Long stateId) {
        log.info("Previewing site code for project: {}, state: {}", projectId, stateId);

        // Find existing generator or use default values
        SiteCodeGenerator generator = siteCodeGeneratorRepository
                .findByProjectAndState(projectId, stateId)
                .orElseGet(() -> {
                    // Create temporary generator for preview (not saved)
                    return createTemporaryGenerator(projectId, stateId);
                });

        // Generate preview code using current sequence (without incrementing)
        String siteCode = mapper.buildSiteCode(
                generator.getProject(),
                generator.getState(),
                generator.getRunningSeq(),
                generator.getMaxSeqDigit()
        );

        log.info("Successfully previewed site code: {}", siteCode);

        return mapper.toGeneratedCodeDto(generator, siteCode);
    }

    /**
     * Find the next available sequence by checking existing site codes in the database.
     * This ensures we never generate duplicate codes, even after bulk uploads.
     */
    private int findNextAvailableSequence(Long projectId, Long stateId, SiteCodeGenerator generator) {
        // Get all existing site codes for this project and state
        List<String> existingCodes = siteRepository.findSiteCodesByProjectAndState(projectId, stateId);
        
        if (existingCodes.isEmpty()) {
            // No existing codes, use generator's running sequence
            return generator.getRunningSeq();
        }

        // Extract numeric sequences from existing codes and find the maximum
        int maxSequence = 0;
        Pattern sequencePattern = Pattern.compile("\\d+$"); // Match trailing digits
        
        for (String code : existingCodes) {
            Matcher matcher = sequencePattern.matcher(code);
            if (matcher.find()) {
                try {
                    int sequence = Integer.parseInt(matcher.group());
                    maxSequence = Math.max(maxSequence, sequence);
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse sequence from site code: {}", code);
                }
            }
        }

        // Return the higher of: (max existing sequence + 1) or generator's running sequence
        int nextSequence = Math.max(maxSequence + 1, generator.getRunningSeq());
        
        if (nextSequence != generator.getRunningSeq()) {
            log.info("Adjusted sequence from {} to {} based on existing site codes", 
                    generator.getRunningSeq(), nextSequence);
        }
        
        return nextSequence;
    }

    private SiteCodeGenerator createDefaultGenerator(Long projectId, Long stateId) {
        log.info("Creating default generator for project: {}, state: {}",
                projectId, stateId);

        ManagedProject project = managedProjectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Managed project not found with id: " + projectId));

        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new EntityNotFoundException("State not found with id: " + stateId));

        SiteCodeGenerator generator = SiteCodeGenerator.builder()
                .project(project)
                .state(state)
                .maxSeqDigit(5)
                .runningSeq(1)
                .build();

        return siteCodeGeneratorRepository.save(generator);
    }

    private SiteCodeGenerator createTemporaryGenerator(Long projectId, Long stateId) {
        ManagedProject project = managedProjectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Managed project not found with id: " + projectId));

        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new EntityNotFoundException("State not found with id: " + stateId));

        return SiteCodeGenerator.builder()
                .project(project)
                .state(state)
                .maxSeqDigit(5)
                .runningSeq(1)
                .build();
    }
}
