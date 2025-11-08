package com.eps.module.api.epsone.payment_details.service;

import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsBulkUploadDto;
import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsErrorReportDto;
import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsRequestDto;
import com.eps.module.api.epsone.payment_details.dto.PaymentDetailsResponseDto;
import com.eps.module.api.epsone.payment_details.mapper.PaymentDetailsMapper;
import com.eps.module.api.epsone.payment_details.processor.PaymentDetailsBulkUploadProcessor;
import com.eps.module.api.epsone.payment_details.repository.PaymentDetailsRepository;
import com.eps.module.api.epsone.payment_method.repository.PaymentMethodRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.payment.PaymentDetails;
import com.eps.module.payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentDetailsServiceImpl extends BaseBulkUploadService<PaymentDetailsBulkUploadDto, PaymentDetails>
        implements PaymentDetailsService {

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentDetailsMapper paymentDetailsMapper;
    private final PaymentDetailsBulkUploadProcessor paymentDetailsBulkUploadProcessor;

    @Override
    @Transactional
    public PaymentDetailsResponseDto createPaymentDetails(PaymentDetailsRequestDto requestDto) {
        log.info("Creating payment details for amount: {}", requestDto.getPaymentAmount());

        // Validate that payment method exists
        PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDto.getPaymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment method not found with ID: " + requestDto.getPaymentMethodId()));

        PaymentDetails paymentDetails = paymentDetailsMapper.toEntity(requestDto);
        paymentDetails.setPaymentMethod(paymentMethod);
        
        PaymentDetails savedPaymentDetails = paymentDetailsRepository.save(paymentDetails);

        log.info("Payment details created successfully with ID: {}", savedPaymentDetails.getId());
        return paymentDetailsMapper.toResponseDto(savedPaymentDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDetailsResponseDto> getAllPaymentDetails(Pageable pageable) {
        log.info("Fetching all payment details with pagination: {}", pageable);
        Page<PaymentDetails> paymentDetails = paymentDetailsRepository.findAll(pageable);
        return paymentDetails.map(paymentDetailsMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDetailsResponseDto> searchPaymentDetails(String searchTerm, Pageable pageable) {
        log.info("Searching payment details with term: {}", searchTerm);
        Page<PaymentDetails> paymentDetails = paymentDetailsRepository.searchPaymentDetails(searchTerm, pageable);
        return paymentDetails.map(paymentDetailsMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDetailsResponseDto> getPaymentDetailsList() {
        log.info("Fetching all payment details as list");
        return paymentDetailsRepository.findAll().stream()
                .map(paymentDetailsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDetailsResponseDto getPaymentDetailsById(Long id) {
        log.info("Fetching payment details with ID: {}", id);
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment details not found with ID: " + id));
        return paymentDetailsMapper.toResponseDto(paymentDetails);
    }

    @Override
    @Transactional
    public PaymentDetailsResponseDto updatePaymentDetails(Long id, PaymentDetailsRequestDto requestDto) {
        log.info("Updating payment details with ID: {}", id);

        PaymentDetails paymentDetails = paymentDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment details not found with ID: " + id));

        // Validate that payment method exists if it's being updated
        if (requestDto.getPaymentMethodId() != null) {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDto.getPaymentMethodId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Payment method not found with ID: " + requestDto.getPaymentMethodId()));
            paymentDetails.setPaymentMethod(paymentMethod);
        }

        paymentDetailsMapper.updateEntityFromDto(requestDto, paymentDetails);
        PaymentDetails updatedPaymentDetails = paymentDetailsRepository.save(paymentDetails);

        log.info("Payment details updated successfully with ID: {}", id);
        return paymentDetailsMapper.toResponseDto(updatedPaymentDetails);
    }

    @Override
    @Transactional
    public void deletePaymentDetails(Long id) {
        log.info("Deleting payment details with ID: {}", id);

        if (!paymentDetailsRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment details not found with ID: " + id);
        }

        paymentDetailsRepository.deleteById(id);
        log.info("Payment details deleted successfully with ID: {}", id);
    }

    @Override
    protected BulkUploadProcessor<PaymentDetailsBulkUploadDto, PaymentDetails> getProcessor() {
        return paymentDetailsBulkUploadProcessor;
    }

    @Override
    public Class<PaymentDetailsBulkUploadDto> getBulkUploadDtoClass() {
        return PaymentDetailsBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "PaymentDetails";
    }

    @Override
    public List<PaymentDetails> getAllEntitiesForExport() {
        return paymentDetailsRepository.findAllForExport();
    }

    @Override
    public Function<PaymentDetails, PaymentDetailsBulkUploadDto> getEntityToDtoMapper() {
        return entity -> PaymentDetailsBulkUploadDto.builder()
                .paymentMethodName(entity.getPaymentMethod() != null ? entity.getPaymentMethod().getMethodName() : null)
                .paymentDate(entity.getPaymentDate())
                .paymentAmount(entity.getPaymentAmount())
                .transactionNumber(entity.getTransactionNumber())
                .vpa(entity.getVpa())
                .beneficiaryName(entity.getBeneficiaryName())
                .beneficiaryAccountNumber(entity.getBeneficiaryAccountNumber())
                .paymentRemarks(entity.getPaymentRemarks())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        PaymentDetailsErrorReportDto.PaymentDetailsErrorReportDtoBuilder builder =
                PaymentDetailsErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorType(error.getErrorType())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.paymentMethodName((String) error.getRowData().get("Payment Method Name"))
                    .transactionNumber((String) error.getRowData().get("Transaction Number"))
                    .vpa((String) error.getRowData().get("VPA"))
                    .beneficiaryName((String) error.getRowData().get("Beneficiary Name"))
                    .beneficiaryAccountNumber((String) error.getRowData().get("Beneficiary Account Number"))
                    .paymentRemarks((String) error.getRowData().get("Payment Remarks"));
            
            // Handle Payment Date using switch expression
            Object paymentDateObj = error.getRowData().get("Payment Date");
            switch (paymentDateObj) {
                case LocalDate date -> builder.paymentDate(date);
                case String dateStr when !dateStr.isEmpty() -> {
                    try {
                        builder.paymentDate(LocalDate.parse(dateStr));
                    } catch (Exception e) {
                        // If parsing fails, leave it null
                    }
                }
                case null, default -> {} // Leave it null
            }
            
            // Handle Payment Amount using switch expression
            Object paymentAmountObj = error.getRowData().get("Payment Amount");
            switch (paymentAmountObj) {
                case BigDecimal amount -> builder.paymentAmount(amount);
                case Number num -> builder.paymentAmount(new BigDecimal(num.toString()));
                case String amountStr when !amountStr.isEmpty() -> {
                    try {
                        builder.paymentAmount(new BigDecimal(amountStr));
                    } catch (Exception e) {
                        // If parsing fails, leave it null
                    }
                }
                case null, default -> {} // Leave it null
            }
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return PaymentDetailsErrorReportDto.class;
    }
}
