package com.eps.module.api.epsone.payment_method.service;

import com.eps.module.api.epsone.payment_method.constant.PaymentMethodErrorMessages;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodBulkUploadDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodErrorReportDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodRequestDto;
import com.eps.module.api.epsone.payment_method.dto.PaymentMethodResponseDto;
import com.eps.module.api.epsone.payment_method.mapper.PaymentMethodMapper;
import com.eps.module.api.epsone.payment_method.processor.PaymentMethodBulkUploadProcessor;
import com.eps.module.api.epsone.payment_method.repository.PaymentMethodRepository;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodServiceImpl extends BaseBulkUploadService<PaymentMethodBulkUploadDto, PaymentMethod>
        implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;
    private final PaymentMethodBulkUploadProcessor paymentMethodBulkUploadProcessor;

    @Override
    @Transactional
    public PaymentMethodResponseDto createPaymentMethod(PaymentMethodRequestDto requestDto) {
        log.info("Creating payment method: {}", requestDto.getMethodName());

        if (paymentMethodRepository.existsByMethodNameIgnoreCase(requestDto.getMethodName())) {
            throw new ConflictException(String.format(PaymentMethodErrorMessages.PAYMENT_METHOD_EXISTS, requestDto.getMethodName()));
        }

        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(requestDto);
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        log.info("Payment method created successfully with ID: {}", savedPaymentMethod.getId());
        return paymentMethodMapper.toResponseDto(savedPaymentMethod);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodResponseDto> getAllPaymentMethods(Pageable pageable) {
        log.info("Fetching all payment methods with pagination: {}", pageable);
        Page<PaymentMethod> paymentMethods = paymentMethodRepository.findAll(pageable);
        return paymentMethods.map(paymentMethodMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodResponseDto> searchPaymentMethods(String searchTerm, Pageable pageable) {
        log.info("Searching payment methods with term: {}", searchTerm);
        Page<PaymentMethod> paymentMethods = paymentMethodRepository.searchPaymentMethods(searchTerm, pageable);
        return paymentMethods.map(paymentMethodMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodResponseDto> getPaymentMethodsList() {
        log.info("Fetching all payment methods as list");
        return paymentMethodRepository.findAll().stream()
                .map(paymentMethodMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethodResponseDto getPaymentMethodById(Long id) {
        log.info("Fetching payment method with ID: {}", id);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentMethodErrorMessages.PAYMENT_METHOD_NOT_FOUND_ID + id));
        return paymentMethodMapper.toResponseDto(paymentMethod);
    }

    @Override
    @Transactional
    public PaymentMethodResponseDto updatePaymentMethod(Long id, PaymentMethodRequestDto requestDto) {
        log.info("Updating payment method with ID: {}", id);

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PaymentMethodErrorMessages.PAYMENT_METHOD_NOT_FOUND_ID + id));

        if (paymentMethodRepository.existsByMethodNameAndIdNot(requestDto.getMethodName(), id)) {
            throw new ConflictException(String.format(PaymentMethodErrorMessages.PAYMENT_METHOD_EXISTS, requestDto.getMethodName()));
        }

        paymentMethodMapper.updateEntityFromDto(requestDto, paymentMethod);
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        log.info("Payment method updated successfully with ID: {}", id);
        return paymentMethodMapper.toResponseDto(updatedPaymentMethod);
    }

    @Override
    @Transactional
    public void deletePaymentMethod(Long id) {
        log.info("Deleting payment method with ID: {}", id);

        if (!paymentMethodRepository.existsById(id)) {
            throw new ResourceNotFoundException(PaymentMethodErrorMessages.PAYMENT_METHOD_NOT_FOUND_ID + id);
        }

        paymentMethodRepository.deleteById(id);
        log.info("Payment method deleted successfully with ID: {}", id);
    }

    @Override
    protected BulkUploadProcessor<PaymentMethodBulkUploadDto, PaymentMethod> getProcessor() {
        return paymentMethodBulkUploadProcessor;
    }

    @Override
    public Class<PaymentMethodBulkUploadDto> getBulkUploadDtoClass() {
        return PaymentMethodBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "PaymentMethod";
    }

    @Override
    public List<PaymentMethod> getAllEntitiesForExport() {
        return paymentMethodRepository.findAllForExport();
    }

    @Override
    public java.util.function.Function<PaymentMethod, PaymentMethodBulkUploadDto> getEntityToDtoMapper() {
        return entity -> PaymentMethodBulkUploadDto.builder()
                .methodName(entity.getMethodName())
                .description(entity.getDescription())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        PaymentMethodErrorReportDto.PaymentMethodErrorReportDtoBuilder builder =
                PaymentMethodErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorType(error.getErrorType())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.methodName((String) error.getRowData().get("Method Name"))
                    .description((String) error.getRowData().get("Description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return PaymentMethodErrorReportDto.class;
    }
}
