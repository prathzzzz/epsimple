package com.eps.module.api.epsone.asset_placement.mapper;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnDatacenterRequestDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnDatacenterResponseDto;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetsOnDatacenter;
import com.eps.module.asset.AssetMovementTracker;
import com.eps.module.status.GenericStatusType;
import com.eps.module.warehouse.Datacenter;
import org.springframework.stereotype.Component;

@Component
public class AssetsOnDatacenterMapper {

    public AssetsOnDatacenter toEntity(
            AssetsOnDatacenterRequestDto dto,
            Asset asset,
            Datacenter datacenter,
            GenericStatusType assetStatus,
            ActivityWork activityWork,
            AssetMovementTracker assetMovementTracker) {

        return AssetsOnDatacenter.builder()
                .asset(asset)
                .datacenter(datacenter)
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
            AssetsOnDatacenter assetsOnDatacenter,
            AssetsOnDatacenterRequestDto dto,
            Asset asset,
            Datacenter datacenter,
            GenericStatusType assetStatus,
            ActivityWork activityWork,
            AssetMovementTracker assetMovementTracker) {

        assetsOnDatacenter.setAsset(asset);
        assetsOnDatacenter.setDatacenter(datacenter);
        assetsOnDatacenter.setAssetStatus(assetStatus);
        assetsOnDatacenter.setActivityWork(activityWork);
        assetsOnDatacenter.setAssetMovementTracker(assetMovementTracker);
        assetsOnDatacenter.setAssignedOn(dto.getAssignedOn());
        assetsOnDatacenter.setDeliveredOn(dto.getDeliveredOn());
        assetsOnDatacenter.setCommissionedOn(dto.getCommissionedOn());
        assetsOnDatacenter.setVacatedOn(dto.getVacatedOn());
        assetsOnDatacenter.setDisposedOn(dto.getDisposedOn());
        assetsOnDatacenter.setScrappedOn(dto.getScrappedOn());
    }

    public AssetsOnDatacenterResponseDto toDto(AssetsOnDatacenter assetsOnDatacenter) {
        return AssetsOnDatacenterResponseDto.builder()
                .id(assetsOnDatacenter.getId())
                .assetId(assetsOnDatacenter.getAsset().getId())
                .assetTagId(assetsOnDatacenter.getAsset().getAssetTagId())
                .assetName(assetsOnDatacenter.getAsset().getAssetName())
                .assetTypeName(assetsOnDatacenter.getAsset().getAssetType() != null ?
                        assetsOnDatacenter.getAsset().getAssetType().getTypeName() : null)
                .assetCategoryName(assetsOnDatacenter.getAsset().getAssetCategory() != null ?
                        assetsOnDatacenter.getAsset().getAssetCategory().getCategoryName() : null)
                .datacenterId(assetsOnDatacenter.getDatacenter().getId())
                .datacenterCode(assetsOnDatacenter.getDatacenter().getDatacenterCode())
                .datacenterName(assetsOnDatacenter.getDatacenter().getDatacenterName())
                .assetStatusId(assetsOnDatacenter.getAssetStatus().getId())
                .assetStatusName(assetsOnDatacenter.getAssetStatus().getStatusName())
                .activityWorkId(assetsOnDatacenter.getActivityWork() != null ?
                        assetsOnDatacenter.getActivityWork().getId() : null)
                .activityWorkNumber(assetsOnDatacenter.getActivityWork() != null ?
                        assetsOnDatacenter.getActivityWork().getVendorOrderNumber() : null)
                .assetMovementTrackerId(assetsOnDatacenter.getAssetMovementTracker() != null ?
                        assetsOnDatacenter.getAssetMovementTracker().getId() : null)
                .assignedOn(assetsOnDatacenter.getAssignedOn())
                .deliveredOn(assetsOnDatacenter.getDeliveredOn())
                .commissionedOn(assetsOnDatacenter.getCommissionedOn())
                .vacatedOn(assetsOnDatacenter.getVacatedOn())
                .disposedOn(assetsOnDatacenter.getDisposedOn())
                .scrappedOn(assetsOnDatacenter.getScrappedOn())
                .build();
    }
}
