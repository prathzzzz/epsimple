package com.eps.module.api.epsone.site_code.mapper;

import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.api.epsone.site_code.dto.GeneratedSiteCodeDto;
import com.eps.module.api.epsone.site_code.dto.SiteCodeGeneratorRequestDto;
import com.eps.module.api.epsone.site_code.dto.SiteCodeGeneratorResponseDto;
import com.eps.module.bank.ManagedProject;
import com.eps.module.location.State;
import com.eps.module.site.SiteCodeGenerator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface SiteCodeGeneratorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "state", ignore = true)
    SiteCodeGenerator toEntity(SiteCodeGeneratorRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "state", ignore = true)
    void updateEntityFromDto(SiteCodeGeneratorRequestDto dto, @MappingTarget SiteCodeGenerator entity);

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "project.projectCode", target = "projectCode")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.stateName", target = "stateName")
    @Mapping(source = "state.stateCode", target = "stateCode")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    SiteCodeGeneratorResponseDto toDto(SiteCodeGenerator entity);

    @Named("buildSiteCode")
    default String buildSiteCode(ManagedProject project, State state, Integer sequence, Integer maxSeqDigit) {
        if (project == null || state == null || sequence == null || maxSeqDigit == null) {
            throw new IllegalArgumentException("Cannot build site code with null parameters");
        }
        
        StringBuilder code = new StringBuilder();
        
        // Add project code or abbreviation (no separator)
        if (project.getProjectCode() != null && !project.getProjectCode().isEmpty()) {
            code.append(project.getProjectCode().toUpperCase());
        } else if (project.getProjectName() != null && !project.getProjectName().isEmpty()) {
            // Use first 3-4 letters of project name if no code
            String projectName = project.getProjectName().replaceAll("[^A-Za-z0-9]", "");
            int length = Math.min(4, Math.max(3, projectName.length()));
            code.append(projectName.substring(0, length).toUpperCase());
        } else {
            throw new IllegalArgumentException("Project must have either projectCode or projectName");
        }
        
        // Add state code (no separator, no category code)
        if (state.getStateCode() == null || state.getStateCode().isEmpty()) {
            throw new IllegalArgumentException("State must have a stateCode");
        }
        code.append(state.getStateCode().toUpperCase());
        
        // Add padded sequence number (no separator)
        String sequenceStr = String.valueOf(sequence);
        int paddingNeeded = maxSeqDigit - sequenceStr.length();
        if (paddingNeeded < 0) {
            throw new IllegalArgumentException("Sequence " + sequence + " exceeds max digits " + maxSeqDigit);
        }
        for (int i = 0; i < paddingNeeded; i++) {
            code.append("0");
        }
        code.append(sequenceStr);
        
        String result = code.toString().toUpperCase();
        
        // Validate final code meets requirements
        if (result.length() < 5 || result.length() > 50) {
            throw new IllegalArgumentException("Generated site code '" + result + "' length (" + result.length() + 
                    ") is outside valid range (5-50 characters). ProjectCode: " + project.getProjectCode() + 
                    ", StateCode: " + state.getStateCode() + ", Sequence: " + sequence);
        }
        
        if (!result.matches("^[A-Z0-9]+$")) {
            throw new IllegalArgumentException("Generated site code '" + result + "' contains invalid characters");
        }
        
        return result;
    }

    default GeneratedSiteCodeDto toGeneratedCodeDto(SiteCodeGenerator generator, String siteCode) {
        return GeneratedSiteCodeDto.builder()
                .siteCode(siteCode)
                .projectId(generator.getProject().getId())
                .projectName(generator.getProject().getProjectName())
                .projectCode(generator.getProject().getProjectCode())
                .stateId(generator.getState().getId())
                .stateName(generator.getState().getStateName())
                .stateCode(generator.getState().getStateCode())
                .currentSequence(generator.getRunningSeq())
                .nextSequence(generator.getRunningSeq() + 1)
                .build();
    }
}
