package com.eps.module.api.epsone.expenditures_invoice.service;

import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceBulkUploadDto;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceErrorReportDto;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceRequestDto;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceResponseDto;
import com.eps.module.api.epsone.expenditures_invoice.mapper.ExpendituresInvoiceMapper;
import com.eps.module.api.epsone.expenditures_invoice.processor.ExpendituresInvoiceBulkUploadProcessor;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.ExpendituresInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpendituresInvoiceServiceImpl extends BaseBulkUploadService<ExpendituresInvoiceBulkUploadDto, ExpendituresInvoice> implements ExpendituresInvoiceService {

    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final ExpendituresInvoiceMapper expendituresInvoiceMapper;
    private final CostItemRepository costItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final ManagedProjectRepository managedProjectRepository;
    private final ExpendituresInvoiceBulkUploadProcessor expendituresInvoiceBulkUploadProcessor;

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<ExpendituresInvoiceBulkUploadDto, ExpendituresInvoice> getProcessor() {
        return expendituresInvoiceBulkUploadProcessor;
    }

    @Override
    public Class<ExpendituresInvoiceBulkUploadDto> getBulkUploadDtoClass() {
        return ExpendituresInvoiceBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Expenditures Invoice";
    }

    @Override
    public List<ExpendituresInvoice> getAllEntitiesForExport() {
        return expendituresInvoiceRepository.findAllForExport();
    }

    @Override
    public Function<ExpendituresInvoice, ExpendituresInvoiceBulkUploadDto> getEntityToDtoMapper() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        return expendituresInvoice -> {
            String costItemName = expendituresInvoice.getCostItem() != null 
                    ? expendituresInvoice.getCostItem().getCostItemFor() : "";
            
            String invoiceNumber = expendituresInvoice.getInvoice() != null 
                    ? expendituresInvoice.getInvoice().getInvoiceNumber() : "";
            
            String projectCode = expendituresInvoice.getManagedProject() != null 
                    ? expendituresInvoice.getManagedProject().getProjectCode() : "";

            return ExpendituresInvoiceBulkUploadDto.builder()
                    .costItemName(costItemName)
                    .invoiceNumber(invoiceNumber)
                    .managedProjectCode(projectCode)
                    .incurredDate(expendituresInvoice.getIncurredDate() != null 
                            ? expendituresInvoice.getIncurredDate().format(dateFormatter) : "")
                    .description(expendituresInvoice.getDescription())
                    .build();
        };
    }

    @Override
    public ExpendituresInvoiceErrorReportDto buildErrorReportDto(BulkUploadErrorDto errorDto) {
        return ExpendituresInvoiceErrorReportDto.builder()
                .rowNumber(errorDto.getRowNumber())
                .costItemName((String) errorDto.getRowData().get("Cost Item Name"))
                .invoiceNumber((String) errorDto.getRowData().get("Invoice Number"))
                .managedProjectCode((String) errorDto.getRowData().get("Managed Project Code"))
                .incurredDate((String) errorDto.getRowData().get("Incurred Date"))
                .description((String) errorDto.getRowData().get("Description"))
                .error(errorDto.getErrorMessage())
                .build();
    }

    @Override
    public Class<ExpendituresInvoiceErrorReportDto> getErrorReportDtoClass() {
        return ExpendituresInvoiceErrorReportDto.class;
    }

    // ========== CRUD Methods ==========

    @Override
    @Transactional
    public ExpendituresInvoiceResponseDto createExpendituresInvoice(ExpendituresInvoiceRequestDto requestDto) {
        log.info("Creating expenditures invoice with cost item ID: {}, invoice ID: {}, project ID: {}",
                requestDto.getCostItemId(), requestDto.getInvoiceId(), requestDto.getManagedProjectId());

        // Validate cost item exists
        if (!costItemRepository.existsById(requestDto.getCostItemId())) {
            throw new ResourceNotFoundException("Cost item not found with ID: " + requestDto.getCostItemId());
        }

        // Validate invoice exists
        if (!invoiceRepository.existsById(requestDto.getInvoiceId())) {
            throw new ResourceNotFoundException("Invoice not found with ID: " + requestDto.getInvoiceId());
        }

        // Validate managed project exists
        if (!managedProjectRepository.existsById(requestDto.getManagedProjectId())) {
            throw new ResourceNotFoundException("Managed project not found with ID: " + requestDto.getManagedProjectId());
        }

        ExpendituresInvoice expendituresInvoice = expendituresInvoiceMapper.toEntity(requestDto);
        ExpendituresInvoice savedExpendituresInvoice = expendituresInvoiceRepository.save(expendituresInvoice);

        log.info("Successfully created expenditures invoice with ID: {}", savedExpendituresInvoice.getId());
        return expendituresInvoiceMapper.toResponseDto(savedExpendituresInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpendituresInvoiceResponseDto> getAllExpendituresInvoices(Pageable pageable) {
        log.info("Fetching all expenditures invoices with pagination: {}", pageable);
        Page<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.findAllWithDetails(pageable);
        return expendituresInvoices.map(expendituresInvoiceMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpendituresInvoiceResponseDto> searchExpendituresInvoices(String searchTerm, Pageable pageable) {
        log.info("Searching expenditures invoices with term: {}", searchTerm);
        Page<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.searchExpendituresInvoices(searchTerm, pageable);
        return expendituresInvoices.map(expendituresInvoiceMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpendituresInvoiceResponseDto getExpendituresInvoiceById(Long id) {
        log.info("Fetching expenditures invoice with ID: {}", id);
        ExpendituresInvoice expendituresInvoice = expendituresInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expenditures invoice not found with ID: " + id));
        return expendituresInvoiceMapper.toResponseDto(expendituresInvoice);
    }

    @Override
    @Transactional
    public ExpendituresInvoiceResponseDto updateExpendituresInvoice(Long id, ExpendituresInvoiceRequestDto requestDto) {
        log.info("Updating expenditures invoice with ID: {}", id);

        ExpendituresInvoice existingExpendituresInvoice = expendituresInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expenditures invoice not found with ID: " + id));

        // Validate cost item exists if changed
        if (!requestDto.getCostItemId().equals(existingExpendituresInvoice.getCostItem().getId())) {
            if (!costItemRepository.existsById(requestDto.getCostItemId())) {
                throw new ResourceNotFoundException("Cost item not found with ID: " + requestDto.getCostItemId());
            }
        }

        // Validate invoice exists if changed
        if (!requestDto.getInvoiceId().equals(existingExpendituresInvoice.getInvoice().getId())) {
            if (!invoiceRepository.existsById(requestDto.getInvoiceId())) {
                throw new ResourceNotFoundException("Invoice not found with ID: " + requestDto.getInvoiceId());
            }
        }

        // Validate managed project exists if changed
        if (!requestDto.getManagedProjectId().equals(existingExpendituresInvoice.getManagedProject().getId())) {
            if (!managedProjectRepository.existsById(requestDto.getManagedProjectId())) {
                throw new ResourceNotFoundException("Managed project not found with ID: " + requestDto.getManagedProjectId());
            }
        }

        expendituresInvoiceMapper.updateEntityFromDto(requestDto, existingExpendituresInvoice);
        ExpendituresInvoice updatedExpendituresInvoice = expendituresInvoiceRepository.save(existingExpendituresInvoice);

        log.info("Successfully updated expenditures invoice with ID: {}", id);
        return expendituresInvoiceMapper.toResponseDto(updatedExpendituresInvoice);
    }

    @Override
    @Transactional
    public void deleteExpendituresInvoice(Long id) {
        log.info("Deleting expenditures invoice with ID: {}", id);
        if (!expendituresInvoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expenditures invoice not found with ID: " + id);
        }
        expendituresInvoiceRepository.deleteById(id);
        log.info("Successfully deleted expenditures invoice with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpendituresInvoiceResponseDto> getExpendituresInvoicesByProjectId(Long projectId, Pageable pageable) {
        log.info("Fetching expenditures invoices for project ID: {}", projectId);
        Page<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.findByManagedProjectId(projectId, pageable);
        return expendituresInvoices.map(expendituresInvoiceMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpendituresInvoiceResponseDto> getExpendituresInvoicesByInvoiceId(Long invoiceId) {
        log.info("Fetching expenditures for invoice ID: {}", invoiceId);
        List<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.findByInvoiceId(invoiceId);
        return expendituresInvoices.stream()
                .map(expendituresInvoiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpendituresInvoiceResponseDto> getAllExpendituresInvoicesList() {
        log.info("Fetching all expenditures invoices as list");
        List<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.findAllWithDetailsList();
        return expendituresInvoices.stream()
                .map(expendituresInvoiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
