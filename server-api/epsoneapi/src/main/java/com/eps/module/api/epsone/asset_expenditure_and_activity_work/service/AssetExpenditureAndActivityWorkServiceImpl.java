package com.eps.module.api.epsone.asset_expenditure_and_activity_work.service;

import com.eps.module.activity.ActivityWork;
import com.eps.module.api.epsone.activity_work.repository.ActivityWorkRepository;
import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkRequestDto;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.dto.AssetExpenditureAndActivityWorkResponseDto;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.mapper.AssetExpenditureAndActivityWorkMapper;
import com.eps.module.api.epsone.asset_expenditure_and_activity_work.repository.AssetExpenditureAndActivityWorkRepository;
import com.eps.module.api.epsone.expenditures_invoice.repository.ExpendituresInvoiceRepository;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetExpenditureAndActivityWork;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.cost.ExpendituresInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetExpenditureAndActivityWorkServiceImpl implements AssetExpenditureAndActivityWorkService {

    private final AssetExpenditureAndActivityWorkRepository repository;
    private final AssetRepository assetRepository;
    private final ActivityWorkRepository activityWorkRepository;
    private final ExpendituresInvoiceRepository expendituresInvoiceRepository;
    private final AssetExpenditureAndActivityWorkMapper mapper;

    @Override
    @Transactional
    public AssetExpenditureAndActivityWorkResponseDto createAssetExpenditureAndActivityWork(
            AssetExpenditureAndActivityWorkRequestDto requestDto) {
        
        log.info("Creating asset expenditure and activity work for asset: {}, expenditure: {}, activity work: {}",
                requestDto.getAssetId(), requestDto.getExpendituresInvoiceId(), requestDto.getActivityWorkId());

        // Validate asset exists
        Asset asset = assetRepository.findById(requestDto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset not found with id: " + requestDto.getAssetId()));

        // Validate expenditures invoice exists (optional)
        ExpendituresInvoice expendituresInvoice = null;
        if (requestDto.getExpendituresInvoiceId() != null) {
            expendituresInvoice = expendituresInvoiceRepository.findById(requestDto.getExpendituresInvoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Expenditures invoice not found with id: " + requestDto.getExpendituresInvoiceId()));
        }

        // Validate activity work exists (optional)
        ActivityWork activityWork = null;
        if (requestDto.getActivityWorkId() != null) {
            activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Activity work not found with id: " + requestDto.getActivityWorkId()));
        }

        // Check for duplicate
        if (repository.existsByAssetIdAndExpendituresInvoiceIdAndActivityWorkId(
                requestDto.getAssetId(), requestDto.getExpendituresInvoiceId(), requestDto.getActivityWorkId())) {
            throw new IllegalStateException(
                    "This asset-expenditure-activity work combination already exists");
        }

        // Create entity
        AssetExpenditureAndActivityWork entity = mapper.toEntity(requestDto, asset, expendituresInvoice, activityWork);
        AssetExpenditureAndActivityWork saved = repository.save(entity);

        // Fetch with details for response
        AssetExpenditureAndActivityWork savedWithDetails = repository.findByIdWithDetails(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset expenditure and activity work not found after save"));

        log.info("Asset expenditure and activity work created successfully with ID: {}", saved.getId());
        return mapper.toDto(savedWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetExpenditureAndActivityWorkResponseDto> getAllAssetExpenditureAndActivityWorks(
            int page, int size, String sortBy, String sortOrder) {
        
        log.info("Fetching all asset expenditure and activity works with pagination: page={}, size={}", page, size);
        
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetExpenditureAndActivityWork> expenditurePage = repository.findAllWithDetails(pageable);
        return expenditurePage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetExpenditureAndActivityWorkResponseDto> getExpendituresByAssetId(
            Long assetId, int page, int size, String sortBy, String sortOrder) {
        
        log.info("Fetching expenditures for asset ID: {}", assetId);

        // Validate asset exists
        if (!assetRepository.existsById(assetId)) {
            throw new ResourceNotFoundException("Asset not found with id: " + assetId);
        }

        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetExpenditureAndActivityWork> expenditurePage = repository.findByAssetId(assetId, pageable);
        return expenditurePage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetExpenditureAndActivityWorkResponseDto> getExpendituresByActivityWorkId(
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

        Page<AssetExpenditureAndActivityWork> expenditurePage = repository.findByActivityWorkId(activityWorkId, pageable);
        return expenditurePage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetExpenditureAndActivityWorkResponseDto> searchExpenditures(
            String searchTerm, int page, int size, String sortBy, String sortOrder) {
        
        log.info("Searching asset expenditure and activity works with keyword: {}", searchTerm);
        
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AssetExpenditureAndActivityWork> expenditurePage = repository.searchExpenditures(searchTerm, pageable);
        return expenditurePage.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetExpenditureAndActivityWorkResponseDto getExpenditureById(Long id) {
        log.info("Fetching asset expenditure and activity work with ID: {}", id);
        
        AssetExpenditureAndActivityWork expenditure = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset expenditure and activity work not found with id: " + id));
        
        return mapper.toDto(expenditure);
    }

    @Override
    @Transactional
    public AssetExpenditureAndActivityWorkResponseDto updateAssetExpenditureAndActivityWork(
            Long id, AssetExpenditureAndActivityWorkRequestDto requestDto) {
        
        log.info("Updating asset expenditure and activity work with ID: {}", id);

        AssetExpenditureAndActivityWork existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset expenditure and activity work not found with id: " + id));

        // Validate asset exists
        Asset asset = assetRepository.findById(requestDto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset not found with id: " + requestDto.getAssetId()));

        // Validate expenditures invoice exists (optional)
        ExpendituresInvoice expendituresInvoice = null;
        if (requestDto.getExpendituresInvoiceId() != null) {
            expendituresInvoice = expendituresInvoiceRepository.findById(requestDto.getExpendituresInvoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Expenditures invoice not found with id: " + requestDto.getExpendituresInvoiceId()));
        }

        // Validate activity work exists (optional)
        ActivityWork activityWork = null;
        if (requestDto.getActivityWorkId() != null) {
            activityWork = activityWorkRepository.findById(requestDto.getActivityWorkId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Activity work not found with id: " + requestDto.getActivityWorkId()));
        }

        // Update entity
        mapper.updateEntity(existing, requestDto, asset, expendituresInvoice, activityWork);
        AssetExpenditureAndActivityWork updated = repository.save(existing);

        // Fetch with details for response
        AssetExpenditureAndActivityWork updatedWithDetails = repository.findByIdWithDetails(updated.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Asset expenditure and activity work not found after update"));

        log.info("Asset expenditure and activity work updated successfully with ID: {}", id);
        return mapper.toDto(updatedWithDetails);
    }

    @Override
    @Transactional
    public void deleteAssetExpenditureAndActivityWork(Long id) {
        log.info("Deleting asset expenditure and activity work with ID: {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Asset expenditure and activity work not found with id: " + id);
        }

        repository.deleteById(id);
        log.info("Asset expenditure and activity work deleted successfully with ID: {}", id);
    }
}
