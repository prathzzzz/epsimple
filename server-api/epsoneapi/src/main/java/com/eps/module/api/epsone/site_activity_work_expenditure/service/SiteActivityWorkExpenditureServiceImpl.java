package com.eps.module.api.epsone.site_activity_work_expenditure.service;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.api.epsone.site.repository.SiteRepository;
import com.eps.module.api.epsone.site_activity_work_expenditure.bulk.SiteActivityWorkExpenditureBulkUploadProcessor;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureBulkUploadDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureErrorReportDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureRequestDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.dto.SiteActivityWorkExpenditureResponseDto;
import com.eps.module.api.epsone.site_activity_work_expenditure.mapper.SiteActivityWorkExpenditureMapper;
import com.eps.module.api.epsone.site_activity_work_expenditure.repository.SiteActivityWorkExpenditureRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.ExpendituresInvoice;
import com.eps.module.site.Site;
import com.eps.module.site.SiteActivityWorkExpenditure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class SiteActivityWorkExpenditureServiceImpl 
        extends BaseBulkUploadService<SiteActivityWorkExpenditureBulkUploadDto, SiteActivityWorkExpenditure>
        implements SiteActivityWorkExpenditureService {

    private final SiteActivityWorkExpenditureRepository repository;
    private final SiteRepository siteRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final SiteActivityWorkExpenditureMapper mapper;
    private final SiteActivityWorkExpenditureBulkUploadProcessor processor;

    public SiteActivityWorkExpenditureServiceImpl(
            SiteActivityWorkExpenditureRepository repository,
            SiteRepository siteRepository,
            ActivityWorkRepository activityWorkRepository,
            ExpendituresInvoiceRepository expendituresInvoiceRepository,
            SiteActivityWorkExpenditureMapper mapper,
            SiteActivityWorkExpenditureBulkUploadProcessor processor) {
        this.repository = repository;
        this.siteRepository = siteRepository;
        this.activityWorkRepository = activityWorkRepository;
        this.expendituresInvoiceRepository = expendituresInvoiceRepository;
        this.mapper = mapper;
        this.processor = processor;
    }

    @Override
    protected BulkUploadProcessor<SiteActivityWorkExpenditureBulkUploadDto, SiteActivityWorkExpenditure> getProcessor() {
        return processor;
    }

    @Override
    public Class<SiteActivityWorkExpenditureBulkUploadDto> getBulkUploadDtoClass() {
        return SiteActivityWorkExpenditureBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Site Activity Work Expenditure";
    }

    @Override
    public List<SiteActivityWorkExpenditure> getAllEntitiesForExport() {
        return repository.findAllForExport();
    }

    @Override
    public Function<SiteActivityWorkExpenditure, SiteActivityWorkExpenditureBulkUploadDto> getEntityToDtoMapper() {
        return entity -> {
            String siteCode = entity.getSite() != null ? entity.getSite().getSiteCode() : "";
            
            String activityName = "";
            String vendorOrderNumber = "";
            if (entity.getActivityWork() != null) {
                if (entity.getActivityWork().getActivities() != null) {
                    activityName = entity.getActivityWork().getActivities().getActivityName() != null ?
                        entity.getActivityWork().getActivities().getActivityName() : "";
                }
                vendorOrderNumber = entity.getActivityWork().getVendorOrderNumber() != null ? 
                    entity.getActivityWork().getVendorOrderNumber() : "";
            }
            
            String invoiceNumber = "";
            if (entity.getExpendituresInvoice() != null && entity.getExpendituresInvoice().getInvoice() != null) {
                invoiceNumber = entity.getExpendituresInvoice().getInvoice().getInvoiceNumber();
            }
            
            return SiteActivityWorkExpenditureBulkUploadDto.builder()
                    .siteCode(siteCode)
                    .activityName(activityName)
                    .vendorOrderNumber(vendorOrderNumber)
                    .invoiceNumber(invoiceNumber)
                    .build();
        };
    }

    @Override
    protected SiteActivityWorkExpenditureErrorReportDto buildErrorReportDto(BulkUploadErrorDto errorDto) {
        return SiteActivityWorkExpenditureErrorReportDto.builder()
                .rowNumber(errorDto.getRowNumber())
                .siteCode((String) errorDto.getRowData().get("Site Code"))
                .activityName((String) errorDto.getRowData().get("Activity Name"))
                .vendorOrderNumber((String) errorDto.getRowData().get("Vendor Order Number"))
                .invoiceNumber((String) errorDto.getRowData().get("Invoice Number"))
                .errorMessage(errorDto.getErrorMessage())
                .build();
    }

    @Override
    protected Class<SiteActivityWorkExpenditureErrorReportDto> getErrorReportDtoClass() {
        return SiteActivityWorkExpenditureErrorReportDto.class;
    }

    @Override
    @Transactional
    public SiteActivityWorkExpenditureResponseDto createSiteActivityWorkExpenditure(
            SiteActivityWorkExpenditureRequestDto requestDto) {
        
        log.info("Creating site activity work expenditure for site: {}, activity work: {}, expenditure: {}",
                requestDto.getSiteId(), requestDto.getActivityWorkId(), requestDto.getExpendituresInvoiceId());

        // Validate site exists
        Site site = siteRepository.findById(requestDto.getSiteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Site not found with id: " + requestDto.getSiteId()));

        // Validate activity work exists
        ActivityWork activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Activity work not found with id: " + requestDto.getActivityWorkId()));

        // Validate expenditures invoice exists
        ExpendituresInvoice expendituresInvoice = expendituresInvoiceRepository.findById(requestDto.getExpendituresInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expenditures invoice not found with id: " + requestDto.getExpendituresInvoiceId()));

        // Check for duplicate
        if (repository.existsBySiteIdAndActivityWorkIdAndExpendituresInvoiceId(
                requestDto.getSiteId(), requestDto.getActivityWorkId(), requestDto.getExpendituresInvoiceId())) {
            throw new IllegalStateException(
                    "This site-activity-work-expenditure combination already exists");
        }

        // Create entity
        SiteActivityWorkExpenditure entity = mapper.toEntity(requestDto, site, activityWork, expendituresInvoice);
        SiteActivityWorkExpenditure saved = repository.save(entity);

        // Fetch with details for response
        SiteActivityWorkExpenditure savedWithDetails = repository.findByIdWithDetails(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Site activity work expenditure not found after save"));

        log.info("Site activity work expenditure created successfully with ID: {}", saved.getId());
        return mapper.toDto(savedWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteActivityWorkExpenditureResponseDto> getAllSiteActivityWorkExpenditures(
            int page, int size, String sortBy, String sortOrder) {
        
        log.info("Fetching all site activity work expenditures with pagination: page={}, size={}", page, size);
        
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SiteActivityWorkExpenditure> expenditurePage = repository.findAllWithDetails(pageable);
        return expenditurePage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteActivityWorkExpenditureResponseDto> getExpendituresBySiteId(
            Long siteId, int page, int size, String sortBy, String sortOrder) {
        
        log.info("Fetching expenditures for site ID: {}", siteId);

        // Validate site exists
        if (!siteRepository.existsById(siteId)) {
            throw new ResourceNotFoundException("Site not found with id: " + siteId);
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SiteActivityWorkExpenditure> expenditurePage = repository.findBySiteId(siteId, pageable);
        return expenditurePage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteActivityWorkExpenditureResponseDto> getExpendituresByActivityWorkId(
            Long activityWorkId, int page, int size, String sortBy, String sortOrder) {
        
        log.info("Fetching expenditures for activity work ID: {}", activityWorkId);

        // Validate activity work exists
        if (!activityWorkRepository.existsById(activityWorkId)) {
            throw new ResourceNotFoundException("Activity work not found with id: " + activityWorkId);
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SiteActivityWorkExpenditure> expenditurePage = repository.findByActivityWorkId(activityWorkId, pageable);
        return expenditurePage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteActivityWorkExpenditureResponseDto> searchExpenditures(
            String searchTerm, int page, int size, String sortBy, String sortOrder) {
        
        log.info("Searching site activity work expenditures with keyword: {}", searchTerm);
        
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SiteActivityWorkExpenditure> expenditurePage = repository.searchExpenditures(searchTerm, pageable);
        return expenditurePage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public SiteActivityWorkExpenditureResponseDto getExpenditureById(Long id) {
        log.info("Fetching site activity work expenditure with ID: {}", id);
        
        SiteActivityWorkExpenditure expenditure = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Site activity work expenditure not found with id: " + id));
        
        return mapper.toDto(expenditure);
    }

    @Override
    @Transactional
    public SiteActivityWorkExpenditureResponseDto updateSiteActivityWorkExpenditure(
            Long id, SiteActivityWorkExpenditureRequestDto requestDto) {
        
        log.info("Updating site activity work expenditure with ID: {}", id);

        SiteActivityWorkExpenditure existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Site activity work expenditure not found with id: " + id));

        // Validate site exists
        Site site = siteRepository.findById(requestDto.getSiteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Site not found with id: " + requestDto.getSiteId()));

        // Validate activity work exists
        ActivityWork activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Activity work not found with id: " + requestDto.getActivityWorkId()));

        // Validate expenditures invoice exists
        ExpendituresInvoice expendituresInvoice = expendituresInvoiceRepository.findById(requestDto.getExpendituresInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expenditures invoice not found with id: " + requestDto.getExpendituresInvoiceId()));

        // Check for duplicate (excluding current record)
        boolean duplicateExists = repository.existsBySiteIdAndActivityWorkIdAndExpendituresInvoiceId(
                requestDto.getSiteId(), requestDto.getActivityWorkId(), requestDto.getExpendituresInvoiceId());
        
        if (duplicateExists && !existing.getSite().getId().equals(requestDto.getSiteId()) ||
            duplicateExists && !existing.getActivityWork().getId().equals(requestDto.getActivityWorkId()) ||
            duplicateExists && !existing.getExpendituresInvoice().getId().equals(requestDto.getExpendituresInvoiceId())) {
            throw new IllegalStateException(
                    "This site-activity-work-expenditure combination already exists");
        }

        // Update entity
        mapper.updateEntity(existing, requestDto, site, activityWork, expendituresInvoice);
        SiteActivityWorkExpenditure updated = repository.save(existing);

        // Fetch with details for response
        SiteActivityWorkExpenditure updatedWithDetails = repository.findByIdWithDetails(updated.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Site activity work expenditure not found after update"));

        log.info("Site activity work expenditure updated successfully with ID: {}", id);
        return mapper.toDto(updatedWithDetails);
    }

    @Override
    @Transactional
    public void deleteSiteActivityWorkExpenditure(Long id) {
        log.info("Deleting site activity work expenditure with ID: {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Site activity work expenditure not found with id: " + id);
        }

        repository.deleteById(id);
        log.info("Site activity work expenditure deleted successfully with ID: {}", id);
    }
}
