package com.eps.module.api.epsone.asset_movement.mapper;

import com.eps.module.api.epsone.asset_movement.dto.AssetCurrentLocationDto;
import com.eps.module.api.epsone.asset_movement.dto.AssetMovementHistoryDto;
import com.eps.module.asset.*;
import org.springframework.stereotype.Component;

@Component
public class AssetMovementMapper {

    public AssetMovementHistoryDto toHistoryDto(AssetMovementTracker tracker) {
        AssetMovementHistoryDto.AssetMovementHistoryDtoBuilder builder = AssetMovementHistoryDto.builder()
                .id(tracker.getId())
                .assetId(tracker.getAsset().getId())
                .assetTagId(tracker.getAsset().getAssetTagId())
                .assetName(tracker.getAsset().getAssetName())
                .movementTypeId(tracker.getAssetMovementType().getId())
                .movementType(tracker.getAssetMovementType().getMovementType())
                .movementDescription(tracker.getAssetMovementType().getDescription())
                .createdAt(tracker.getCreatedAt())
                .movedAt(tracker.getCreatedAt());

        // From location
        if (tracker.getFromFactory() != null) {
            builder.fromFactory(tracker.getFromFactory());
        }
        if (tracker.getFromSite() != null) {
            builder.fromSiteId(tracker.getFromSite().getId())
                    .fromSiteCode(tracker.getFromSite().getSiteCode())
                    .fromSiteName(tracker.getFromSite().getSiteCode()); // Site uses code as name
        }
        if (tracker.getFromWarehouse() != null) {
            builder.fromWarehouseId(tracker.getFromWarehouse().getId())
                    .fromWarehouseCode(tracker.getFromWarehouse().getWarehouseCode())
                    .fromWarehouseName(tracker.getFromWarehouse().getWarehouseName());
        }
        if (tracker.getFromDatacenter() != null) {
            builder.fromDatacenterId(tracker.getFromDatacenter().getId())
                    .fromDatacenterCode(tracker.getFromDatacenter().getDatacenterCode())
                    .fromDatacenterName(tracker.getFromDatacenter().getDatacenterName());
        }

        // To location
        if (tracker.getToSite() != null) {
            builder.toSiteId(tracker.getToSite().getId())
                    .toSiteCode(tracker.getToSite().getSiteCode())
                    .toSiteName(tracker.getToSite().getSiteCode()); // Site uses code as name
        }
        if (tracker.getToWarehouse() != null) {
            builder.toWarehouseId(tracker.getToWarehouse().getId())
                    .toWarehouseCode(tracker.getToWarehouse().getWarehouseCode())
                    .toWarehouseName(tracker.getToWarehouse().getWarehouseName());
        }
        if (tracker.getToDatacenter() != null) {
            builder.toDatacenterId(tracker.getToDatacenter().getId())
                    .toDatacenterCode(tracker.getToDatacenter().getDatacenterCode())
                    .toDatacenterName(tracker.getToDatacenter().getDatacenterName());
        }

        return builder.build();
    }

    public AssetCurrentLocationDto toCurrentLocationDto(Asset asset, Object placement, String locationType) {
        AssetCurrentLocationDto.AssetCurrentLocationDtoBuilder builder = AssetCurrentLocationDto.builder()
                .assetId(asset.getId())
                .assetTagId(asset.getAssetTagId())
                .assetName(asset.getAssetName())
                .isPlaced(true)
                .locationType(locationType);

        if (placement instanceof AssetsOnSite) {
            AssetsOnSite siteP = (AssetsOnSite) placement;
            builder.placementId(siteP.getId())
                    .locationId(siteP.getSite().getId())
                    .locationCode(siteP.getSite().getSiteCode())
                    .locationName(siteP.getSite().getSiteCode()) // Site uses code as name
                    .assignedOn(siteP.getAssignedOn())
                    .deliveredOn(siteP.getDeliveredOn())
                    .assetStatusName(siteP.getAssetStatus() != null ? siteP.getAssetStatus().getStatusName() : null)
                    .activityWorkId(siteP.getActivityWork() != null ? siteP.getActivityWork().getId() : null)
                    .activityWorkNumber(siteP.getActivityWork() != null ? siteP.getActivityWork().getVendorOrderNumber() : null);
        } else if (placement instanceof AssetsOnWarehouse) {
            AssetsOnWarehouse warehouseP = (AssetsOnWarehouse) placement;
            builder.placementId(warehouseP.getId())
                    .locationId(warehouseP.getWarehouse().getId())
                    .locationCode(warehouseP.getWarehouse().getWarehouseCode())
                    .locationName(warehouseP.getWarehouse().getWarehouseName())
                    .assignedOn(warehouseP.getAssignedOn())
                    .deliveredOn(warehouseP.getDeliveredOn())
                    .assetStatusName(warehouseP.getAssetStatus() != null ? warehouseP.getAssetStatus().getStatusName() : null)
                    .activityWorkId(warehouseP.getActivityWork() != null ? warehouseP.getActivityWork().getId() : null)
                    .activityWorkNumber(warehouseP.getActivityWork() != null ? warehouseP.getActivityWork().getVendorOrderNumber() : null);
        } else if (placement instanceof AssetsOnDatacenter) {
            AssetsOnDatacenter datacenterP = (AssetsOnDatacenter) placement;
            builder.placementId(datacenterP.getId())
                    .locationId(datacenterP.getDatacenter().getId())
                    .locationCode(datacenterP.getDatacenter().getDatacenterCode())
                    .locationName(datacenterP.getDatacenter().getDatacenterName())
                    .assignedOn(datacenterP.getAssignedOn())
                    .deliveredOn(datacenterP.getDeliveredOn())
                    .assetStatusName(datacenterP.getAssetStatus() != null ? datacenterP.getAssetStatus().getStatusName() : null)
                    .activityWorkId(datacenterP.getActivityWork() != null ? datacenterP.getActivityWork().getId() : null)
                    .activityWorkNumber(datacenterP.getActivityWork() != null ? datacenterP.getActivityWork().getVendorOrderNumber() : null);
        }

        return builder.build();
    }
}
