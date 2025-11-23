package com.eps.module.api.epsone.asset_expenditure_and_activity_work.validator;

import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.constant.AssetExpenditureAndActivityWorkErrorMessages;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkBulkUploadDto;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.repository.AssetExpenditureAndActivityWorkRepository;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssetExpenditureAndActivityWorkBulkUploadValidator implements BulkRowValidator<AssetExpenditureAndActivityWorkBulkUploadDto> {

    private final AssetExpenditureAndActivityWorkRepository assetExpenditureAndActivityWorkRepository;
    private final AssetRepository assetRepository;
    private final InvoiceRepository invoiceRepository;
    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final ActivityWorkRepository activityWorkRepository;

    @Override
    public List<BulkUploadErrorDto> validate(AssetExpenditureAndActivityWorkBulkUploadDto rowData, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Asset Tag ID (required)
        if (rowData.getAssetTagId() == null || rowData.getAssetTagId().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Asset Tag ID")
                    .errorMessage(AssetExpenditureAndActivityWorkErrorMessages.ASSET_TAG_ID_REQUIRED)
                    .rejectedValue(rowData.getAssetTagId())
                    .build());
        } else {
            // Check if asset exists
            var asset = assetRepository.findByAssetTagId(rowData.getAssetTagId().trim());
            if (asset.isEmpty()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Asset Tag ID")
                        .errorMessage(String.format(AssetExpenditureAndActivityWorkErrorMessages.ASSET_NOT_FOUND_TAG, rowData.getAssetTagId()))
                        .rejectedValue(rowData.getAssetTagId())
                        .build());
            }
        }

        // Validate Invoice Number (optional, but must exist if provided)
        if (rowData.getInvoiceNumber() != null && !rowData.getInvoiceNumber().trim().isEmpty()) {
            boolean exists = invoiceRepository.existsByInvoiceNumber(rowData.getInvoiceNumber().trim());
            if (!exists) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Invoice Number")
                        .errorMessage(String.format(AssetExpenditureAndActivityWorkErrorMessages.INVOICE_NOT_FOUND, rowData.getInvoiceNumber()))
                        .rejectedValue(rowData.getInvoiceNumber())
                        .build());
            }
        }

        // Validate Activity Work ID (optional, but must exist if provided)
        if (rowData.getActivityWorkId() != null && !rowData.getActivityWorkId().trim().isEmpty()) {
            try {
                Long activityWorkId = Long.parseLong(rowData.getActivityWorkId().trim());
                boolean exists = activityWorkRepository.existsById(activityWorkId);
                if (!exists) {
                    errors.add(BulkUploadErrorDto.builder()
                            .rowNumber(rowNumber)
                            .fieldName("Activity Work ID")
                            .errorMessage(String.format(AssetExpenditureAndActivityWorkErrorMessages.ACTIVITY_WORK_NOT_FOUND_MSG, rowData.getActivityWorkId()))
                            .rejectedValue(rowData.getActivityWorkId())
                            .build());
                }
            } catch (NumberFormatException e) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Activity Work ID")
                        .errorMessage(AssetExpenditureAndActivityWorkErrorMessages.ACTIVITY_WORK_ID_INVALID)
                        .rejectedValue(rowData.getActivityWorkId())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(AssetExpenditureAndActivityWorkBulkUploadDto rowData) {
        if (rowData.getAssetTagId() == null || rowData.getAssetTagId().trim().isEmpty()) {
            return false;
        }

        // Find asset by tag ID
        var assetOpt = assetRepository.findByAssetTagId(rowData.getAssetTagId().trim());
        if (assetOpt.isEmpty()) {
            return false;
        }
        Long assetId = assetOpt.get().getId();

        // Find expenditure invoice ID if invoice number is provided
        Long expendituresInvoiceId = null;
        if (rowData.getInvoiceNumber() != null && !rowData.getInvoiceNumber().trim().isEmpty()) {
            // We need to find the expenditure invoice by invoice number
            // This requires finding the invoice first, then finding the expenditure invoice
            var invoices = expendituresInvoiceRepository.findAllWithDetailsList();
            for (var ei : invoices) {
                if (ei.getInvoice() != null && 
                    ei.getInvoice().getInvoiceNumber().equalsIgnoreCase(rowData.getInvoiceNumber().trim())) {
                    expendituresInvoiceId = ei.getId();
                    break;
                }
            }
        }

        // Parse activity work ID if provided
        Long activityWorkId = null;
        if (rowData.getActivityWorkId() != null && !rowData.getActivityWorkId().trim().isEmpty()) {
            try {
                activityWorkId = Long.parseLong(rowData.getActivityWorkId().trim());
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // Check if this combination already exists
        return assetExpenditureAndActivityWorkRepository.existsByAssetIdAndExpendituresInvoiceIdAndActivityWorkId(
                assetId, expendituresInvoiceId, activityWorkId);
    }
}
