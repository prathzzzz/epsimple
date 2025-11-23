package com.eps.module.api.epsone.site_activity_work_expenditure.bulk;

import com.eps.module.api.epsone.activities.repository.ActivitiesRepository;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.site_activity_work_expenditure.constant.SiteActivityWorkExpenditureErrorMessages;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureBulkUploadDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.repository.SiteActivityWorkExpenditureRepository;
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
public class SiteActivityWorkExpenditureBulkUploadValidator implements BulkRowValidator<SiteActivityWorkExpenditureBulkUploadDto> {

    private final SiteRepository siteRepository;
    private final ActivitiesRepository activitiesRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final SiteActivityWorkExpenditureRepository repository;

    @Override
    public List<BulkUploadErrorDto> validate(SiteActivityWorkExpenditureBulkUploadDto dto, int rowNumber) {
        List<BulkUploadErrorDto> errors = new ArrayList<>();

        // Validate Site Code (required)
        if (dto.getSiteCode() == null || dto.getSiteCode().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Site Code")
                    .errorMessage(SiteActivityWorkExpenditureErrorMessages.SITE_CODE_REQUIRED)
                    .rejectedValue(dto.getSiteCode())
                    .build());
        } else {
            if (!siteRepository.existsBySiteCodeIgnoreCase(dto.getSiteCode().trim())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Site Code")
                        .errorMessage(String.format(SiteActivityWorkExpenditureErrorMessages.SITE_NOT_FOUND_CODE, dto.getSiteCode()))
                        .rejectedValue(dto.getSiteCode())
                        .build());
            }
        }

        // Validate Activity Name (required)
        if (dto.getActivityName() == null || dto.getActivityName().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Activity Name")
                    .errorMessage(SiteActivityWorkExpenditureErrorMessages.ACTIVITY_NAME_REQUIRED)
                    .rejectedValue(dto.getActivityName())
                    .build());
        } else {
            if (!activitiesRepository.existsByActivityNameIgnoreCase(dto.getActivityName().trim())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Activity Name")
                        .errorMessage(String.format(SiteActivityWorkExpenditureErrorMessages.ACTIVITY_NOT_FOUND_NAME, dto.getActivityName()))
                        .rejectedValue(dto.getActivityName())
                        .build());
            }
        }

        // Validate Vendor Order Number (required)
        if (dto.getVendorOrderNumber() == null || dto.getVendorOrderNumber().trim().isEmpty()) {
            errors.add(BulkUploadErrorDto.builder()
                    .rowNumber(rowNumber)
                    .fieldName("Vendor Order Number")
                    .errorMessage(SiteActivityWorkExpenditureErrorMessages.VENDOR_ORDER_NUMBER_REQUIRED)
                    .rejectedValue(dto.getVendorOrderNumber())
                    .build());
        } else if (dto.getActivityName() != null && !dto.getActivityName().trim().isEmpty()) {
            // Only validate if activity name is valid
            var activityWork = activityWorkRepository.findByActivityNameAndVendorOrderNumber(
                    dto.getActivityName().trim(),
                    dto.getVendorOrderNumber().trim());
            if (activityWork.isEmpty()) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Vendor Order Number")
                        .errorMessage(String.format(SiteActivityWorkExpenditureErrorMessages.ACTIVITY_WORK_NOT_FOUND_DETAILS, dto.getActivityName(), dto.getVendorOrderNumber()))
                        .rejectedValue(dto.getVendorOrderNumber())
                        .build());
            }
        }

        // Validate Invoice Number (optional, but must exist if provided)
        if (dto.getInvoiceNumber() != null && !dto.getInvoiceNumber().trim().isEmpty()) {
             if (!expendituresInvoiceRepository.existsByInvoiceNumber(dto.getInvoiceNumber().trim())) {
                errors.add(BulkUploadErrorDto.builder()
                        .rowNumber(rowNumber)
                        .fieldName("Invoice Number")
                        .errorMessage(String.format(SiteActivityWorkExpenditureErrorMessages.EXPENDITURES_INVOICE_NOT_FOUND_NUMBER, dto.getInvoiceNumber()))
                        .rejectedValue(dto.getInvoiceNumber())
                        .build());
            }
        }

        return errors;
    }

    @Override
    public boolean isDuplicate(SiteActivityWorkExpenditureBulkUploadDto dto) {
        if (dto.getSiteCode() == null || dto.getActivityName() == null || 
            dto.getVendorOrderNumber() == null || dto.getInvoiceNumber() == null) {
            return false;
        }

        try {
            var site = siteRepository.findBySiteCodeIgnoreCase(dto.getSiteCode().trim());
            if (site.isEmpty()) {
                return false;
            }

            var activityWork = activityWorkRepository.findByActivityNameAndVendorOrderNumber(
                    dto.getActivityName().trim(),
                    dto.getVendorOrderNumber().trim());
            if (activityWork.isEmpty()) {
                return false;
            }

            var expenditureInvoice = expendituresInvoiceRepository.findByInvoiceNumber(dto.getInvoiceNumber().trim());
            if (expenditureInvoice.isEmpty()) {
                return false;
            }

            return repository.existsBySiteIdAndActivityWorkIdAndExpendituresInvoiceId(
                    site.get().getId(),
                    activityWork.get().getId(),
                    expenditureInvoice.get().getId()
            );
        } catch (Exception e) {
            log.error("Error checking duplicate: {}", e.getMessage());
            return false;
        }
    }
}
