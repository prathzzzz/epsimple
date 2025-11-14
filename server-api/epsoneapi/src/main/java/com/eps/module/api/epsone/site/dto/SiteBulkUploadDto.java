package com.eps.module.api.epsone.site.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteBulkUploadDto {

    @ExcelColumn(value = "Site Code", order = 1, required = false, example = "PROJ001MH00001")
    private String siteCode;

    @ExcelColumn(value = "Project Code", order = 2, required = true, example = "PROJ001")
    private String projectCode;

    @ExcelColumn(value = "Project Phase", order = 3, required = false, example = "Phase 1")
    private String projectPhase;

    @ExcelColumn(value = "Old Site Code", order = 4, required = false, example = "OLD123")
    private String oldSiteCode;

    @ExcelColumn(value = "Previous MSP Term ID", order = 5, required = false, example = "MSP001")
    private String previousMspTermId;

    @ExcelColumn(value = "Site Category", order = 6, required = false, example = "Type A")
    private String siteCategoryName;

    @ExcelColumn(value = "Location Name", order = 7, required = true, example = "Mumbai Central")
    private String locationName;

    @ExcelColumn(value = "Location Class", order = 8, required = false, example = "Class A")
    private String locationClass;

    @ExcelColumn(value = "Site Type", order = 9, required = false, example = "Main Site")
    private String siteTypeName;

    @ExcelColumn(value = "Site Status", order = 10, required = false, example = "ACTIVE")
    private String siteStatusCode;

    @ExcelColumn(value = "Tech Live Date", order = 11, required = false, example = "2025-01-15")
    private String techLiveDate;

    @ExcelColumn(value = "Cash Live Date", order = 12, required = false, example = "2025-02-01")
    private String cashLiveDate;

    @ExcelColumn(value = "Site Close Date", order = 13, required = false, example = "2026-12-31")
    private String siteCloseDate;

    @ExcelColumn(value = "Possession Date", order = 14, required = false, example = "2024-11-01")
    private String possessionDate;

    @ExcelColumn(value = "Actual Possession Date", order = 15, required = false, example = "2024-11-05")
    private String actualPossessionDate;

    @ExcelColumn(value = "Grouting Status", order = 16, required = false, example = "Completed")
    private String groutingStatus;

    @ExcelColumn(value = "IT Stabilizer", order = 17, required = false, example = "5KVA")
    private String itStabilizer;

    @ExcelColumn(value = "Ramp Status", order = 18, required = false, example = "Available")
    private String rampStatus;

    @ExcelColumn(value = "UPS Battery Backup Capacity", order = 19, required = false, example = "2 Hours")
    private String upsBatteryBackupCapacity;

    @ExcelColumn(value = "Connectivity Type", order = 20, required = false, example = "Fiber")
    private String connectivityType;

    @ExcelColumn(value = "AC Units", order = 21, required = false, example = "2 Ton Split")
    private String acUnits;

    @ExcelColumn(value = "Main Door Glass Width", order = 22, required = false, example = "1200.50")
    private String mainDoorGlassWidth;

    @ExcelColumn(value = "Fixed Glass Width", order = 23, required = false, example = "800.00")
    private String fixedGlassWidth;

    @ExcelColumn(value = "Signboard Size", order = 24, required = false, example = "4x3 feet")
    private String signboardSize;

    @ExcelColumn(value = "Branding Size", order = 25, required = false, example = "6x4 feet")
    private String brandingSize;

    @ExcelColumn(value = "Channel Manager Contact", order = 26, required = false, example = "9876543210")
    private String channelManagerContact;

    @ExcelColumn(value = "Regional Manager Contact", order = 27, required = false, example = "9876543211")
    private String regionalManagerContact;

    @ExcelColumn(value = "State Head Contact", order = 28, required = false, example = "9876543212")
    private String stateHeadContact;

    @ExcelColumn(value = "Bank Person Contact", order = 29, required = false, example = "9876543213")
    private String bankPersonContact;

    @ExcelColumn(value = "Master Franchisee Contact", order = 30, required = false, example = "9876543214")
    private String masterFranchiseeContact;

    @ExcelColumn(value = "Gateway IP", order = 31, required = false, example = "192.168.1.1")
    private String gatewayIp;

    @ExcelColumn(value = "ATM IP", order = 32, required = false, example = "192.168.1.2")
    private String atmIp;

    @ExcelColumn(value = "Subnet Mask", order = 33, required = false, example = "255.255.255.0")
    private String subnetMask;

    @ExcelColumn(value = "NAT IP", order = 34, required = false, example = "203.0.113.1")
    private String natIp;

    @ExcelColumn(value = "TLS Port", order = 35, required = false, example = "8443")
    private String tlsPort;

    @ExcelColumn(value = "Switch IP", order = 36, required = false, example = "192.168.1.254")
    private String switchIp;

    @ExcelColumn(value = "TLS Domain Name", order = 37, required = false, example = "site.example.com")
    private String tlsDomainName;

    @ExcelColumn(value = "EJ Docket", order = 38, required = false, example = "EJ12345")
    private String ejDocket;

    @ExcelColumn(value = "TSS Docket", order = 39, required = false, example = "TSS67890")
    private String tssDocket;

    @ExcelColumn(value = "OTC Activation Status", order = 40, required = false, example = "Active")
    private String otcActivationStatus;

    @ExcelColumn(value = "OTC Activation Date", order = 41, required = false, example = "2025-03-01")
    private String otcActivationDate;

    @ExcelColumn(value = "CRA Name", order = 42, required = false, example = "CRA Company Ltd")
    private String craName;

    @ExcelColumn(value = "Cassette Swap Status", order = 43, required = false, example = "Enabled")
    private String cassetteSwapStatus;

    @ExcelColumn(value = "Cassette Type 1", order = 44, required = false, example = "500")
    private String cassetteType1;

    @ExcelColumn(value = "Cassette Type 2", order = 45, required = false, example = "200")
    private String cassetteType2;

    @ExcelColumn(value = "Cassette Type 3", order = 46, required = false, example = "100")
    private String cassetteType3;

    @ExcelColumn(value = "Cassette Type 4", order = 47, required = false, example = "50")
    private String cassetteType4;
}
