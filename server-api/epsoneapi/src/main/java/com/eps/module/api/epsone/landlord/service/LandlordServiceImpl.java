package com.eps.module.api.epsone.landlord.service;

import com.eps.module.api.epsone.landlord.dto.LandlordRequestDto;
import com.eps.module.api.epsone.landlord.dto.LandlordResponseDto;
import com.eps.module.api.epsone.landlord.mapper.LandlordMapper;
import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.person.PersonDetails;
import com.eps.module.vendor.Landlord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LandlordServiceImpl implements LandlordService {

    private final LandlordRepository landlordRepository;
    private final PersonDetailsRepository personDetailsRepository;
    private final PayeeRepository payeeRepository;
    private final LandlordMapper landlordMapper;

    @Override
    @Transactional
    public LandlordResponseDto createLandlord(LandlordRequestDto requestDto) {
        // Validate person details exists
        PersonDetails personDetails = personDetailsRepository.findById(requestDto.getLandlordDetailsId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person details not found with id: " + requestDto.getLandlordDetailsId()));

        // Check if person details is already used by another landlord
        if (landlordRepository.findByLandlordDetailsId(requestDto.getLandlordDetailsId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Person details with id " + requestDto.getLandlordDetailsId() + 
                    " is already associated with another landlord");
        }

        Landlord landlord = landlordMapper.toEntity(requestDto);
        landlord.setLandlordDetails(personDetails);

        Landlord savedLandlord = landlordRepository.save(landlord);
        return landlordMapper.toDto(savedLandlord);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LandlordResponseDto> getAllLandlords(Pageable pageable) {
        return landlordRepository.findAll(pageable)
                .map(landlordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LandlordResponseDto> searchLandlords(String searchTerm, Pageable pageable) {
        return landlordRepository.searchLandlords(searchTerm, pageable)
                .map(landlordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LandlordResponseDto> getAllLandlordsList() {
        return landlordRepository.findAllList().stream()
                .map(landlordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LandlordResponseDto getLandlordById(Long id) {
        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Landlord not found with id: " + id));
        return landlordMapper.toDto(landlord);
    }

    @Override
    @Transactional
    public LandlordResponseDto updateLandlord(Long id, LandlordRequestDto requestDto) {
        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Landlord not found with id: " + id));

        // Validate person details exists
        PersonDetails personDetails = personDetailsRepository.findById(requestDto.getLandlordDetailsId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person details not found with id: " + requestDto.getLandlordDetailsId()));

        // Check if person details is already used by another landlord (excluding current)
        landlordRepository.findByLandlordDetailsId(requestDto.getLandlordDetailsId())
                .ifPresent(existingLandlord -> {
                    if (!existingLandlord.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Person details with id " + requestDto.getLandlordDetailsId() + 
                                " is already associated with another landlord");
                    }
                });

        landlordMapper.updateEntityFromDto(requestDto, landlord);
        landlord.setLandlordDetails(personDetails);

        Landlord updatedLandlord = landlordRepository.save(landlord);
        return landlordMapper.toDto(updatedLandlord);
    }

    @Override
    @Transactional
    public void deleteLandlord(Long id) {
        if (!landlordRepository.existsById(id)) {
            throw new IllegalArgumentException("Landlord not found with id: " + id);
        }

        // Check for dependencies - payees
        long payeeCount = payeeRepository.countByLandlordId(id);
        if (payeeCount > 0) {
            throw new IllegalStateException("Cannot delete landlord as it has " + payeeCount + " associated payees");
        }

        landlordRepository.deleteById(id);
    }
}
