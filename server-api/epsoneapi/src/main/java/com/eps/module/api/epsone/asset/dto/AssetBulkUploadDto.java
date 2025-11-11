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
public class AssetBulkUploadDto {

    // Asset Basic Information
    @ExcelColumn(value = "Asset Tag ID", order = 1, required = false, example = "AST12345")
    private String assetTagId;

    @ExcelColumn(value = "Asset Name", order = 2, required = false, example = "Dell Laptop")
    private String assetName;

    @ExcelColumn(value = "Asset Type", order = 3, required = true, example = "Laptop")
    private String assetTypeName;

    @ExcelColumn(value = "Asset Category", order = 4, required = true, example = "Computer")
    private String assetCategoryName;

    @ExcelColumn(value = "Serial Number", order = 5, required = false, example = "SN123456789")
    private String serialNumber;

    @ExcelColumn(value = "Model Number", order = 6, required = false, example = "Dell Latitude 5520")
    private String modelNumber;

    // Vendor & Financial Information
    @ExcelColumn(value = "Vendor Code", order = 7, required = true, example = "VEN001")
    private String vendorCode;

    @ExcelColumn(value = "Lender Bank Name", order = 8, required = true, example = "HDFC Bank")
    private String lenderBankName;

    @ExcelColumn(value = "Purchase Order Number", order = 9, required = false, example = "PO2025001")
    private String purchaseOrderNumber;

    @ExcelColumn(value = "Purchase Order Date", order = 10, required = false, example = "2025-01-15")
    private String purchaseOrderDate;

    @ExcelColumn(value = "Purchase Order Cost", order = 11, required = false, example = "45000.00")
    private String purchaseOrderCost;

    @ExcelColumn(value = "Dispatch Order Number", order = 12, required = false, example = "DO2025001")
    private String dispatchOrderNumber;

    @ExcelColumn(value = "Dispatch Order Date", order = 13, required = false, example = "2025-01-20")
    private String dispatchOrderDate;

    // Warranty & Support Information
    @ExcelColumn(value = "Warranty Period (Months)", order = 14, required = false, example = "36")
    private String warrantyPeriod;

    @ExcelColumn(value = "Warranty Expiry Date", order = 15, required = false, example = "2028-01-15")
    private String warrantyExpiryDate;

    @ExcelColumn(value = "End Of Life Date", order = 16, required = false, example = "2030-01-15")
    private String endOfLifeDate;

    @ExcelColumn(value = "End Of Support Date", order = 17, required = false, example = "2030-01-15")
    private String endOfSupportDate;

    // Status
    @ExcelColumn(value = "Status Code", order = 18, required = false, example = "ACTIVE")
    private String statusCode;

    // Placement Information (Optional - provide Location Code for placement)
    // Location Code can be: Site Code (e.g., PROJ001MH00001), Datacenter Code (e.g., DC001), or Warehouse Code (e.g., WH001)
    @ExcelColumn(value = "Location Code", order = 19, required = false, example = "PROJ001MH00001")
    private String locationCode;

    @ExcelColumn(value = "Placement Status Code", order = 20, required = false, example = "DEPLOYED")
    private String placementStatusCode;

    @ExcelColumn(value = "Assigned On", order = 21, required = false, example = "2025-02-01")
    private String assignedOn;

    @ExcelColumn(value = "Delivered On", order = 22, required = false, example = "2025-02-05")
    private String deliveredOn;

    @ExcelColumn(value = "Deployed On", order = 23, required = false, example = "2025-02-10")
    private String deployedOn;

    @ExcelColumn(value = "Activated On", order = 24, required = false, example = "2025-02-15")
    private String activatedOn;

    @ExcelColumn(value = "Commissioned On", order = 25, required = false, example = "2025-02-15")
    private String commissionedOn;

    @ExcelColumn(value = "Decommissioned On", order = 26, required = false, example = "2029-12-31")
    private String decommissionedOn;

    @ExcelColumn(value = "Vacated On", order = 27, required = false, example = "2030-01-01")
    private String vacatedOn;

    @ExcelColumn(value = "Disposed On", order = 28, required = false, example = "2030-01-15")
    private String disposedOn;

    @ExcelColumn(value = "Scrapped On", order = 29, required = false, example = "2030-01-20")
    private String scrappedOn;

    // Export-only fields (populated from relationships - no @ExcelColumn)
    private String assetTypeCode;
    private String assetCategoryCode;
    private String vendorName;
    private String lenderBankCode;
    private String statusName;
    private String placementStatusName;
}
