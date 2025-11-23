package com.eps.module.api.epsone.payee.service;

import com.eps.module.api.epsone.invoice.repository.InvoiceRepository;
import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.payee.constant.PayeeErrorMessages;
import com.eps.module.api.epsone.payee.dto.PayeeBulkUploadDto;
import com.eps.module.api.epsone.payee.dto.PayeeErrorReportDto;
import com.eps.module.api.epsone.payee.dto.PayeeRequestDto;
import com.eps.module.api.epsone.payee.dto.PayeeResponseDto;
import com.eps.module.api.epsone.payee.mapper.PayeeMapper;
import com.eps.module.api.epsone.payee.processor.PayeeBulkUploadProcessor;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.payee_type.repository.PayeeTypeRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.BadRequestException;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PayeeDetails;
import com.eps.module.payment.PayeeType;
import com.eps.module.vendor.Landlord;
import com.eps.module.vendor.Vendor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayeeServiceImpl extends BaseBulkUploadService<PayeeBulkUploadDto, Payee> implements PayeeService {

    private final PayeeRepository payeeRepository;
    private final PayeeTypeRepository payeeTypeRepository;
    private final PayeeDetailsRepository payeeDetailsRepository;
    private final VendorRepository vendorRepository;
    private final InvoiceRepository invoiceRepository;
    private final VoucherRepository voucherRepository;
    private final LandlordRepository landlordRepository;
    private final PayeeMapper payeeMapper;
    private final PayeeBulkUploadProcessor payeeBulkUploadProcessor;

    @Override
    @Transactional
    public PayeeResponseDto createPayee(PayeeRequestDto requestDto) {
        log.info("Creating new payee with payeeTypeId: {}, payeeDetailsId: {}", 
                requestDto.getPayeeTypeId(), requestDto.getPayeeDetailsId());

        // Validate payee type exists
        PayeeType payeeType = payeeTypeRepository.findById(requestDto.getPayeeTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        PayeeErrorMessages.PAYEE_TYPE_NOT_FOUND + requestDto.getPayeeTypeId()));

        // Validate payee details exists
        PayeeDetails payeeDetails = payeeDetailsRepository.findById(requestDto.getPayeeDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        PayeeErrorMessages.PAYEE_DETAILS_NOT_FOUND + requestDto.getPayeeDetailsId()));

        // Check if payee details already used
        if (payeeRepository.countByPayeeDetailsId(requestDto.getPayeeDetailsId()) > 0) {
            throw new ConflictException(
                    String.format(PayeeErrorMessages.PAYEE_DETAILS_ALREADY_ASSIGNED, requestDto.getPayeeDetailsId()));
        }

        // Validate vendor if provided
        Vendor vendor = null;
        if (requestDto.getVendorId() != null) {
            vendor = vendorRepository.findById(requestDto.getVendorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            PayeeErrorMessages.VENDOR_NOT_FOUND + requestDto.getVendorId()));
        }

        // Validate landlord if provided
        Landlord landlord = null;
        if (requestDto.getLandlordId() != null) {
            landlord = landlordRepository.findById(requestDto.getLandlordId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            PayeeErrorMessages.LANDLORD_NOT_FOUND + requestDto.getLandlordId()));
        }

        Payee payee = payeeMapper.toEntity(requestDto, payeeType, payeeDetails, vendor, landlord);
        Payee savedPayee = payeeRepository.save(payee);

        log.info("Payee created successfully with id: {}", savedPayee.getId());
        return payeeMapper.toDto(savedPayee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeResponseDto> getAllPayees(Pageable pageable) {
        log.info("Fetching all payees with pagination");
        Page<Payee> payees = payeeRepository.findAllWithDetails(pageable);
        return payees.map(payeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeResponseDto> searchPayees(String searchTerm, Pageable pageable) {
        log.info("Searching payees with term: {}", searchTerm);
        Page<Payee> payees = payeeRepository.searchPayees(searchTerm, pageable);
        return payees.map(payeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayeeResponseDto> getPayeesList() {
        log.info("Fetching all payees as list");
        List<Payee> payees = payeeRepository.findAll();
        return payees.stream()
                .map(payeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PayeeResponseDto getPayeeById(Long id) {
        log.info("Fetching payee by id: {}", id);
        Payee payee = payeeRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(PayeeErrorMessages.PAYEE_NOT_FOUND + id));
        return payeeMapper.toDto(payee);
    }

    @Override
    @Transactional
    public PayeeResponseDto updatePayee(Long id, PayeeRequestDto requestDto) {
        log.info("Updating payee with id: {}", id);

        Payee payee = payeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PayeeErrorMessages.PAYEE_NOT_FOUND + id));

        // Validate payee type exists
        PayeeType payeeType = payeeTypeRepository.findById(requestDto.getPayeeTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        PayeeErrorMessages.PAYEE_TYPE_NOT_FOUND + requestDto.getPayeeTypeId()));

        // Validate payee details exists
        PayeeDetails payeeDetails = payeeDetailsRepository.findById(requestDto.getPayeeDetailsId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        PayeeErrorMessages.PAYEE_DETAILS_NOT_FOUND + requestDto.getPayeeDetailsId()));

        // Check if payee details already used by another payee
        long count = payeeRepository.countByPayeeDetailsId(requestDto.getPayeeDetailsId());
        if (count > 0 && !payee.getPayeeDetails().getId().equals(requestDto.getPayeeDetailsId())) {
            throw new ConflictException(
                    String.format(PayeeErrorMessages.PAYEE_DETAILS_ALREADY_ASSIGNED, requestDto.getPayeeDetailsId()));
        }

        // Validate vendor if provided
        Vendor vendor = null;
        if (requestDto.getVendorId() != null) {
            vendor = vendorRepository.findById(requestDto.getVendorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            PayeeErrorMessages.VENDOR_NOT_FOUND + requestDto.getVendorId()));
        }

        // Validate landlord if provided
        Landlord landlord = null;
        if (requestDto.getLandlordId() != null) {
            landlord = landlordRepository.findById(requestDto.getLandlordId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            PayeeErrorMessages.LANDLORD_NOT_FOUND + requestDto.getLandlordId()));
        }

        payeeMapper.updateEntity(payee, requestDto, payeeType, payeeDetails, vendor, landlord);
        Payee updatedPayee = payeeRepository.save(payee);

        log.info("Payee updated successfully with id: {}", updatedPayee.getId());
        return payeeMapper.toDto(updatedPayee);
    }

    @Override
    @Transactional
    public void deletePayee(Long id) {
        log.info("Deleting payee with id: {}", id);

        if (!payeeRepository.existsById(id)) {
            throw new ResourceNotFoundException(PayeeErrorMessages.PAYEE_NOT_FOUND + id);
        }

        // Check if payee is used in invoices
        var invoices = invoiceRepository.findByPayeeId(id);
        if (!invoices.isEmpty()) {
            throw new BadRequestException(String.format(PayeeErrorMessages.CANNOT_DELETE_PAYEE_INVOICE_REF, invoices.size()));
        }

        // Check if payee is used in vouchers
        var vouchers = voucherRepository.findByPayeeId(id, Pageable.unpaged());
        if (vouchers.hasContent()) {
            throw new BadRequestException(String.format(PayeeErrorMessages.CANNOT_DELETE_PAYEE_VOUCHER_REF, vouchers.getTotalElements()));
        }

        payeeRepository.deleteById(id);
        log.info("Payee deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPayees() {
        return payeeRepository.count();
    }

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<PayeeBulkUploadDto, Payee> getProcessor() {
        return payeeBulkUploadProcessor;
    }

    @Override
    public Class<PayeeBulkUploadDto> getBulkUploadDtoClass() {
        return PayeeBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Payee";
    }

    @Override
    public List<Payee> getAllEntitiesForExport() {
        return payeeRepository.findAllForExport();
    }

    @Override
    public Function<Payee, PayeeBulkUploadDto> getEntityToDtoMapper() {
        return entity -> {
            String vendorContactNumber = null;
            if (entity.getVendor() != null && entity.getVendor().getVendorDetails() != null) {
                vendorContactNumber = entity.getVendor().getVendorDetails().getContactNumber();
            }

            String landlordContactNumber = null;
            if (entity.getLandlord() != null && entity.getLandlord().getLandlordDetails() != null) {
                landlordContactNumber = entity.getLandlord().getLandlordDetails().getContactNumber();
            }

            return PayeeBulkUploadDto.builder()
                    .payeeType(entity.getPayeeType() != null ? entity.getPayeeType().getPayeeType() : "")
                    .payeeName(entity.getPayeeDetails() != null ? entity.getPayeeDetails().getPayeeName() : "")
                    .vendorContactNumber(vendorContactNumber)
                    .landlordContactNumber(landlordContactNumber)
                    .build();
        };
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        PayeeErrorReportDto.PayeeErrorReportDtoBuilder builder =
                PayeeErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorType(error.getErrorType())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.payeeType((String) error.getRowData().get("Payee Type"))
                    .payeeName((String) error.getRowData().get("Payee Name"))
                    .vendorContactNumber((String) error.getRowData().get("Vendor Contact Number"))
                    .landlordContactNumber((String) error.getRowData().get("Landlord Contact Number"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return PayeeErrorReportDto.class;
    }
}
