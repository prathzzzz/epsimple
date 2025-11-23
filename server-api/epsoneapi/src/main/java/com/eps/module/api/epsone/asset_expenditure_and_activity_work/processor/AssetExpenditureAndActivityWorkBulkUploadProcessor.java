package com.eps.module.api.epsone.asset_expenditure_and_activity_work.processor;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkBulkUploadDto;
import com.eps.module.api.epsone.activity_work.constant.ActivityWorkErrorMessages;
import com.eps.module.api.epsone.asset.constant.AssetErrorMessages;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.repository.AssetExpenditureAndActivityWorkRepository;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.validator.AssetExpenditureAndActivityWorkBulkUploadValidator;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.api.epsone.invoice.constant.InvoiceErrorMessages;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetExpenditureAndActivityWork;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.ExpendituresInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssetExpenditureAndActivityWorkBulkUploadProcessor extends BulkUploadProcessor<AssetExpenditureAndActivityWorkBulkUploadDto, AssetExpenditureAndActivityWork> {

    private final AssetExpenditureAndActivityWorkRepository assetExpenditureAndActivityWorkRepository;
    private final AssetRepository assetRepository;
    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final AssetExpenditureAndActivityWorkBulkUploadValidator validator;

    @Override
    protected AssetExpenditureAndActivityWorkBulkUploadValidator getValidator() {
        return validator;
    }

    @Override
    @Transactional
    protected AssetExpenditureAndActivityWork convertToEntity(AssetExpenditureAndActivityWorkBulkUploadDto dto) {
        // Find asset by tag ID
        Asset asset = assetRepository
                .findByAssetTagId(dto.getAssetTagId().trim())
                .orElseThrow(() -> new ResourceNotFoundException(AssetErrorMessages.ASSET_NOT_FOUND_TAG + dto.getAssetTagId()));

        // Find expenditure invoice by invoice number (if provided)
        ExpendituresInvoice expendituresInvoice = null;
        if (dto.getInvoiceNumber() != null && !dto.getInvoiceNumber().trim().isEmpty()) {
            var invoices = expendituresInvoiceRepository.findAllWithDetailsList();
            for (var ei : invoices) {
                if (ei.getInvoice() != null && 
                    ei.getInvoice().getInvoiceNumber().equalsIgnoreCase(dto.getInvoiceNumber().trim())) {
                    expendituresInvoice = ei;
                    break;
                }
            }
            if (expendituresInvoice == null) {
                throw new ResourceNotFoundException(InvoiceErrorMessages.EXPENDITURE_INVOICE_NOT_FOUND + dto.getInvoiceNumber());
            }
        }

        // Find activity work by ID (if provided)
        ActivityWork activityWork = null;
        if (dto.getActivityWorkId() != null && !dto.getActivityWorkId().trim().isEmpty()) {
            Long activityWorkId = Long.parseLong(dto.getActivityWorkId().trim());
            activityWork = activityWorkRepository.findById(activityWorkId)
                    .orElseThrow(() -> new ResourceNotFoundException(ActivityWorkErrorMessages.ACTIVITY_WORK_NOT_FOUND_ID + activityWorkId));
        }

        // Build AssetExpenditureAndActivityWork entity
        return AssetExpenditureAndActivityWork.builder()
                .asset(asset)
                .expendituresInvoice(expendituresInvoice)
                .activityWork(activityWork)
                .build();
    }

    @Override
    @Transactional
    protected void saveEntity(AssetExpenditureAndActivityWork entity) {
        assetExpenditureAndActivityWorkRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(AssetExpenditureAndActivityWorkBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Asset Tag ID", dto.getAssetTagId());
        rowData.put("Invoice Number", dto.getInvoiceNumber());
        rowData.put("Activity Work ID", dto.getActivityWorkId());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(AssetExpenditureAndActivityWorkBulkUploadDto dto) {
        return (dto.getAssetTagId() == null || dto.getAssetTagId().trim().isEmpty()) &&
                (dto.getInvoiceNumber() == null || dto.getInvoiceNumber().trim().isEmpty()) &&
                (dto.getActivityWorkId() == null || dto.getActivityWorkId().trim().isEmpty());
    }
}
