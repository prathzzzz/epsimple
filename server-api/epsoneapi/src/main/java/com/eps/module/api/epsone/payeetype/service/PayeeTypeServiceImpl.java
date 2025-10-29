package com.eps.module.api.epsone.payeetype.service;

import com.eps.module.api.epsone.payeetype.dto.PayeeTypeRequestDto;
import com.eps.module.api.epsone.payeetype.dto.PayeeTypeResponseDto;
import com.eps.module.api.epsone.payeetype.mapper.PayeeTypeMapper;
import com.eps.module.api.epsone.payeetype.repository.PayeeTypeRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.payment.PayeeType;
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
public class PayeeTypeServiceImpl implements PayeeTypeService {

    private final PayeeTypeRepository payeeTypeRepository;
    private final PayeeRepository payeeRepository;
    private final PayeeTypeMapper payeeTypeMapper;

    @Override
    @Transactional
    public PayeeTypeResponseDto createPayeeType(PayeeTypeRequestDto requestDto) {
        log.info("Creating payee type: {}", requestDto.getPayeeType());

        if (payeeTypeRepository.existsByPayeeTypeIgnoreCase(requestDto.getPayeeType())) {
            throw new IllegalArgumentException("Payee type '" + requestDto.getPayeeType() + "' already exists");
        }

        PayeeType payeeType = payeeTypeMapper.toEntity(requestDto);
        PayeeType savedPayeeType = payeeTypeRepository.save(payeeType);

        log.info("Payee type created successfully with ID: {}", savedPayeeType.getId());
        return payeeTypeMapper.toResponseDto(savedPayeeType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeTypeResponseDto> getAllPayeeTypes(Pageable pageable) {
        log.info("Fetching all payee types with pagination: {}", pageable);
        Page<PayeeType> payeeTypes = payeeTypeRepository.findAll(pageable);
        return payeeTypes.map(payeeTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayeeTypeResponseDto> searchPayeeTypes(String searchTerm, Pageable pageable) {
        log.info("Searching payee types with term: {}", searchTerm);
        Page<PayeeType> payeeTypes = payeeTypeRepository.searchPayeeTypes(searchTerm, pageable);
        return payeeTypes.map(payeeTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PayeeTypeResponseDto> getPayeeTypesList() {
        log.info("Fetching all payee types as list");
        return payeeTypeRepository.findAll().stream()
                .map(payeeTypeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PayeeTypeResponseDto getPayeeTypeById(Long id) {
        log.info("Fetching payee type with ID: {}", id);
        PayeeType payeeType = payeeTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payee type not found with ID: " + id));
        return payeeTypeMapper.toResponseDto(payeeType);
    }

    @Override
    @Transactional
    public PayeeTypeResponseDto updatePayeeType(Long id, PayeeTypeRequestDto requestDto) {
        log.info("Updating payee type with ID: {}", id);

        PayeeType payeeType = payeeTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payee type not found with ID: " + id));

        if (payeeTypeRepository.existsByPayeeTypeAndIdNot(requestDto.getPayeeType(), id)) {
            throw new IllegalArgumentException("Payee type '" + requestDto.getPayeeType() + "' already exists");
        }

        payeeTypeMapper.updateEntityFromDto(requestDto, payeeType);
        PayeeType updatedPayeeType = payeeTypeRepository.save(payeeType);

        log.info("Payee type updated successfully with ID: {}", id);
        return payeeTypeMapper.toResponseDto(updatedPayeeType);
    }

    @Override
    @Transactional
    public void deletePayeeType(Long id) {
        log.info("Deleting payee type with ID: {}", id);

        if (!payeeTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("Payee type not found with ID: " + id);
        }

        // Check for dependencies - payees
        long payeeCount = payeeRepository.countByPayeeTypeId(id);
        if (payeeCount > 0) {
            throw new IllegalStateException("Cannot delete payee type as it has " + payeeCount + " associated payees");
        }

        payeeTypeRepository.deleteById(id);
        log.info("Payee type deleted successfully with ID: {}", id);
    }
}
