package com.eps.module.api.epsone.site.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteRequestDto {

    private Long projectId;

    @Size(max = 100, message = "Project phase cannot exceed 100 characters")
    private String projectPhase;

    @NotBlank(message = "Site code is required")
    @Size(min = 5, max = 50, message = "Site code must be between 5 and 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Site code must be uppercase alphanumeric with hyphens/underscores")
    private String siteCode;

    @Size(max = 50, message = "Old site code cannot exceed 50 characters")
    private String oldSiteCode;

    @Size(max = 50, message = "Previous MSP term ID cannot exceed 50 characters")
    private String previousMspTermId;

    private Long siteCategoryId;

    @NotNull(message = "Location is required")
    private Long locationId;

    @Size(max = 50, message = "Location class cannot exceed 50 characters")
    private String locationClass;

    private Long siteTypeId;

    private Long siteStatusId;

    private LocalDate techLiveDate;

    private LocalDate cashLiveDate;

    private LocalDate siteCloseDate;

    private LocalDate possessionDate;

    private LocalDate actualPossessionDate;

    @Size(max = 50, message = "Grouting status cannot exceed 50 characters")
    private String groutingStatus;

    @Size(max = 50, message = "IT stabilizer info cannot exceed 50 characters")
    private String itStabilizer;

    @Size(max = 50, message = "Ramp status cannot exceed 50 characters")
    private String rampStatus;

    @Size(max = 50, message = "UPS battery backup capacity cannot exceed 50 characters")
    private String upsBatteryBackupCapacity;

    @Size(max = 50, message = "Connectivity type cannot exceed 50 characters")
    private String connectivityType;

    @Size(max = 50, message = "AC units info cannot exceed 50 characters")
    private String acUnits;

    @DecimalMin(value = "0.0", message = "Main door glass width cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid width format")
    private BigDecimal mainDoorGlassWidth;

    @DecimalMin(value = "0.0", message = "Fixed glass width cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Invalid width format")
    private BigDecimal fixedGlassWidth;

    @Size(max = 50, message = "Signboard size cannot exceed 50 characters")
    private String signboardSize;

    @Size(max = 50, message = "Branding size cannot exceed 50 characters")
    private String brandingSize;

    private Long channelManagerContactId;

    private Long regionalManagerContactId;

    private Long stateHeadContactId;

    private Long bankPersonContactId;

    private Long masterFranchiseeContactId;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
             message = "Invalid IPv4 address format")
    private String gatewayIp;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
             message = "Invalid IPv4 address format")
    private String atmIp;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
             message = "Invalid IPv4 address format")
    private String subnetMask;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
             message = "Invalid IPv4 address format")
    private String natIp;

    @Size(max = 20, message = "TLS port cannot exceed 20 characters")
    private String tlsPort;

    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
             message = "Invalid IPv4 address format")
    private String switchIp;

    @Size(max = 255, message = "TLS domain name cannot exceed 255 characters")
    private String tlsDomainName;

    @Size(max = 100, message = "EJ docket cannot exceed 100 characters")
    private String ejDocket;

    @Size(max = 100, message = "TSS docket cannot exceed 100 characters")
    private String tssDocket;

    @Size(max = 50, message = "OTC activation status cannot exceed 50 characters")
    private String otcActivationStatus;

    private LocalDate otcActivationDate;

    @Size(max = 255, message = "CRA name cannot exceed 255 characters")
    private String craName;

    @Size(max = 50, message = "Cassette swap status cannot exceed 50 characters")
    private String cassetteSwapStatus;

    @Size(max = 50, message = "Cassette type 1 cannot exceed 50 characters")
    private String cassetteType1;

    @Size(max = 50, message = "Cassette type 2 cannot exceed 50 characters")
    private String cassetteType2;

    @Size(max = 50, message = "Cassette type 3 cannot exceed 50 characters")
    private String cassetteType3;

    @Size(max = 50, message = "Cassette type 4 cannot exceed 50 characters")
    private String cassetteType4;
}
