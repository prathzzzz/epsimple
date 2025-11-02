package com.eps.module.api.epsone.assetplacement.mapper;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnWarehouseRequestDto;
import com.eps.module.api.epsone.assetplacement.dto.AssetsOnWarehouseResponseDto;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetsOnWarehouse;
import com.eps.module.asset.AssetMovementTracker;
import com.eps.module.status.GenericStatusType;
import com.eps.module.warehouse.Warehouse;
import org.springframework.stereotype.Component;

@Component
public class AssetsOnWarehouseMapper {

    public AssetsOnWarehouse toEntity(
            AssetsOnWarehouseRequestDto dto,
            Asset asset,
            Warehouse warehouse,
            GenericStatusType assetStatus,
            ActivityWork activityWork,
            AssetMovementTracker assetMovementTracker) {

        return AssetsOnWarehouse.builder()
                .asset(asset)
                .warehouse(warehouse)
                .assetStatus(assetStatus)
                .activityWork(activityWork)
                .assetMovementTracker(assetMovementTracker)
                .assignedOn(dto.getAssignedOn())
                .deliveredOn(dto.getDeliveredOn())
                .commissionedOn(dto.getCommissionedOn())
                .vacatedOn(dto.getVacatedOn())
                .disposedOn(dto.getDisposedOn())
                .scrappedOn(dto.getScrappedOn())
                .build();
    }

    public void updateEntity(
            AssetsOnWarehouse assetsOnWarehouse,
            AssetsOnWarehouseRequestDto dto,
            Asset asset,
            Warehouse warehouse,
            GenericStatusType assetStatus,
            ActivityWork activityWork,
            AssetMovementTracker assetMovementTracker) {

        assetsOnWarehouse.setAsset(asset);
        assetsOnWarehouse.setWarehouse(warehouse);
        assetsOnWarehouse.setAssetStatus(assetStatus);
        assetsOnWarehouse.setActivityWork(activityWork);
        assetsOnWarehouse.setAssetMovementTracker(assetMovementTracker);
        assetsOnWarehouse.setAssignedOn(dto.getAssignedOn());
        assetsOnWarehouse.setDeliveredOn(dto.getDeliveredOn());
        assetsOnWarehouse.setCommissionedOn(dto.getCommissionedOn());
        assetsOnWarehouse.setVacatedOn(dto.getVacatedOn());
        assetsOnWarehouse.setDisposedOn(dto.getDisposedOn());
        assetsOnWarehouse.setScrappedOn(dto.getScrappedOn());
    }

    public AssetsOnWarehouseResponseDto toDto(AssetsOnWarehouse assetsOnWarehouse) {
        return AssetsOnWarehouseResponseDto.builder()
                .id(assetsOnWarehouse.getId())
                .assetId(assetsOnWarehouse.getAsset().getId())
                .assetTagId(assetsOnWarehouse.getAsset().getAssetTagId())
                .assetName(assetsOnWarehouse.getAsset().getAssetName())
                .assetTypeName(assetsOnWarehouse.getAsset().getAssetType() != null ?
                        assetsOnWarehouse.getAsset().getAssetType().getTypeName() : null)
                .assetCategoryName(assetsOnWarehouse.getAsset().getAssetCategory() != null ?
                        assetsOnWarehouse.getAsset().getAssetCategory().getCategoryName() : null)
                .warehouseId(assetsOnWarehouse.getWarehouse().getId())
                .warehouseCode(assetsOnWarehouse.getWarehouse().getWarehouseCode())
                .warehouseName(assetsOnWarehouse.getWarehouse().getWarehouseName())
                .assetStatusId(assetsOnWarehouse.getAssetStatus().getId())
                .assetStatusName(assetsOnWarehouse.getAssetStatus().getStatusName())
                .activityWorkId(assetsOnWarehouse.getActivityWork() != null ?
                        assetsOnWarehouse.getActivityWork().getId() : null)
                .activityWorkNumber(assetsOnWarehouse.getActivityWork() != null ?
                        assetsOnWarehouse.getActivityWork().getVendorOrderNumber() : null)
                .assetMovementTrackerId(assetsOnWarehouse.getAssetMovementTracker() != null ?
                        assetsOnWarehouse.getAssetMovementTracker().getId() : null)
                .assignedOn(assetsOnWarehouse.getAssignedOn())
                .deliveredOn(assetsOnWarehouse.getDeliveredOn())
                .commissionedOn(assetsOnWarehouse.getCommissionedOn())
                .vacatedOn(assetsOnWarehouse.getVacatedOn())
                .disposedOn(assetsOnWarehouse.getDisposedOn())
                .scrappedOn(assetsOnWarehouse.getScrappedOn())
                .build();
    }
}
