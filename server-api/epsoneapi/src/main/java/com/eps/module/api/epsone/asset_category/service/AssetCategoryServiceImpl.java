package com.eps.module.api.epsone.asset_category.service;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_category.constant.AssetCategoryErrorMessages;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryBulkUploadDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryErrorReportDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryRequestDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryResponseDto;
import com.eps.module.api.epsone.asset_category.mapper.AssetCategoryMapper;
import com.eps.module.api.epsone.asset_category.processor.AssetCategoryBulkUploadProcessor;
import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.asset.AssetCategory;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import com.eps.module.common.exception.ConflictException;
import com.eps.module.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetCategoryServiceImpl extends BaseBulkUploadService<AssetCategoryBulkUploadDto, AssetCategory> implements AssetCategoryService {

    private final AssetCategoryRepository assetCategoryRepository;
    private final AssetRepository assetRepository;
    private final AssetCategoryMapper assetCategoryMapper;
    private final AssetCategoryBulkUploadProcessor assetCategoryBulkUploadProcessor;

    @Override
    @Transactional
    public AssetCategoryResponseDto createAssetCategory(AssetCategoryRequestDto requestDto) {
        log.info("Creating asset category: {}", requestDto.getCategoryName());

        // Check for duplicates
        if (assetCategoryRepository.existsByCategoryNameIgnoreCase(requestDto.getCategoryName())) {
            throw new ConflictException(String.format(AssetCategoryErrorMessages.ASSET_CATEGORY_NAME_ALREADY_EXISTS, requestDto.getCategoryName()));
        }

        if (assetCategoryRepository.existsByCategoryCodeIgnoreCase(requestDto.getCategoryCode())) {
            throw new ConflictException(String.format(AssetCategoryErrorMessages.ASSET_CATEGORY_CODE_ALREADY_EXISTS, requestDto.getCategoryCode()));
        }

        if (assetCategoryRepository.existsByAssetCodeAltIgnoreCase(requestDto.getAssetCodeAlt())) {
            throw new ConflictException(String.format(AssetCategoryErrorMessages.ASSET_CODE_ALT_ALREADY_EXISTS, requestDto.getAssetCodeAlt()));
        }

        AssetCategory assetCategory = assetCategoryMapper.toEntity(requestDto);
        AssetCategory saved = assetCategoryRepository.save(assetCategory);

        log.info("Asset category created successfully with ID: {}", saved.getId());
        return assetCategoryMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetCategoryResponseDto> getAllAssetCategories(Pageable pageable) {
        log.info("Fetching all asset categories with pagination");
        return assetCategoryRepository.findAll(pageable)
                .map(assetCategoryMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetCategoryResponseDto> searchAssetCategories(String searchTerm, Pageable pageable) {
        log.info("Searching asset categories with term: {}", searchTerm);
        return assetCategoryRepository.searchAssetCategories(searchTerm, pageable)
                .map(assetCategoryMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetCategoryResponseDto> getAllAssetCategoriesList() {
        log.info("Fetching all asset categories as list");
        return assetCategoryRepository.findAll().stream()
                .map(assetCategoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AssetCategoryResponseDto getAssetCategoryById(Long id) {
        log.info("Fetching asset category by ID: {}", id);
        AssetCategory assetCategory = assetCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AssetCategoryErrorMessages.ASSET_CATEGORY_NOT_FOUND_ID + id));
        return assetCategoryMapper.toResponseDto(assetCategory);
    }

    @Override
    @Transactional
    public AssetCategoryResponseDto updateAssetCategory(Long id, AssetCategoryRequestDto requestDto) {
        log.info("Updating asset category with ID: {}", id);

        AssetCategory existing = assetCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AssetCategoryErrorMessages.ASSET_CATEGORY_NOT_FOUND_ID + id));

        // Check for duplicates (excluding current record)
        if (assetCategoryRepository.existsByCategoryNameAndIdNot(requestDto.getCategoryName(), id)) {
            throw new ConflictException(String.format(AssetCategoryErrorMessages.ASSET_CATEGORY_NAME_ALREADY_EXISTS, requestDto.getCategoryName()));
        }

        if (assetCategoryRepository.existsByCategoryCodeAndIdNot(requestDto.getCategoryCode(), id)) {
            throw new ConflictException(String.format(AssetCategoryErrorMessages.ASSET_CATEGORY_CODE_ALREADY_EXISTS, requestDto.getCategoryCode()));
        }

        if (assetCategoryRepository.existsByAssetCodeAltAndIdNot(requestDto.getAssetCodeAlt(), id)) {
            throw new ConflictException(String.format(AssetCategoryErrorMessages.ASSET_CODE_ALT_ALREADY_EXISTS, requestDto.getAssetCodeAlt()));
        }

        assetCategoryMapper.updateEntityFromDto(requestDto, existing);
        AssetCategory updated = assetCategoryRepository.save(existing);

        log.info("Asset category updated successfully with ID: {}", id);
        return assetCategoryMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteAssetCategory(Long id) {
        log.info("Deleting asset category with ID: {}", id);

        AssetCategory assetCategory = assetCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AssetCategoryErrorMessages.ASSET_CATEGORY_NOT_FOUND_ID + id));

        // Check if this asset category is being used by any assets
        long assetCount = assetRepository.countByAssetCategoryId(id);
        if (assetCount > 0) {
            throw new ConflictException(String.format(AssetCategoryErrorMessages.CANNOT_DELETE_ASSET_CATEGORY_IN_USE, assetCount));
        }

        assetCategoryRepository.deleteById(id);
        log.info("Asset category deleted successfully with ID: {}", id);
    }

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<AssetCategoryBulkUploadDto, AssetCategory> getProcessor() {
        return assetCategoryBulkUploadProcessor;
    }

    @Override
    public Class<AssetCategoryBulkUploadDto> getBulkUploadDtoClass() {
        return AssetCategoryBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Asset Category";
    }

    @Override
    public List<AssetCategory> getAllEntitiesForExport() {
        return assetCategoryRepository.findAllForExport();
    }

    @Override
    public Function<AssetCategory, AssetCategoryBulkUploadDto> getEntityToDtoMapper() {
        return entity -> AssetCategoryBulkUploadDto.builder()
                .categoryName(entity.getCategoryName())
                .categoryCode(entity.getCategoryCode())
                .assetCodeAlt(entity.getAssetCodeAlt())
                .description(entity.getDescription())
                .depreciation(entity.getDepreciation())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        AssetCategoryErrorReportDto.AssetCategoryErrorReportDtoBuilder builder =
                AssetCategoryErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.categoryName((String) error.getRowData().get("categoryName"))
                    .categoryCode((String) error.getRowData().get("categoryCode"))
                    .assetCodeAlt((String) error.getRowData().get("assetCodeAlt"))
                    .description((String) error.getRowData().get("description"))
                    .depreciation((Double) error.getRowData().get("depreciation"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return AssetCategoryErrorReportDto.class;
    }
}
