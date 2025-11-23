package com.eps.module.api.epsone.voucher.service;

import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.api.epsone.voucher.bulk.VoucherBulkUploadProcessor;
import com.eps.module.api.epsone.voucher.constant.VoucherErrorMessages;
import com.eps.module.api.epsone.voucher.dto.VoucherBulkUploadDto;
import com.eps.module.api.epsone.voucher.dto.VoucherErrorReportDto;
import com.eps.module.api.epsone.voucher.dto.VoucherRequestDto;
import com.eps.module.api.epsone.voucher.dto.VoucherResponseDto;
import com.eps.module.api.epsone.voucher.mapper.VoucherMapper;
import com.eps.module.api.epsone.voucher.repository.VoucherRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.payment.Voucher;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PaymentDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherServiceImpl extends BaseBulkUploadService<VoucherBulkUploadDto, Voucher> implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final PayeeRepository payeeRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final VoucherBulkUploadProcessor processor;

    @Override
    @Transactional
    public VoucherResponseDto createVoucher(VoucherRequestDto requestDto) {
        log.info("Creating new voucher: {}", requestDto.getVoucherNumber());

        // Validate unique voucher number
        if (voucherRepository.existsByVoucherNumberIgnoreCase(requestDto.getVoucherNumber())) {
            throw new ConflictException(String.format(VoucherErrorMessages.VOUCHER_NUMBER_EXISTS, requestDto.getVoucherNumber()));
        }

        // Validate payee exists
        Payee payee = payeeRepository.findById(requestDto.getPayeeId())
                .orElseThrow(() -> new ResourceNotFoundException(VoucherErrorMessages.PAYEE_NOT_FOUND_ID + requestDto.getPayeeId()));

        // Validate payment details if provided
        if (requestDto.getPaymentDetailsId() != null) {
            PaymentDetails paymentDetails = paymentDetailsRepository.findById(requestDto.getPaymentDetailsId())
                    .orElseThrow(() -> new ResourceNotFoundException(VoucherErrorMessages.PAYMENT_DETAILS_NOT_FOUND_ID + requestDto.getPaymentDetailsId()));
        }

        Voucher voucher = voucherMapper.toEntity(requestDto);
        voucher.setPayee(payee);
        
        Voucher savedVoucher = voucherRepository.save(voucher);
        log.info("Voucher created successfully with ID: {}", savedVoucher.getId());

        return voucherMapper.toResponseDto(savedVoucher);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherResponseDto> getAllVouchers(Pageable pageable) {
        log.info("Fetching all vouchers with pagination: {}", pageable);
        Page<Voucher> vouchers = voucherRepository.findAll(pageable);
        return vouchers.map(voucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherResponseDto> searchVouchers(String searchTerm, Pageable pageable) {
        log.info("Searching vouchers with term: {}", searchTerm);
        Page<Voucher> vouchers = voucherRepository.searchVouchers(searchTerm, pageable);
        return vouchers.map(voucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoucherResponseDto> getVouchersList() {
        log.info("Fetching all vouchers as list");
        return voucherRepository.findAll().stream()
                .map(voucherMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherResponseDto> getVouchersByPayee(Long payeeId, Pageable pageable) {
        log.info("Fetching vouchers for payee ID: {}", payeeId);
        
        // Validate payee exists
        if (!payeeRepository.existsById(payeeId)) {
            throw new ResourceNotFoundException(VoucherErrorMessages.PAYEE_NOT_FOUND_ID + payeeId);
        }

        Page<Voucher> vouchers = voucherRepository.findByPayeeId(payeeId, pageable);
        return vouchers.map(voucherMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherResponseDto getVoucherById(Long id) {
        log.info("Fetching voucher by ID: {}", id);
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VoucherErrorMessages.VOUCHER_NOT_FOUND_ID + id));
        return voucherMapper.toResponseDto(voucher);
    }

    @Override
    @Transactional
    public VoucherResponseDto updateVoucher(Long id, VoucherRequestDto requestDto) {
        log.info("Updating voucher ID: {}", id);

        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VoucherErrorMessages.VOUCHER_NOT_FOUND_ID + id));

        // Validate unique voucher number if changed
        if (!voucher.getVoucherNumber().equalsIgnoreCase(requestDto.getVoucherNumber()) &&
            voucherRepository.existsByVoucherNumberIgnoreCaseAndIdNot(requestDto.getVoucherNumber(), id)) {
            throw new ConflictException(String.format(VoucherErrorMessages.VOUCHER_NUMBER_EXISTS, requestDto.getVoucherNumber()));
        }

        // Validate payee exists
        Payee payee = payeeRepository.findById(requestDto.getPayeeId())
                .orElseThrow(() -> new ResourceNotFoundException(VoucherErrorMessages.PAYEE_NOT_FOUND_ID + requestDto.getPayeeId()));

        // Validate payment details if provided
        if (requestDto.getPaymentDetailsId() != null) {
            PaymentDetails paymentDetails = paymentDetailsRepository.findById(requestDto.getPaymentDetailsId())
                    .orElseThrow(() -> new ResourceNotFoundException(VoucherErrorMessages.PAYMENT_DETAILS_NOT_FOUND_ID + requestDto.getPaymentDetailsId()));
        }

        voucherMapper.updateEntityFromDto(requestDto, voucher);
        voucher.setPayee(payee);

        Voucher updatedVoucher = voucherRepository.save(voucher);
        log.info("Voucher updated successfully: {}", id);

        return voucherMapper.toResponseDto(updatedVoucher);
    }

    @Override
    @Transactional
    public VoucherResponseDto updatePaymentStatus(Long id, String paymentStatus) {
        log.info("Updating payment status for voucher ID: {} to {}", id, paymentStatus);

        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VoucherErrorMessages.VOUCHER_NOT_FOUND_ID + id));

        voucher.setPaymentStatus(paymentStatus);
        Voucher updatedVoucher = voucherRepository.save(voucher);

        log.info("Payment status updated successfully for voucher ID: {}", id);
        return voucherMapper.toResponseDto(updatedVoucher);
    }

    @Override
    @Transactional
    public void deleteVoucher(Long id) {
        log.info("Deleting voucher ID: {}", id);

        if (!voucherRepository.existsById(id)) {
            throw new ResourceNotFoundException(VoucherErrorMessages.VOUCHER_NOT_FOUND_ID + id);
        }

        voucherRepository.deleteById(id);
        log.info("Voucher deleted successfully: {}", id);
    }

    // ==================== Bulk Upload Methods ====================

    @Override
    protected BulkUploadProcessor<VoucherBulkUploadDto, Voucher> getProcessor() {
        return processor;
    }

    @Override
    public Class<VoucherBulkUploadDto> getBulkUploadDtoClass() {
        return VoucherBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Voucher";
    }

    @Override
    public List<Voucher> getAllEntitiesForExport() {
        return voucherRepository.findAllForExport();
    }

    @Override
    public Function<Voucher, VoucherBulkUploadDto> getEntityToDtoMapper() {
        return entity -> VoucherBulkUploadDto.builder()
                .voucherNumber(entity.getVoucherNumber())
                .voucherDate(entity.getVoucherDate() != null ? entity.getVoucherDate().toString() : null)
                .orderNumber(entity.getOrderNumber())
                .payeeName(entity.getPayee() != null && entity.getPayee().getPayeeDetails() != null ?
                        entity.getPayee().getPayeeDetails().getPayeeName() : null)
                .paymentTransactionNumber(entity.getPaymentDetails() != null ?
                        entity.getPaymentDetails().getTransactionNumber() : null)
                .paymentDueDate(entity.getPaymentDueDate() != null ? entity.getPaymentDueDate().toString() : null)
                .paymentStatus(entity.getPaymentStatus())
                .quantity(entity.getQuantity() != null ? entity.getQuantity().toString() : null)
                .unit(entity.getUnit())
                .unitPrice(entity.getUnitPrice() != null ? entity.getUnitPrice().toString() : null)
                .taxCgst(entity.getTaxCgst() != null ? entity.getTaxCgst().toString() : null)
                .taxSgst(entity.getTaxSgst() != null ? entity.getTaxSgst().toString() : null)
                .taxIgst(entity.getTaxIgst() != null ? entity.getTaxIgst().toString() : null)
                .amount1(entity.getAmount1() != null ? entity.getAmount1().toString() : null)
                .amount2(entity.getAmount2() != null ? entity.getAmount2().toString() : null)
                .discountPercentage(entity.getDiscountPercentage() != null ? entity.getDiscountPercentage().toString() : null)
                .discountAmount(entity.getDiscountAmount() != null ? entity.getDiscountAmount().toString() : null)
                .finalAmount(entity.getFinalAmount() != null ? entity.getFinalAmount().toString() : null)
                .remarks(entity.getRemarks())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        Map<String, Object> rowData = error.getRowData();
        return VoucherErrorReportDto.builder()
                .rowNumber(error.getRowNumber())
                .voucherNumber(getStringValue(rowData, "voucherNumber"))
                .voucherDate(getStringValue(rowData, "voucherDate"))
                .orderNumber(getStringValue(rowData, "orderNumber"))
                .payeeName(getStringValue(rowData, "payeeName"))
                .paymentTransactionNumber(getStringValue(rowData, "paymentTransactionNumber"))
                .paymentDueDate(getStringValue(rowData, "paymentDueDate"))
                .paymentStatus(getStringValue(rowData, "paymentStatus"))
                .quantity(getStringValue(rowData, "quantity"))
                .unit(getStringValue(rowData, "unit"))
                .unitPrice(getStringValue(rowData, "unitPrice"))
                .taxCgst(getStringValue(rowData, "taxCgst"))
                .taxSgst(getStringValue(rowData, "taxSgst"))
                .taxIgst(getStringValue(rowData, "taxIgst"))
                .amount1(getStringValue(rowData, "amount1"))
                .amount2(getStringValue(rowData, "amount2"))
                .discountPercentage(getStringValue(rowData, "discountPercentage"))
                .discountAmount(getStringValue(rowData, "discountAmount"))
                .finalAmount(getStringValue(rowData, "finalAmount"))
                .remarks(getStringValue(rowData, "remarks"))
                .errorType(error.getErrorType())
                .errorMessage(error.getErrorMessage())
                .build();
    }

    @Override
    public Class<VoucherErrorReportDto> getErrorReportDtoClass() {
        return VoucherErrorReportDto.class;
    }

    private String getStringValue(Map<String, Object> rowData, String key) {
        Object value = rowData.get(key);
        return value != null ? value.toString() : null;
    }
}
