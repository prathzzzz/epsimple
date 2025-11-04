package com.eps.module.api.epsone.expenditures_invoice.service;

import com.eps.module.api.epsone.cost_item.repository.CostItemRepository;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceRequestDto;
import com.eps.module.api.epsone.expenditures_invoice.dto.ExpendituresInvoiceResponseDto;
import com.eps.module.api.epsone.expenditures_invoice.mapper.ExpendituresInvoiceMapper;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.managed_project.repository.ManagedProjectRepository;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.ExpendituresInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpendituresInvoiceService {

    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final ExpendituresInvoiceMapper expendituresInvoiceMapper;
    private final CostItemRepository costItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final ManagedProjectRepository managedProjectRepository;

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

    @Transactional(readOnly = true)
    public Page<ExpendituresInvoiceResponseDto> getAllExpendituresInvoices(Pageable pageable) {
        log.info("Fetching all expenditures invoices with pagination: {}", pageable);
        Page<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.findAllWithDetails(pageable);
        return expendituresInvoices.map(expendituresInvoiceMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ExpendituresInvoiceResponseDto> searchExpendituresInvoices(String searchTerm, Pageable pageable) {
        log.info("Searching expenditures invoices with term: {}", searchTerm);
        Page<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.searchExpendituresInvoices(searchTerm, pageable);
        return expendituresInvoices.map(expendituresInvoiceMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public ExpendituresInvoiceResponseDto getExpendituresInvoiceById(Long id) {
        log.info("Fetching expenditures invoice with ID: {}", id);
        ExpendituresInvoice expendituresInvoice = expendituresInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expenditures invoice not found with ID: " + id));
        return expendituresInvoiceMapper.toResponseDto(expendituresInvoice);
    }

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

    @Transactional
    public void deleteExpendituresInvoice(Long id) {
        log.info("Deleting expenditures invoice with ID: {}", id);
        if (!expendituresInvoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expenditures invoice not found with ID: " + id);
        }
        expendituresInvoiceRepository.deleteById(id);
        log.info("Successfully deleted expenditures invoice with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<ExpendituresInvoiceResponseDto> getExpendituresInvoicesByProjectId(Long projectId, Pageable pageable) {
        log.info("Fetching expenditures invoices for project ID: {}", projectId);
        Page<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.findByManagedProjectId(projectId, pageable);
        return expendituresInvoices.map(expendituresInvoiceMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public List<ExpendituresInvoiceResponseDto> getExpendituresInvoicesByInvoiceId(Long invoiceId) {
        log.info("Fetching expenditures for invoice ID: {}", invoiceId);
        List<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.findByInvoiceId(invoiceId);
        return expendituresInvoices.stream()
                .map(expendituresInvoiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpendituresInvoiceResponseDto> getAllExpendituresInvoicesList() {
        log.info("Fetching all expenditures invoices as list");
        List<ExpendituresInvoice> expendituresInvoices = expendituresInvoiceRepository.findAllWithDetailsList();
        return expendituresInvoices.stream()
                .map(expendituresInvoiceMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
