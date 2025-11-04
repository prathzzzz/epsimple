package com.eps.module.api.epsone.site_activity_work_expenditure.mapper;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureRequestDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureResponseDto;
import com.eps.module.cost.ExpendituresInvoice;
import com.eps.module.site.Site;
import com.eps.module.site.SiteActivityWorkExpenditure;
import org.springframework.stereotype.Component;

@Component
public class SiteActivityWorkExpenditureMapper {

    public SiteActivityWorkExpenditure toEntity(
            SiteActivityWorkExpenditureRequestDto dto,
            Site site,
            ActivityWork activityWork,
            ExpendituresInvoice expendituresInvoice) {
        
        return SiteActivityWorkExpenditure.builder()
                .site(site)
                .activityWork(activityWork)
                .expendituresInvoice(expendituresInvoice)
                .build();
    }

    public void updateEntity(
            SiteActivityWorkExpenditure entity,
            SiteActivityWorkExpenditureRequestDto dto,
            Site site,
            ActivityWork activityWork,
            ExpendituresInvoice expendituresInvoice) {
        
        entity.setSite(site);
        entity.setActivityWork(activityWork);
        entity.setExpendituresInvoice(expendituresInvoice);
    }

    public SiteActivityWorkExpenditureResponseDto toDto(SiteActivityWorkExpenditure entity) {
        return SiteActivityWorkExpenditureResponseDto.builder()
                .id(entity.getId())
                // Site details
                .siteId(entity.getSite().getId())
                .siteCode(entity.getSite().getSiteCode())
                .siteName(entity.getSite().getLocation() != null ? 
                         entity.getSite().getLocation().getLocationName() : null)
                // Activity Work details
                .activityWorkId(entity.getActivityWork().getId())
                .vendorOrderNumber(entity.getActivityWork().getVendorOrderNumber())
                .activityName(entity.getActivityWork().getActivities() != null ?
                             entity.getActivityWork().getActivities().getActivityName() : null)
                // Expenditure Invoice details
                .expendituresInvoiceId(entity.getExpendituresInvoice().getId())
                .invoiceNumber(entity.getExpendituresInvoice().getInvoice() != null ?
                              entity.getExpendituresInvoice().getInvoice().getInvoiceNumber() : null)
                .amount(entity.getExpendituresInvoice().getInvoice() != null ?
                       entity.getExpendituresInvoice().getInvoice().getTotalInvoiceValue() != null ?
                       entity.getExpendituresInvoice().getInvoice().getTotalInvoiceValue().doubleValue() : null : null)
                .costItemName(entity.getExpendituresInvoice().getCostItem() != null ?
                             entity.getExpendituresInvoice().getCostItem().getCostItemFor() : null)
                // Timestamps
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
