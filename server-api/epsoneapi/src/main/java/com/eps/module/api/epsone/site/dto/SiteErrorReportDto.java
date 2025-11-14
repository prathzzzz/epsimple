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
public class SiteErrorReportDto {

    @ExcelColumn(value = "Row Number", order = 1, required = true)
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type", order = 2, required = true)
    private String errorType;

    @ExcelColumn(value = "Error Message", order = 3, required = true)
    private String errorMessage;

    @ExcelColumn(value = "Site Code", order = 4)
    private String siteCode;

    @ExcelColumn(value = "Project Code", order = 5)
    private String projectCode;

    @ExcelColumn(value = "Project Phase", order = 6)
    private String projectPhase;

    @ExcelColumn(value = "Old Site Code", order = 7)
    private String oldSiteCode;

    @ExcelColumn(value = "Previous MSP Term ID", order = 8)
    private String previousMspTermId;

    @ExcelColumn(value = "Site Category", order = 9)
    private String siteCategoryName;

    @ExcelColumn(value = "Location Name", order = 10)
    private String locationName;

    @ExcelColumn(value = "Location Class", order = 11)
    private String locationClass;

    @ExcelColumn(value = "Site Type", order = 12)
    private String siteTypeName;

    @ExcelColumn(value = "Site Status", order = 13)
    private String siteStatusCode;

    @ExcelColumn(value = "Tech Live Date", order = 14)
    private String techLiveDate;

    @ExcelColumn(value = "Cash Live Date", order = 15)
    private String cashLiveDate;

    @ExcelColumn(value = "Site Close Date", order = 16)
    private String siteCloseDate;

    @ExcelColumn(value = "Possession Date", order = 17)
    private String possessionDate;

    @ExcelColumn(value = "Actual Possession Date", order = 18)
    private String actualPossessionDate;

    @ExcelColumn(value = "Grouting Status", order = 19)
    private String groutingStatus;

    @ExcelColumn(value = "IT Stabilizer", order = 20)
    private String itStabilizer;

    @ExcelColumn(value = "Ramp Status", order = 21)
    private String rampStatus;

    @ExcelColumn(value = "UPS Battery Backup Capacity", order = 22)
    private String upsBatteryBackupCapacity;

    @ExcelColumn(value = "Connectivity Type", order = 23)
    private String connectivityType;

    @ExcelColumn(value = "AC Units", order = 24)
    private String acUnits;

    @ExcelColumn(value = "Main Door Glass Width", order = 25)
    private String mainDoorGlassWidth;

    @ExcelColumn(value = "Fixed Glass Width", order = 26)
    private String fixedGlassWidth;

    @ExcelColumn(value = "Signboard Size", order = 27)
    private String signboardSize;

    @ExcelColumn(value = "Branding Size", order = 28)
    private String brandingSize;

    @ExcelColumn(value = "Channel Manager Contact", order = 29)
    private String channelManagerContact;

    @ExcelColumn(value = "Regional Manager Contact", order = 30)
    private String regionalManagerContact;

    @ExcelColumn(value = "State Head Contact", order = 31)
    private String stateHeadContact;

    @ExcelColumn(value = "Bank Person Contact", order = 32)
    private String bankPersonContact;

    @ExcelColumn(value = "Master Franchisee Contact", order = 33)
    private String masterFranchiseeContact;

    @ExcelColumn(value = "Gateway IP", order = 34)
    private String gatewayIp;

    @ExcelColumn(value = "ATM IP", order = 35)
    private String atmIp;

    @ExcelColumn(value = "Subnet Mask", order = 36)
    private String subnetMask;

    @ExcelColumn(value = "NAT IP", order = 37)
    private String natIp;

    @ExcelColumn(value = "TLS Port", order = 38)
    private String tlsPort;

    @ExcelColumn(value = "Switch IP", order = 39)
    private String switchIp;

    @ExcelColumn(value = "TLS Domain Name", order = 40)
    private String tlsDomainName;

    @ExcelColumn(value = "EJ Docket", order = 41)
    private String ejDocket;

    @ExcelColumn(value = "TSS Docket", order = 42)
    private String tssDocket;

    @ExcelColumn(value = "OTC Activation Status", order = 43)
    private String otcActivationStatus;

    @ExcelColumn(value = "OTC Activation Date", order = 44)
    private String otcActivationDate;

    @ExcelColumn(value = "CRA Name", order = 45)
    private String craName;

    @ExcelColumn(value = "Cassette Swap Status", order = 46)
    private String cassetteSwapStatus;

    @ExcelColumn(value = "Cassette Type 1", order = 47)
    private String cassetteType1;

    @ExcelColumn(value = "Cassette Type 2", order = 48)
    private String cassetteType2;

    @ExcelColumn(value = "Cassette Type 3", order = 49)
    private String cassetteType3;

    @ExcelColumn(value = "Cassette Type 4", order = 50)
    private String cassetteType4;
}
