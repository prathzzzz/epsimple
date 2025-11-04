package com.eps.module.api.epsone.asset_expenditure_and_activity_work.mapper;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkRequestDto;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkResponseDto;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetExpenditureAndActivityWork;
import com.eps.module.cost.ExpendituresInvoice;
import org.springframework.stereotype.Component;

@Component
public class AssetExpenditureAndActivityWorkMapper {

    public AssetExpenditureAndActivityWork toEntity(
            AssetExpenditureAndActivityWorkRequestDto dto,
            Asset asset,
            ExpendituresInvoice expendituresInvoice,
            ActivityWork activityWork) {
        
        return AssetExpenditureAndActivityWork.builder()
                .asset(asset)
                .expendituresInvoice(expendituresInvoice)
                .activityWork(activityWork)
                .build();
    }

    public void updateEntity(
            AssetExpenditureAndActivityWork entity,
            AssetExpenditureAndActivityWorkRequestDto dto,
            Asset asset,
            ExpendituresInvoice expendituresInvoice,
            ActivityWork activityWork) {
        
        entity.setAsset(asset);
        entity.setExpendituresInvoice(expendituresInvoice);
        entity.setActivityWork(activityWork);
    }

    public AssetExpenditureAndActivityWorkResponseDto toDto(AssetExpenditureAndActivityWork entity) {
        return AssetExpenditureAndActivityWorkResponseDto.builder()
                .id(entity.getId())
                // Asset details
                .assetId(entity.getAsset().getId())
                .assetTagId(entity.getAsset().getAssetTagId())
                .assetName(entity.getAsset().getAssetName())
                // Activity Work details (optional)
                .activityWorkId(entity.getActivityWork() != null ? entity.getActivityWork().getId() : null)
                .vendorOrderNumber(entity.getActivityWork() != null ? 
                                  entity.getActivityWork().getVendorOrderNumber() : null)
                .activityName(entity.getActivityWork() != null && 
                             entity.getActivityWork().getActivities() != null ?
                             entity.getActivityWork().getActivities().getActivityName() : null)
                // Expenditure Invoice details (optional)
                .expendituresInvoiceId(entity.getExpendituresInvoice() != null ? 
                                      entity.getExpendituresInvoice().getId() : null)
                .invoiceNumber(entity.getExpendituresInvoice() != null && 
                              entity.getExpendituresInvoice().getInvoice() != null ?
                              entity.getExpendituresInvoice().getInvoice().getInvoiceNumber() : null)
                .amount(entity.getExpendituresInvoice() != null && 
                       entity.getExpendituresInvoice().getInvoice() != null &&
                       entity.getExpendituresInvoice().getInvoice().getTotalInvoiceValue() != null ?
                       entity.getExpendituresInvoice().getInvoice().getTotalInvoiceValue().doubleValue() : null)
                .costItemName(entity.getExpendituresInvoice() != null && 
                             entity.getExpendituresInvoice().getCostItem() != null ?
                             entity.getExpendituresInvoice().getCostItem().getCostItemFor() : null)
                // Timestamps
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
