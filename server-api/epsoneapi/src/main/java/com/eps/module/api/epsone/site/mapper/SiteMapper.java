package com.eps.module.api.epsone.site.mapper;

import com.eps.module.api.epsone.site.dto.SiteRequestDto;
import com.eps.module.api.epsone.site.dto.SiteResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.person.PersonDetails;
import com.eps.module.site.Site;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface SiteMapper {

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.projectName")
    @Mapping(target = "bankName", source = "project.bank.bankName")
    @Mapping(target = "siteCategoryId", source = "siteCategory.id")
    @Mapping(target = "siteCategoryName", source = "siteCategory.categoryName")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "locationName", source = "location.locationName")
    @Mapping(target = "cityName", source = "location.city.cityName")
    @Mapping(target = "stateName", source = "location.city.state.stateName")
    @Mapping(target = "siteTypeId", source = "siteType.id")
    @Mapping(target = "siteTypeName", source = "siteType.typeName")
    @Mapping(target = "siteStatusId", source = "siteStatus.id")
    @Mapping(target = "siteStatusName", source = "siteStatus.statusName")
    @Mapping(target = "channelManagerContactId", source = "channelManagerContact.id")
    @Mapping(target = "channelManagerContactName", expression = "java(buildFullName(site.getChannelManagerContact()))")
    @Mapping(target = "regionalManagerContactId", source = "regionalManagerContact.id")
    @Mapping(target = "regionalManagerContactName", expression = "java(buildFullName(site.getRegionalManagerContact()))")
    @Mapping(target = "stateHeadContactId", source = "stateHeadContact.id")
    @Mapping(target = "stateHeadContactName", expression = "java(buildFullName(site.getStateHeadContact()))")
    @Mapping(target = "bankPersonContactId", source = "bankPersonContact.id")
    @Mapping(target = "bankPersonContactName", expression = "java(buildFullName(site.getBankPersonContact()))")
    @Mapping(target = "masterFranchiseeContactId", source = "masterFranchiseeContact.id")
    @Mapping(target = "masterFranchiseeContactName", expression = "java(buildFullName(site.getMasterFranchiseeContact()))")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    SiteResponseDto toResponseDto(Site site);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "siteCategory", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "siteType", ignore = true)
    @Mapping(target = "siteStatus", ignore = true)
    @Mapping(target = "channelManagerContact", ignore = true)
    @Mapping(target = "regionalManagerContact", ignore = true)
    @Mapping(target = "stateHeadContact", ignore = true)
    @Mapping(target = "bankPersonContact", ignore = true)
    @Mapping(target = "masterFranchiseeContact", ignore = true)
    Site toEntity(SiteRequestDto siteRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "siteCategory", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "siteType", ignore = true)
    @Mapping(target = "siteStatus", ignore = true)
    @Mapping(target = "channelManagerContact", ignore = true)
    @Mapping(target = "regionalManagerContact", ignore = true)
    @Mapping(target = "stateHeadContact", ignore = true)
    @Mapping(target = "bankPersonContact", ignore = true)
    @Mapping(target = "masterFranchiseeContact", ignore = true)
    void updateEntityFromDto(SiteRequestDto siteRequestDto, @MappingTarget Site site);

    default String buildFullName(PersonDetails personDetails) {
        if (personDetails == null) {
            return null;
        }
        StringBuilder fullName = new StringBuilder();
        if (personDetails.getFirstName() != null && !personDetails.getFirstName().isEmpty()) {
            fullName.append(personDetails.getFirstName());
        }
        if (personDetails.getMiddleName() != null && !personDetails.getMiddleName().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(personDetails.getMiddleName());
        }
        if (personDetails.getLastName() != null && !personDetails.getLastName().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(personDetails.getLastName());
        }
        return fullName.length() > 0 ? fullName.toString() : null;
    }
}
