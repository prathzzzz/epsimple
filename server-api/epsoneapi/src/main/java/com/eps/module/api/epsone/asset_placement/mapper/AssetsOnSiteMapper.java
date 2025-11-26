package com.eps.module.api.epsone.asset_placement.mapper;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnSiteRequestDto;
import com.eps.module.api.epsone.asset_placement.dto.AssetsOnSiteResponseDto;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetsOnSite;
import com.eps.module.asset.AssetMovementTracker;
import com.eps.module.auth.audit.AuditUserResolver;
import com.eps.module.site.Site;
import com.eps.module.status.GenericStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssetsOnSiteMapper {

    private final AuditUserResolver auditUserResolver;

    public AssetsOnSite toEntity(
            AssetsOnSiteRequestDto dto,
            Asset asset,
            Site site,
            GenericStatusType assetStatus,
            ActivityWork activityWork,
            AssetMovementTracker assetMovementTracker) {

        return AssetsOnSite.builder()
                .asset(asset)
                .site(site)
                .assetStatus(assetStatus)
                .activityWork(activityWork)
                .assetMovementTracker(assetMovementTracker)
                .assignedOn(dto.getAssignedOn())
                .deliveredOn(dto.getDeliveredOn())
                .deployedOn(dto.getDeployedOn())
                .activatedOn(dto.getActivatedOn())
                .decommissionedOn(dto.getDecommissionedOn())
                .vacatedOn(dto.getVacatedOn())
                .build();
    }

    public void updateEntity(
            AssetsOnSite assetsOnSite,
            AssetsOnSiteRequestDto dto,
            Asset asset,
            Site site,
            GenericStatusType assetStatus,
            ActivityWork activityWork,
            AssetMovementTracker assetMovementTracker) {

        assetsOnSite.setAsset(asset);
        assetsOnSite.setSite(site);
        assetsOnSite.setAssetStatus(assetStatus);
        assetsOnSite.setActivityWork(activityWork);
        assetsOnSite.setAssetMovementTracker(assetMovementTracker);
        assetsOnSite.setAssignedOn(dto.getAssignedOn());
        assetsOnSite.setDeliveredOn(dto.getDeliveredOn());
        assetsOnSite.setDeployedOn(dto.getDeployedOn());
        assetsOnSite.setActivatedOn(dto.getActivatedOn());
        assetsOnSite.setDecommissionedOn(dto.getDecommissionedOn());
        assetsOnSite.setVacatedOn(dto.getVacatedOn());
    }

    public AssetsOnSiteResponseDto toDto(AssetsOnSite assetsOnSite) {
        return AssetsOnSiteResponseDto.builder()
                .id(assetsOnSite.getId())
                .assetId(assetsOnSite.getAsset().getId())
                .assetTagId(assetsOnSite.getAsset().getAssetTagId())
                .assetName(assetsOnSite.getAsset().getAssetName())
                .assetTypeName(assetsOnSite.getAsset().getAssetType() != null ?
                        assetsOnSite.getAsset().getAssetType().getTypeName() : null)
                .assetCategoryName(assetsOnSite.getAsset().getAssetCategory() != null ?
                        assetsOnSite.getAsset().getAssetCategory().getCategoryName() : null)
                .siteId(assetsOnSite.getSite().getId())
                .siteCode(assetsOnSite.getSite().getSiteCode())
                .siteName(assetsOnSite.getSite().getLocation() != null ?
                        assetsOnSite.getSite().getLocation().getLocationName() : null)
                .assetStatusId(assetsOnSite.getAssetStatus().getId())
                .assetStatusName(assetsOnSite.getAssetStatus().getStatusName())
                .activityWorkId(assetsOnSite.getActivityWork() != null ?
                        assetsOnSite.getActivityWork().getId() : null)
                .activityWorkNumber(assetsOnSite.getActivityWork() != null ?
                        assetsOnSite.getActivityWork().getVendorOrderNumber() : null)
                .assetMovementTrackerId(assetsOnSite.getAssetMovementTracker() != null ?
                        assetsOnSite.getAssetMovementTracker().getId() : null)
                .assignedOn(assetsOnSite.getAssignedOn())
                .deliveredOn(assetsOnSite.getDeliveredOn())
                .deployedOn(assetsOnSite.getDeployedOn())
                .activatedOn(assetsOnSite.getActivatedOn())
                .decommissionedOn(assetsOnSite.getDecommissionedOn())
                .vacatedOn(assetsOnSite.getVacatedOn())
                .createdAt(assetsOnSite.getCreatedAt())
                .updatedAt(assetsOnSite.getUpdatedAt())
                .createdBy(auditUserResolver.resolveUserName(assetsOnSite.getCreatedBy()))
                .updatedBy(auditUserResolver.resolveUserName(assetsOnSite.getUpdatedBy()))
                .build();
    }
}
