package com.eps.module.api.epsone.asset_category.service;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryBulkUploadDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryErrorReportDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryRequestDto;
import com.eps.module.api.epsone.asset_category.dto.AssetCategoryResponseDto;
import com.eps.module.api.epsone.asset_category.mapper.AssetCategoryMapper;
import com.eps.module.api.epsone.asset_category.processor.AssetCategoryBulkUploadProcessor;
import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.api.epsone.asset_type.repository.AssetTypeRepository;
import com.eps.module.asset.AssetCategory;
import com.eps.module.asset.AssetType;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
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
    private final AssetTypeRepository assetTypeRepository;
    private final AssetRepository assetRepository;
    private final AssetCategoryMapper assetCategoryMapper;
    private final AssetCategoryBulkUploadProcessor assetCategoryBulkUploadProcessor;

    @Override
    @Transactional
    public AssetCategoryResponseDto createAssetCategory(AssetCategoryRequestDto requestDto) {
        log.info("Creating asset category: {}", requestDto.getCategoryName());

        // Validate asset type exists
        AssetType assetType = assetTypeRepository.findById(requestDto.getAssetTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Asset type not found with id: " + requestDto.getAssetTypeId()));

        // Check for duplicates
        if (assetCategoryRepository.existsByCategoryNameIgnoreCase(requestDto.getCategoryName())) {
            throw new IllegalArgumentException("Asset category with name '" + requestDto.getCategoryName() + "' already exists");
        }

        if (assetCategoryRepository.existsByCategoryCodeIgnoreCase(requestDto.getCategoryCode())) {
            throw new IllegalArgumentException("Asset category with code '" + requestDto.getCategoryCode() + "' already exists");
        }

        if (assetCategoryRepository.existsByAssetCodeAltIgnoreCase(requestDto.getAssetCodeAlt())) {
            throw new IllegalArgumentException("Asset code alt '" + requestDto.getAssetCodeAlt() + "' already exists");
        }

        AssetCategory assetCategory = assetCategoryMapper.toEntity(requestDto);
        assetCategory.setAssetType(assetType);
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
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.ASSET_CATEGORY_NOT_FOUND, id)));
        return assetCategoryMapper.toResponseDto(assetCategory);
    }

    @Override
    @Transactional
    public AssetCategoryResponseDto updateAssetCategory(Long id, AssetCategoryRequestDto requestDto) {
        log.info("Updating asset category with ID: {}", id);

        AssetCategory existing = assetCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.ASSET_CATEGORY_NOT_FOUND, id)));

        // Validate asset type exists
        AssetType assetType = assetTypeRepository.findById(requestDto.getAssetTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Asset type not found with id: " + requestDto.getAssetTypeId()));

        // Check for duplicates (excluding current record)
        if (assetCategoryRepository.existsByCategoryNameAndIdNot(requestDto.getCategoryName(), id)) {
            throw new IllegalArgumentException("Asset category with name '" + requestDto.getCategoryName() + "' already exists");
        }

        if (assetCategoryRepository.existsByCategoryCodeAndIdNot(requestDto.getCategoryCode(), id)) {
            throw new IllegalArgumentException("Asset category with code '" + requestDto.getCategoryCode() + "' already exists");
        }

        if (assetCategoryRepository.existsByAssetCodeAltAndIdNot(requestDto.getAssetCodeAlt(), id)) {
            throw new IllegalArgumentException("Asset code alt '" + requestDto.getAssetCodeAlt() + "' already exists");
        }

        assetCategoryMapper.updateEntityFromDto(requestDto, existing);
        existing.setAssetType(assetType);
        AssetCategory updated = assetCategoryRepository.save(existing);

        log.info("Asset category updated successfully with ID: {}", id);
        return assetCategoryMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteAssetCategory(Long id) {
        log.info("Deleting asset category with ID: {}", id);

        AssetCategory assetCategory = assetCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessages.ASSET_CATEGORY_NOT_FOUND, id)));

        // Check if this asset category is being used by any assets
        long assetCount = assetRepository.countByAssetCategoryId(id);
        if (assetCount > 0) {
            throw new IllegalStateException("Cannot delete asset category. It is referenced in " + assetCount + " asset(s).");
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
                .assetTypeCode(entity.getAssetType() != null ? entity.getAssetType().getTypeCode() : null)
                .assetCodeAlt(entity.getAssetCodeAlt())
                .description(entity.getDescription())
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
                    .assetTypeCode((String) error.getRowData().get("assetTypeCode"))
                    .assetCodeAlt((String) error.getRowData().get("assetCodeAlt"))
                    .description((String) error.getRowData().get("description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return AssetCategoryErrorReportDto.class;
    }
}
