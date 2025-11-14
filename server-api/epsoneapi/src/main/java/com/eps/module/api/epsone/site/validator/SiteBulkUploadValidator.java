package com.eps.module.api.epsone.site.validator;

import com.eps.module.api.epsone.generic_status_type.repository.GenericStatusTypeRepository;
import com.eps.module.api.epsone.location.repository.LocationRepository;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.site.dto.SiteBulkUploadDto;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.site_category.repository.SiteCategoryRepository;
import com.eps.module.api.epsone.site_type.repository.SiteTypeRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteBulkUploadValidator implements BulkRowValidator<SiteBulkUploadDto> {

    private final SiteRepository siteRepository;
    private final ManagedProjectRepository managedProjectRepository;
    private final LocationRepository locationRepository;
    private final SiteCategoryRepository siteCategoryRepository;
    private final SiteTypeRepository siteTypeRepository;
    private final GenericStatusTypeRepository genericStatusTypeRepository;
    private final PersonDetailsRepository personDetailsRepository;

    private static final Pattern SITE_CODE_PATTERN = Pattern.compile("^[A-Z0-9]{5,50}$");
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter[] ACCEPTED_DATE_FORMATTERS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    };

    @Override
    public List<BulkUploadErrorDto> validate(SiteBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();
        
        log.debug("=== Validating row {} - Site Code: '{}', Project: '{}', Location: '{}'", 
                rowNumber, rowData.getSiteCode(), rowData.getProjectCode(), rowData.getLocationName());

        // Validate Project Code (required)
        if (rowData.getProjectCode() == null || rowData.getProjectCode().trim().isEmpty()) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", "Project Code is required"));
        } else if (!managedProjectRepository.findByProjectCode(rowData.getProjectCode()).isPresent()) {
            errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                    "Project with code '" + rowData.getProjectCode() + "' does not exist"));
        }

        // Validate Location Name (required)
        if (rowData.getLocationName() == null || rowData.getLocationName().trim().isEmpty()) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", "Location Name is required"));
        } else if (!locationRepository.findByLocationNameIgnoreCase(rowData.getLocationName()).isPresent()) {
            errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                    "Location with name '" + rowData.getLocationName() + "' does not exist"));
        }

        // Validate Site Code (optional - will be auto-generated if not provided)
        if (rowData.getSiteCode() != null && !rowData.getSiteCode().trim().isEmpty()) {
            String siteCode = rowData.getSiteCode().trim();
            log.debug("Validating provided site code: '{}'", siteCode);
            
            if (!SITE_CODE_PATTERN.matcher(siteCode).matches()) {
                log.warn("Site code '{}' does not match pattern", siteCode);
                errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                        "Site Code must be 5-50 uppercase alphanumeric characters"));
            } else if (siteRepository.existsBySiteCode(siteCode)) {
                log.warn("Duplicate site code detected: '{}'", siteCode);
                errors.add(createError(rowNumber, "DUPLICATE_ERROR", 
                        "Site with code '" + siteCode + "' already exists"));
            }
        } else {
            log.debug("No site code provided - will be auto-generated");
        }

        // Validate Site Category (optional)
        if (rowData.getSiteCategoryName() != null && !rowData.getSiteCategoryName().trim().isEmpty()) {
            if (!siteCategoryRepository.findByCategoryNameIgnoreCase(rowData.getSiteCategoryName()).isPresent()) {
                errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                        "Site Category '" + rowData.getSiteCategoryName() + "' does not exist"));
            }
        }

        // Validate Site Type (optional)
        if (rowData.getSiteTypeName() != null && !rowData.getSiteTypeName().trim().isEmpty()) {
            if (!siteTypeRepository.findByTypeNameIgnoreCase(rowData.getSiteTypeName()).isPresent()) {
                errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                        "Site Type '" + rowData.getSiteTypeName() + "' does not exist"));
            }
        }

        // Validate Site Status (optional)
        if (rowData.getSiteStatusCode() != null && !rowData.getSiteStatusCode().trim().isEmpty()) {
            if (!genericStatusTypeRepository.findByStatusCodeIgnoreCase(rowData.getSiteStatusCode()).isPresent()) {
                errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                        "Site Status '" + rowData.getSiteStatusCode() + "' does not exist"));
            }
        }

        // Validate dates (optional)
        validateDate(rowData.getTechLiveDate(), "Tech Live Date", rowNumber, errors);
        validateDate(rowData.getCashLiveDate(), "Cash Live Date", rowNumber, errors);
        validateDate(rowData.getSiteCloseDate(), "Site Close Date", rowNumber, errors);
        validateDate(rowData.getPossessionDate(), "Possession Date", rowNumber, errors);
        validateDate(rowData.getActualPossessionDate(), "Actual Possession Date", rowNumber, errors);
        validateDate(rowData.getOtcActivationDate(), "OTC Activation Date", rowNumber, errors);

        // Validate IP addresses (optional)
        validateIp(rowData.getGatewayIp(), "Gateway IP", rowNumber, errors);
        validateIp(rowData.getAtmIp(), "ATM IP", rowNumber, errors);
        validateIp(rowData.getSubnetMask(), "Subnet Mask", rowNumber, errors);
        validateIp(rowData.getNatIp(), "NAT IP", rowNumber, errors);
        validateIp(rowData.getSwitchIp(), "Switch IP", rowNumber, errors);

        // Validate decimal values (optional)
        validateDecimal(rowData.getMainDoorGlassWidth(), "Main Door Glass Width", rowNumber, errors);
        validateDecimal(rowData.getFixedGlassWidth(), "Fixed Glass Width", rowNumber, errors);

        // Validate string lengths
        validateLength(rowData.getProjectPhase(), "Project Phase", 100, rowNumber, errors);
        validateLength(rowData.getOldSiteCode(), "Old Site Code", 50, rowNumber, errors);
        validateLength(rowData.getPreviousMspTermId(), "Previous MSP Term ID", 50, rowNumber, errors);
        validateLength(rowData.getLocationClass(), "Location Class", 50, rowNumber, errors);
        validateLength(rowData.getGroutingStatus(), "Grouting Status", 50, rowNumber, errors);
        validateLength(rowData.getItStabilizer(), "IT Stabilizer", 50, rowNumber, errors);
        validateLength(rowData.getRampStatus(), "Ramp Status", 50, rowNumber, errors);
        validateLength(rowData.getUpsBatteryBackupCapacity(), "UPS Battery Backup Capacity", 50, rowNumber, errors);
        validateLength(rowData.getConnectivityType(), "Connectivity Type", 50, rowNumber, errors);
        validateLength(rowData.getAcUnits(), "AC Units", 50, rowNumber, errors);
        validateLength(rowData.getSignboardSize(), "Signboard Size", 50, rowNumber, errors);
        validateLength(rowData.getBrandingSize(), "Branding Size", 50, rowNumber, errors);
        validateLength(rowData.getTlsPort(), "TLS Port", 20, rowNumber, errors);
        validateLength(rowData.getTlsDomainName(), "TLS Domain Name", 255, rowNumber, errors);
        validateLength(rowData.getEjDocket(), "EJ Docket", 100, rowNumber, errors);
        validateLength(rowData.getTssDocket(), "TSS Docket", 100, rowNumber, errors);
        validateLength(rowData.getOtcActivationStatus(), "OTC Activation Status", 50, rowNumber, errors);
        validateLength(rowData.getCraName(), "CRA Name", 255, rowNumber, errors);
        validateLength(rowData.getCassetteSwapStatus(), "Cassette Swap Status", 50, rowNumber, errors);
        validateLength(rowData.getCassetteType1(), "Cassette Type 1", 50, rowNumber, errors);
        validateLength(rowData.getCassetteType2(), "Cassette Type 2", 50, rowNumber, errors);
        validateLength(rowData.getCassetteType3(), "Cassette Type 3", 50, rowNumber, errors);
        validateLength(rowData.getCassetteType4(), "Cassette Type 4", 50, rowNumber, errors);

        // Validate person contacts by phone number (optional)
        validatePersonContact(rowData.getChannelManagerContact(), "Channel Manager Contact", rowNumber, errors);
        validatePersonContact(rowData.getRegionalManagerContact(), "Regional Manager Contact", rowNumber, errors);
        validatePersonContact(rowData.getStateHeadContact(), "State Head Contact", rowNumber, errors);
        validatePersonContact(rowData.getBankPersonContact(), "Bank Person Contact", rowNumber, errors);
        validatePersonContact(rowData.getMasterFranchiseeContact(), "Master Franchisee Contact", rowNumber, errors);

        return errors;
    }

    @Override
    public boolean isDuplicate(SiteBulkUploadDto rowData) {
        // Check if site code already exists (if provided)
        if (rowData.getSiteCode() != null && !rowData.getSiteCode().trim().isEmpty()) {
            return siteRepository.existsBySiteCode(rowData.getSiteCode().trim());
        }
        // If no site code provided, it's not a duplicate (will be auto-generated)
        return false;
    }

    private void validateDate(String dateStr, String fieldName, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            String s = dateStr.trim();

            // Try the accepted formatters
            for (DateTimeFormatter fmt : ACCEPTED_DATE_FORMATTERS) {
                try {
                    LocalDate.parse(s, fmt);
                    return; // valid
                } catch (DateTimeParseException ignored) {
                }
            }

            // Accept Excel numeric date serials (e.g. 44927)
            if (s.matches("^\\d+(?:\\.\\d+)?$")) {
                try {
                    double serial = Double.parseDouble(s);
                    if (serial >= 1 && serial < 100000) {
                        return; // valid Excel serial number
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            errors.add(createError(rowNumber, "VALIDATION_ERROR",
                    fieldName + " must be in YYYY-MM-DD format or a valid Excel date/serial (e.g. 44927) or common formats like dd/MM/yyyy"));
        }
    }

    private void validateIp(String ip, String fieldName, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (ip != null && !ip.trim().isEmpty()) {
            if (!IP_PATTERN.matcher(ip.trim()).matches()) {
                errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                        fieldName + " must be a valid IPv4 address"));
            }
        }
    }

    private void validateDecimal(String value, String fieldName, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                java.math.BigDecimal decimal = new java.math.BigDecimal(value.trim());
                if (decimal.compareTo(java.math.BigDecimal.ZERO) < 0) {
                    errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                            fieldName + " cannot be negative"));
                }
            } catch (NumberFormatException e) {
                errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                        fieldName + " must be a valid decimal number"));
            }
        }
    }

    private void validateLength(String value, String fieldName, int maxLength, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (value != null && value.length() > maxLength) {
            errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                    fieldName + " cannot exceed " + maxLength + " characters"));
        }
    }

    private void validatePersonContact(String contactNumber, String fieldName, int rowNumber, List<BulkUploadErrorDto> errors) {
        if (contactNumber != null && !contactNumber.trim().isEmpty()) {
            String trimmed = contactNumber.trim();
            
            // Validate phone number format (10 digits)
            if (!trimmed.matches("^[0-9]{10}$")) {
                errors.add(createError(rowNumber, "VALIDATION_ERROR", 
                        fieldName + " must be a 10-digit phone number"));
                return;
            }
            
            // Check if person exists with this contact number
            if (!personDetailsRepository.findByContactNumber(trimmed).isPresent()) {
                errors.add(createError(rowNumber, "REFERENCE_ERROR", 
                        fieldName + " '" + trimmed + "' not found in person details. Please ensure the person is registered first."));
            }
        }
    }

    private BulkUploadErrorDto createError(int rowNumber, String errorType, String errorMessage) {
        return BulkUploadErrorDto.builder()
                .rowNumber(rowNumber)
                .errorType(errorType)
                .errorMessage(errorMessage)
                .build();
    }
}
