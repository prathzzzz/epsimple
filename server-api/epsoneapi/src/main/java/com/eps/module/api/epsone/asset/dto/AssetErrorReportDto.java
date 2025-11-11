package com.eps.module.api.epsone.asset.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetErrorReportDto {

    @ExcelColumn(value = "Row Number")
    private Integer rowNumber;

    @ExcelColumn(value = "Error Type")
    private String errorType;

    @ExcelColumn(value = "Error Message")
    private String errorMessage;

    // Asset Basic Information
    @ExcelColumn(value = "Asset Tag ID")
    private String assetTagId;

    @ExcelColumn(value = "Asset Name")
    private String assetName;

    @ExcelColumn(value = "Asset Type")
    private String assetTypeName;

    @ExcelColumn(value = "Asset Type Code")
    private String assetTypeCode;

    @ExcelColumn(value = "Asset Category")
    private String assetCategoryName;

    @ExcelColumn(value = "Asset Category Code")
    private String assetCategoryCode;

    @ExcelColumn(value = "Serial Number")
    private String serialNumber;

    @ExcelColumn(value = "Model Number")
    private String modelNumber;

    // Vendor & Financial Information
    @ExcelColumn(value = "Vendor Code")
    private String vendorCode;

    @ExcelColumn(value = "Vendor Name")
    private String vendorName;

    @ExcelColumn(value = "Lender Bank Name")
    private String lenderBankName;

    @ExcelColumn(value = "Lender Bank Code")
    private String lenderBankCode;

    @ExcelColumn(value = "Purchase Order Number")
    private String purchaseOrderNumber;

    @ExcelColumn(value = "Purchase Order Date")
    private String purchaseOrderDate;

    @ExcelColumn(value = "Purchase Order Cost")
    private String purchaseOrderCost;

    @ExcelColumn(value = "Dispatch Order Number")
    private String dispatchOrderNumber;

    @ExcelColumn(value = "Dispatch Order Date")
    private String dispatchOrderDate;

    // Warranty & Support Information
    @ExcelColumn(value = "Warranty Period (Months)")
    private String warrantyPeriod;

    @ExcelColumn(value = "Warranty Expiry Date")
    private String warrantyExpiryDate;

    @ExcelColumn(value = "End Of Life Date")
    private String endOfLifeDate;

    @ExcelColumn(value = "End Of Support Date")
    private String endOfSupportDate;

    // Status
    @ExcelColumn(value = "Status Code")
    private String statusCode;

    @ExcelColumn(value = "Status Name")
    private String statusName;

    // Placement Information
    @ExcelColumn(value = "Location Code")
    private String locationCode;

    @ExcelColumn(value = "Placement Status Code")
    private String placementStatusCode;

    @ExcelColumn(value = "Placement Status Name")
    private String placementStatusName;

    @ExcelColumn(value = "Assigned On")
    private String assignedOn;

    @ExcelColumn(value = "Delivered On")
    private String deliveredOn;

    @ExcelColumn(value = "Deployed On")
    private String deployedOn;

    @ExcelColumn(value = "Activated On")
    private String activatedOn;

    @ExcelColumn(value = "Commissioned On")
    private String commissionedOn;

    @ExcelColumn(value = "Decommissioned On")
    private String decommissionedOn;

    @ExcelColumn(value = "Vacated On")
    private String vacatedOn;

    @ExcelColumn(value = "Disposed On")
    private String disposedOn;

    @ExcelColumn(value = "Scrapped On")
    private String scrappedOn;
}
