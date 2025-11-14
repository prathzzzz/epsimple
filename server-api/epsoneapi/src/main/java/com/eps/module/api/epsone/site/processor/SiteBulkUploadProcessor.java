package com.eps.module.api.epsone.site.processor;

import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.site.dto.SiteBulkUploadDto;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.site_category.repository.SiteCategoryRepository;
import com.eps.module.api.epsone.site_code.service.SiteCodeGeneratorService;
import com.eps.module.api.epsone.site_type.repository.SiteTypeRepository;
import com.eps.module.api.epsone.site.validator.SiteBulkUploadValidator;
import com.eps.module.bank.ManagedProject;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.location.Location;
import com.eps.module.person.PersonDetails;
import com.eps.module.site.Site;
import com.eps.module.site.SiteCategory;
import com.eps.module.site.SiteType;
import com.eps.module.status.GenericStatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteBulkUploadProcessor extends BulkUploadProcessor<SiteBulkUploadDto, Site> {

    private final SiteRepository siteRepository;
    private final ManagedProjectRepository managedProjectRepository;
    private final LocationRepository locationRepository;
    private final SiteCategoryRepository siteCategoryRepository;
    private final SiteTypeRepository siteTypeRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final PersonDetailsRepository personDetailsRepository;
    private final SiteCodeGeneratorService siteCodeGeneratorService;
    private final SiteBulkUploadValidator validator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter[] ACCEPTED_DATE_FORMATTERS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    };

    @Override
    protected BulkRowValidator<SiteBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected Site convertToEntity(SiteBulkUploadDto dto) {
        log.info("=== Converting DTO to Entity - Site Code from Excel: '{}', Project: '{}', Location: '{}'", 
                dto.getSiteCode(), dto.getProjectCode(), dto.getLocationName());
        
        Site.SiteBuilder builder = Site.builder();

        // Get required relationships
        ManagedProject project = managedProjectRepository
                .findByProjectCode(dto.getProjectCode())
                .orElse(null);
        
        // Use method that eagerly fetches city and state to avoid lazy initialization issues
        Location location = locationRepository
                .findByLocationNameWithCityAndState(dto.getLocationName())
                .orElse(null);

        builder.project(project);
        builder.location(location);

        // Handle Site Code - auto-generate if not provided
        String siteCode = dto.getSiteCode();
        log.info("Processing site code - Provided: '{}', IsNull: {}, IsEmpty: {}", 
                siteCode, siteCode == null, siteCode != null && siteCode.trim().isEmpty());
        
        if (siteCode == null || siteCode.trim().isEmpty()) {
            log.info("No site code provided - will auto-generate");
            // ONLY auto-generate if no site code was provided
            // Auto-generate site code based on project and state (from location)
            if (project != null && location != null && location.getCity() != null && location.getCity().getState() != null) {
                try {
                    String generatedCode = siteCodeGeneratorService
                            .generateSiteCode(project.getId(), location.getCity().getState().getId())
                            .getSiteCode();
                    
                    // Validate generated code
                    if (generatedCode == null || generatedCode.length() < 5 || generatedCode.length() > 50) {
                        log.error("Generated site code '{}' is invalid (must be 5-50 characters). Project: {}, State: {}", 
                                generatedCode, project.getProjectCode(), location.getCity().getState().getStateCode());
                        throw new IllegalStateException("Generated site code is too short or too long");
                    } else if (!generatedCode.matches("^[A-Z0-9]+$")) {
                        log.error("Generated site code '{}' contains invalid characters (must be uppercase alphanumeric). Project: {}, State: {}", 
                                generatedCode, project.getProjectCode(), location.getCity().getState().getStateCode());
                        throw new IllegalStateException("Generated site code contains invalid characters");
                    } else {
                        builder.siteCode(generatedCode);
                        log.info("Auto-generated site code: {} for project: {}, state: {}", 
                                generatedCode, project.getProjectCode(), location.getCity().getState().getStateCode());
                    }
                } catch (Exception e) {
                    log.error("Failed to auto-generate site code for project: {}, location: {}", 
                            dto.getProjectCode(), dto.getLocationName(), e);
                    throw new IllegalStateException("Cannot auto-generate site code: " + e.getMessage(), e);
                }
            } else {
                log.error("Cannot auto-generate site code - missing project or location data. Project: {}, Location: {}, City: {}, State: {}", 
                        project != null ? project.getProjectCode() : "null",
                        location != null ? location.getLocationName() : "null",
                        (location != null && location.getCity() != null) ? location.getCity().getCityName() : "null",
                        (location != null && location.getCity() != null && location.getCity().getState() != null) ? location.getCity().getState().getStateCode() : "null");
                throw new IllegalStateException("Cannot auto-generate site code - missing required project or location data");
            }
        } else {
            // User provided a site code - use it as-is (should have been validated already)
            // If this is a duplicate, validator should have caught it and this code shouldn't run
            String trimmedCode = siteCode.trim().toUpperCase();
            
            // Double-check for duplicates (safety check - should never happen if validator works correctly)
            if (siteRepository.existsBySiteCode(trimmedCode)) {
                log.error("CRITICAL: Duplicate site code '{}' reached processor despite validation. This should not happen!", trimmedCode);
                throw new IllegalStateException("Duplicate site code: " + trimmedCode + " - This row should have been skipped by validator");
            }
            
            builder.siteCode(trimmedCode);
            log.debug("Using provided site code: {}", trimmedCode);
        }

        // Set optional relationships
        if (dto.getSiteCategoryName() != null && !dto.getSiteCategoryName().trim().isEmpty()) {
            SiteCategory category = siteCategoryRepository
                    .findByCategoryNameIgnoreCase(dto.getSiteCategoryName())
                    .orElse(null);
            builder.siteCategory(category);
        }

        if (dto.getSiteTypeName() != null && !dto.getSiteTypeName().trim().isEmpty()) {
            SiteType type = siteTypeRepository
                    .findByTypeNameIgnoreCase(dto.getSiteTypeName())
                    .orElse(null);
            builder.siteType(type);
        }

        if (dto.getSiteStatusCode() != null && !dto.getSiteStatusCode().trim().isEmpty()) {
            GenericStatusType status = genericStatusTypeRepository
                    .findByStatusCodeIgnoreCase(dto.getSiteStatusCode())
                    .orElse(null);
            builder.siteStatus(status);
        }

        // Set optional fields
        builder.projectPhase(dto.getProjectPhase());
        builder.oldSiteCode(dto.getOldSiteCode());
        builder.previousMspTermId(dto.getPreviousMspTermId());
        builder.locationClass(dto.getLocationClass());
        builder.groutingStatus(dto.getGroutingStatus());
        builder.itStabilizer(dto.getItStabilizer());
        builder.rampStatus(dto.getRampStatus());
        builder.upsBatteryBackupCapacity(dto.getUpsBatteryBackupCapacity());
        builder.connectivityType(dto.getConnectivityType());
        builder.acUnits(dto.getAcUnits());
        builder.signboardSize(dto.getSignboardSize());
        builder.brandingSize(dto.getBrandingSize());
        builder.tlsPort(dto.getTlsPort());
        builder.tlsDomainName(dto.getTlsDomainName());
        builder.ejDocket(dto.getEjDocket());
        builder.tssDocket(dto.getTssDocket());
        builder.otcActivationStatus(dto.getOtcActivationStatus());
        builder.craName(dto.getCraName());
        builder.cassetteSwapStatus(dto.getCassetteSwapStatus());
        builder.cassetteType1(dto.getCassetteType1());
        builder.cassetteType2(dto.getCassetteType2());
        builder.cassetteType3(dto.getCassetteType3());
        builder.cassetteType4(dto.getCassetteType4());

        // Parse and set dates
        builder.techLiveDate(parseDate(dto.getTechLiveDate()));
        builder.cashLiveDate(parseDate(dto.getCashLiveDate()));
        builder.siteCloseDate(parseDate(dto.getSiteCloseDate()));
        builder.possessionDate(parseDate(dto.getPossessionDate()));
        builder.actualPossessionDate(parseDate(dto.getActualPossessionDate()));
        builder.otcActivationDate(parseDate(dto.getOtcActivationDate()));

        // Parse and set decimal values
        builder.mainDoorGlassWidth(parseDecimal(dto.getMainDoorGlassWidth()));
        builder.fixedGlassWidth(parseDecimal(dto.getFixedGlassWidth()));

        // Set IP addresses
        builder.gatewayIp(dto.getGatewayIp());
        builder.atmIp(dto.getAtmIp());
        builder.subnetMask(dto.getSubnetMask());
        builder.natIp(dto.getNatIp());
        builder.switchIp(dto.getSwitchIp());

        // Set person contacts by phone number
        findPersonByContactNumber(dto.getChannelManagerContact()).ifPresent(person -> builder.channelManagerContact(person));
        findPersonByContactNumber(dto.getRegionalManagerContact()).ifPresent(person -> builder.regionalManagerContact(person));
        findPersonByContactNumber(dto.getStateHeadContact()).ifPresent(person -> builder.stateHeadContact(person));
        findPersonByContactNumber(dto.getBankPersonContact()).ifPresent(person -> builder.bankPersonContact(person));
        findPersonByContactNumber(dto.getMasterFranchiseeContact()).ifPresent(person -> builder.masterFranchiseeContact(person));

        return builder.build();
    }

    @Override
    protected void saveEntity(Site entity) {
        siteRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(SiteBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Site Code", dto.getSiteCode());
        rowData.put("Project Code", dto.getProjectCode());
        rowData.put("Project Phase", dto.getProjectPhase());
        rowData.put("Old Site Code", dto.getOldSiteCode());
        rowData.put("Previous MSP Term ID", dto.getPreviousMspTermId());
        rowData.put("Site Category", dto.getSiteCategoryName());
        rowData.put("Location Name", dto.getLocationName());
        rowData.put("Location Class", dto.getLocationClass());
        rowData.put("Site Type", dto.getSiteTypeName());
        rowData.put("Site Status", dto.getSiteStatusCode());
        rowData.put("Tech Live Date", dto.getTechLiveDate());
        rowData.put("Cash Live Date", dto.getCashLiveDate());
        rowData.put("Site Close Date", dto.getSiteCloseDate());
        rowData.put("Possession Date", dto.getPossessionDate());
        rowData.put("Actual Possession Date", dto.getActualPossessionDate());
        rowData.put("Grouting Status", dto.getGroutingStatus());
        rowData.put("IT Stabilizer", dto.getItStabilizer());
        rowData.put("Ramp Status", dto.getRampStatus());
        rowData.put("UPS Battery Backup Capacity", dto.getUpsBatteryBackupCapacity());
        rowData.put("Connectivity Type", dto.getConnectivityType());
        rowData.put("AC Units", dto.getAcUnits());
        rowData.put("Main Door Glass Width", dto.getMainDoorGlassWidth());
        rowData.put("Fixed Glass Width", dto.getFixedGlassWidth());
        rowData.put("Signboard Size", dto.getSignboardSize());
        rowData.put("Branding Size", dto.getBrandingSize());
        rowData.put("Channel Manager Contact", dto.getChannelManagerContact());
        rowData.put("Regional Manager Contact", dto.getRegionalManagerContact());
        rowData.put("State Head Contact", dto.getStateHeadContact());
        rowData.put("Bank Person Contact", dto.getBankPersonContact());
        rowData.put("Master Franchisee Contact", dto.getMasterFranchiseeContact());
        rowData.put("Gateway IP", dto.getGatewayIp());
        rowData.put("ATM IP", dto.getAtmIp());
        rowData.put("Subnet Mask", dto.getSubnetMask());
        rowData.put("NAT IP", dto.getNatIp());
        rowData.put("TLS Port", dto.getTlsPort());
        rowData.put("Switch IP", dto.getSwitchIp());
        rowData.put("TLS Domain Name", dto.getTlsDomainName());
        rowData.put("EJ Docket", dto.getEjDocket());
        rowData.put("TSS Docket", dto.getTssDocket());
        rowData.put("OTC Activation Status", dto.getOtcActivationStatus());
        rowData.put("OTC Activation Date", dto.getOtcActivationDate());
        rowData.put("CRA Name", dto.getCraName());
        rowData.put("Cassette Swap Status", dto.getCassetteSwapStatus());
        rowData.put("Cassette Type 1", dto.getCassetteType1());
        rowData.put("Cassette Type 2", dto.getCassetteType2());
        rowData.put("Cassette Type 3", dto.getCassetteType3());
        rowData.put("Cassette Type 4", dto.getCassetteType4());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(SiteBulkUploadDto dto) {
        return (dto.getSiteCode() == null || dto.getSiteCode().trim().isEmpty()) &&
               (dto.getProjectCode() == null || dto.getProjectCode().trim().isEmpty()) &&
               (dto.getLocationName() == null || dto.getLocationName().trim().isEmpty()) &&
               (dto.getSiteCategoryName() == null || dto.getSiteCategoryName().trim().isEmpty()) &&
               (dto.getSiteTypeName() == null || dto.getSiteTypeName().trim().isEmpty());
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        String s = dateStr.trim();

        // Try accepted formatters
        for (DateTimeFormatter fmt : ACCEPTED_DATE_FORMATTERS) {
            try {
                return LocalDate.parse(s, fmt);
            } catch (DateTimeParseException ignored) {
            }
        }

        // Try Excel numeric serial (e.g. 44927)
        if (s.matches("^\\d+(?:\\.\\d+)?$")) {
            try {
                double serial = Double.parseDouble(s);
                if (serial >= 1 && serial < 100000) {
                    // Excel's epoch is 1899-12-30 (due to Excel's leap year bug)
                    return LocalDate.of(1899, 12, 30).plusDays((long) Math.floor(serial));
                }
            } catch (NumberFormatException e) {
                log.warn("Failed to parse excel serial date: {}", dateStr);
            }
        }

        // If all parsing fails, return null (validation already passed)
        log.warn("Failed to parse date: {} - This should have been caught by validation", dateStr);
        return null;
    }

    private java.math.BigDecimal parseDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new java.math.BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            log.warn("Failed to parse decimal: {}", value);
            return null;
        }
    }

    private Optional<PersonDetails> findPersonByContactNumber(String contactNumber) {
        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            return Optional.empty();
        }

        String trimmed = contactNumber.trim();
        
        // Validate format (10 digits)
        if (!trimmed.matches("^[0-9]{10}$")) {
            log.warn("Invalid phone number format: '{}', skipping assignment", trimmed);
            return Optional.empty();
        }
        
        // Find person by contact number (unique)
        Optional<PersonDetails> person = personDetailsRepository.findByContactNumber(trimmed);
        
        if (person.isEmpty()) {
            log.warn("No person found with contact number '{}', skipping assignment", trimmed);
        }
        
        return person;
    }
}
