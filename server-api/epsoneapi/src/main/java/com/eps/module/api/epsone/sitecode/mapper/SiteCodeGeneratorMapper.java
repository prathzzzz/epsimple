package com.eps.module.api.epsone.sitecode.mapper;

import com.eps.module.api.epsone.sitecode.dto.GeneratedSiteCodeDto;
import com.eps.module.api.epsone.sitecode.dto.SiteCodeGeneratorRequestDto;
import com.eps.module.api.epsone.sitecode.dto.SiteCodeGeneratorResponseDto;
import com.eps.module.bank.ManagedProject;
import com.eps.module.location.State;
import com.eps.module.site.SiteCategory;
import com.eps.module.site.SiteCodeGenerator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
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
    SiteCodeGeneratorResponseDto toDto(SiteCodeGenerator entity);

    @Named("buildSiteCode")
    default String buildSiteCode(ManagedProject project, State state, Integer sequence, Integer maxSeqDigit) {
        StringBuilder code = new StringBuilder();
        
        // Add project code or abbreviation (no separator)
        if (project.getProjectCode() != null && !project.getProjectCode().isEmpty()) {
            code.append(project.getProjectCode());
        } else {
            // Use first 3 letters of project name if no code
            String projectName = project.getProjectName();
            code.append(projectName.substring(0, Math.min(3, projectName.length())).toUpperCase());
        }
        
        // Add state code (no separator, no category code)
        code.append(state.getStateCode());
        
        // Add padded sequence number (no separator)
        String sequenceStr = String.valueOf(sequence);
        int paddingNeeded = maxSeqDigit - sequenceStr.length();
        for (int i = 0; i < paddingNeeded; i++) {
            code.append("0");
        }
        code.append(sequenceStr);
        
        return code.toString();
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
