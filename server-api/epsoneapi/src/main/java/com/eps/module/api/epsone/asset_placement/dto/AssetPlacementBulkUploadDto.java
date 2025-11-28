package com.eps.module.api.epsone.asset_placement.dto;

import com.eps.module.common.bulk.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Asset Placement Bulk Upload
 * Minimal fields needed to place or move assets between locations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetPlacementBulkUploadDto {

    @ExcelColumn(value = "Asset Tag ID", order = 1, required = true, example = "ATMNCRBOI00001")
    private String assetTagId;

    @ExcelColumn(value = "Location Code", order = 2, required = true, example = "SITE-001")
    private String locationCode;

    @ExcelColumn(value = "Placement Status Code", order = 3, required = true, example = "ACTIVE")
    private String placementStatusCode;

    @ExcelColumn(value = "Assigned On", order = 4, required = false, example = "2025-01-01")
    private String assignedOn;

    @ExcelColumn(value = "Delivered On", order = 5, required = false, example = "2025-01-02")
    private String deliveredOn;

    @ExcelColumn(value = "Deployed On", order = 6, required = false, example = "2025-01-03")
    private String deployedOn;

    @ExcelColumn(value = "Activated On", order = 7, required = false, example = "2025-01-04")
    private String activatedOn;

    @ExcelColumn(value = "Commissioned On", order = 8, required = false, example = "2025-01-05")
    private String commissionedOn;

    @ExcelColumn(value = "Decommissioned On", order = 9, required = false, example = "")
    private String decommissionedOn;

    @ExcelColumn(value = "Vacated On", order = 10, required = false, example = "")
    private String vacatedOn;

    @ExcelColumn(value = "Disposed On", order = 11, required = false, example = "")
    private String disposedOn;
}
