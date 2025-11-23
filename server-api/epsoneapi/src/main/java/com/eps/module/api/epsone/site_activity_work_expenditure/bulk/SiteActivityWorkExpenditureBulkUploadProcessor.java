package com.eps.module.api.epsone.site_activity_work_expenditure.bulk;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureBulkUploadDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.repository.SiteActivityWorkExpenditureRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.ExpendituresInvoice;
import com.eps.module.site.Site;
import com.eps.module.site.SiteActivityWorkExpenditure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteActivityWorkExpenditureBulkUploadProcessor extends BulkUploadProcessor<SiteActivityWorkExpenditureBulkUploadDto, SiteActivityWorkExpenditure> {

    private final SiteActivityWorkExpenditureRepository repository;
    private final SiteRepository siteRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final SiteActivityWorkExpenditureBulkUploadValidator validator;

    @Override
    protected SiteActivityWorkExpenditureBulkUploadValidator getValidator() {
        return validator;
    }

    @Override
    @Transactional
    protected SiteActivityWorkExpenditure convertToEntity(SiteActivityWorkExpenditureBulkUploadDto dto) {
        // Fetch site by code
        Site site = siteRepository.findBySiteCodeIgnoreCase(dto.getSiteCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Site not found with code: " + dto.getSiteCode()));

        // Fetch activity work by activity name and vendor order number (required)
        ActivityWork activityWork;
        try {
            activityWork = activityWorkRepository.findByActivityNameAndVendorOrderNumber(
                    dto.getActivityName().trim(),
                    dto.getVendorOrderNumber().trim())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No Activity Work found for Activity '" + dto.getActivityName() + 
                            "' with Vendor Order Number '" + dto.getVendorOrderNumber() + "'. " +
                            "Please verify both values are correct."));
        } catch (jakarta.persistence.NonUniqueResultException e) {
            throw new ConflictException(
                    "Multiple Activity Works found for Activity '" + dto.getActivityName() + 
                    "' with Vendor Order Number '" + dto.getVendorOrderNumber() + "'. " +
                    "This indicates duplicate records in the database. Please contact your administrator.");
        }

        // Fetch expenditure invoice by invoice number (optional)
        ExpendituresInvoice expendituresInvoice = null;
        if (dto.getInvoiceNumber() != null && !dto.getInvoiceNumber().trim().isEmpty()) {
            var expenditureInvoiceList = expendituresInvoiceRepository.findAllWithDetailsList();
            for (var ei : expenditureInvoiceList) {
                if (ei.getInvoice() != null && 
                    ei.getInvoice().getInvoiceNumber().equalsIgnoreCase(dto.getInvoiceNumber().trim())) {
                    expendituresInvoice = ei;
                    break;
                }
            }
            
            if (expendituresInvoice == null) {
                throw new ResourceNotFoundException("Expenditure Invoice not found with invoice number: " + dto.getInvoiceNumber());
            }
        }

        // Build entity
        return SiteActivityWorkExpenditure.builder()
                .site(site)
                .activityWork(activityWork)
                .expendituresInvoice(expendituresInvoice)
                .build();
    }

    @Override
    @Transactional
    protected void saveEntity(SiteActivityWorkExpenditure entity) {
        repository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(SiteActivityWorkExpenditureBulkUploadDto dto) {
        Map<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Site Code", dto.getSiteCode());
        rowData.put("Activity Name", dto.getActivityName());
        rowData.put("Vendor Order Number", dto.getVendorOrderNumber());
        rowData.put("Invoice Number", dto.getInvoiceNumber());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(SiteActivityWorkExpenditureBulkUploadDto dto) {
        return (dto.getSiteCode() == null || dto.getSiteCode().trim().isEmpty()) &&
               (dto.getActivityName() == null || dto.getActivityName().trim().isEmpty()) &&
               (dto.getVendorOrderNumber() == null || dto.getVendorOrderNumber().trim().isEmpty()) &&
               (dto.getInvoiceNumber() == null || dto.getInvoiceNumber().trim().isEmpty());
    }
}
