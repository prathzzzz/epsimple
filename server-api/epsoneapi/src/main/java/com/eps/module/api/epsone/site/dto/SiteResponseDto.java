package com.eps.module.api.epsone.site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteResponseDto {

    private Long id;

    // Project details
    private Long projectId;
    private String projectName;
    private String bankName;
    private String projectPhase;

    // Site identification
    private String siteCode;
    private String oldSiteCode;
    private String previousMspTermId;

    // Site classification
    private Long siteCategoryId;
    private String siteCategoryName;

    // Location details
    private Long locationId;
    private String locationName;
    private String cityName;
    private String stateName;
    private String locationClass;

    // Site type
    private Long siteTypeId;
    private String siteTypeName;

    // Status
    private Long siteStatusId;
    private String siteStatusName;

    // Dates
    private LocalDate techLiveDate;
    private LocalDate cashLiveDate;
    private LocalDate siteCloseDate;
    private LocalDate possessionDate;
    private LocalDate actualPossessionDate;

    // Infrastructure
    private String groutingStatus;
    private String itStabilizer;
    private String rampStatus;
    private String upsBatteryBackupCapacity;
    private String connectivityType;
    private String acUnits;

    // Physical specs
    private BigDecimal mainDoorGlassWidth;
    private BigDecimal fixedGlassWidth;
    private String signboardSize;
    private String brandingSize;

    // Contacts
    private Long channelManagerContactId;
    private String channelManagerContactName;

    private Long regionalManagerContactId;
    private String regionalManagerContactName;

    private Long stateHeadContactId;
    private String stateHeadContactName;

    private Long bankPersonContactId;
    private String bankPersonContactName;

    private Long masterFranchiseeContactId;
    private String masterFranchiseeContactName;

    // Network configuration
    private String gatewayIp;
    private String atmIp;
    private String subnetMask;
    private String natIp;
    private String tlsPort;
    private String switchIp;
    private String tlsDomainName;

    // Technical details
    private String ejDocket;
    private String tssDocket;
    private String otcActivationStatus;
    private LocalDate otcActivationDate;
    private String craName;

    // Cassette configuration
    private String cassetteSwapStatus;
    private String cassetteType1;
    private String cassetteType2;
    private String cassetteType3;
    private String cassetteType4;

    // Timestamps and audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
