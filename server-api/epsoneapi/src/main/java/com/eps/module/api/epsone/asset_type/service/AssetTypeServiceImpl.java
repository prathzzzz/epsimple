package com.eps.module.api.epsone.asset_type.service;

import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeBulkUploadDto;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeErrorReportDto;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeRequestDto;
import com.eps.module.api.epsone.asset_type.dto.AssetTypeResponseDto;
import com.eps.module.api.epsone.asset_type.mapper.AssetTypeMapper;
import com.eps.module.api.epsone.asset_type.processor.AssetTypeBulkUploadProcessor;
import com.eps.module.api.epsone.asset_type.repository.AssetTypeRepository;
import com.eps.module.asset.AssetCategory;
import com.eps.module.asset.AssetType;
import com.eps.module.common.bulk.dto.BulkUploadErrorDto;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.service.BaseBulkUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetTypeServiceImpl extends BaseBulkUploadService<AssetTypeBulkUploadDto, AssetType> implements AssetTypeService {

    private final AssetTypeRepository assetTypeRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final AssetTypeMapper assetTypeMapper;
    private final AssetTypeBulkUploadProcessor assetTypeBulkUploadProcessor;

    @Override
    @Transactional
    public AssetTypeResponseDto createAssetType(AssetTypeRequestDto requestDto) {
        // Check if type name already exists
        if (assetTypeRepository.existsByTypeNameIgnoreCase(requestDto.getTypeName())) {
            throw new IllegalArgumentException("Asset type with name '" + requestDto.getTypeName() + "' already exists");
        }

        // Check if type code already exists
        if (assetTypeRepository.existsByTypeCodeIgnoreCase(requestDto.getTypeCode())) {
            throw new IllegalArgumentException("Asset type with code '" + requestDto.getTypeCode() + "' already exists");
        }

        AssetType assetType = assetTypeMapper.toEntity(requestDto);
        AssetType savedAssetType = assetTypeRepository.save(assetType);
        return assetTypeMapper.toResponseDto(savedAssetType);
    }

    @Override
    @Transactional
    public AssetTypeResponseDto updateAssetType(Long id, AssetTypeRequestDto requestDto) {
        AssetType existingAssetType = assetTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset type not found with id: " + id));

        // Check if type name already exists for another asset type
        if (assetTypeRepository.existsByTypeNameAndIdNot(requestDto.getTypeName(), id)) {
            throw new IllegalArgumentException("Asset type with name '" + requestDto.getTypeName() + "' already exists");
        }

        // Check if type code already exists for another asset type
        if (assetTypeRepository.existsByTypeCodeAndIdNot(requestDto.getTypeCode(), id)) {
            throw new IllegalArgumentException("Asset type with code '" + requestDto.getTypeCode() + "' already exists");
        }

        assetTypeMapper.updateEntityFromDto(requestDto, existingAssetType);
        AssetType updatedAssetType = assetTypeRepository.save(existingAssetType);
        return assetTypeMapper.toResponseDto(updatedAssetType);
    }

    @Override
    @Transactional
    public void deleteAssetType(Long id) {
        log.info("Deleting asset type with ID: {}", id);

        AssetType assetType = assetTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset type not found with id: " + id));

        // Check for dependent asset categories
        Page<AssetCategory> dependentCategories = assetCategoryRepository.findByAssetTypeId(id, PageRequest.of(0, 6));

        if (!dependentCategories.isEmpty()) {
            long totalCount = dependentCategories.getTotalElements();
            List<String> categoryNames = dependentCategories.getContent().stream()
                    .limit(5)
                    .map(AssetCategory::getCategoryName)
                    .collect(Collectors.toList());

            String errorMessage = String.format(
                    "Cannot delete '%s' asset type because it is being used by %d asset %s: %s%s. Please delete or reassign these asset categories first.",
                    assetType.getTypeName(),
                    totalCount,
                    totalCount == 1 ? "category" : "categories",
                    String.join(", ", categoryNames),
                    totalCount > 5 ? " and " + (totalCount - 5) + " more" : ""
            );

            log.warn("Cannot delete asset type ID {} - has {} dependent asset categories", id, totalCount);
            throw new IllegalStateException(errorMessage);
        }

        assetTypeRepository.deleteById(id);
        log.info("Asset type deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public AssetTypeResponseDto getAssetTypeById(Long id) {
        AssetType assetType = assetTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asset type not found with id: " + id));
        return assetTypeMapper.toResponseDto(assetType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetTypeResponseDto> getAllAssetTypes(Pageable pageable) {
        Page<AssetType> assetTypes = assetTypeRepository.findAll(pageable);
        return assetTypes.map(assetTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetTypeResponseDto> searchAssetTypes(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getAllAssetTypes(pageable);
        }
        Page<AssetType> assetTypes = assetTypeRepository.searchAssetTypes(search.trim(), pageable);
        return assetTypes.map(assetTypeMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetTypeResponseDto> getAllAssetTypesList() {
        List<AssetType> assetTypes = assetTypeRepository.findAll();
        return assetTypeMapper.toResponseDtoList(assetTypes);
    }

    // ========== Bulk Upload Methods ==========

    @Override
    protected BulkUploadProcessor<AssetTypeBulkUploadDto, AssetType> getProcessor() {
        return assetTypeBulkUploadProcessor;
    }

    @Override
    public Class<AssetTypeBulkUploadDto> getBulkUploadDtoClass() {
        return AssetTypeBulkUploadDto.class;
    }

    @Override
    public String getEntityName() {
        return "Asset Type";
    }

    @Override
    public List<AssetType> getAllEntitiesForExport() {
        return assetTypeRepository.findAllForExport();
    }

    @Override
    public Function<AssetType, AssetTypeBulkUploadDto> getEntityToDtoMapper() {
        return entity -> AssetTypeBulkUploadDto.builder()
                .typeName(entity.getTypeName())
                .typeCode(entity.getTypeCode())
                .description(entity.getDescription())
                .build();
    }

    @Override
    protected Object buildErrorReportDto(BulkUploadErrorDto error) {
        AssetTypeErrorReportDto.AssetTypeErrorReportDtoBuilder builder = 
                AssetTypeErrorReportDto.builder()
                        .rowNumber(error.getRowNumber())
                        .errorMessage(error.getErrorMessage());

        if (error.getRowData() != null) {
            builder.typeName((String) error.getRowData().get("typeName"))
                    .typeCode((String) error.getRowData().get("typeCode"))
                    .description((String) error.getRowData().get("description"));
        }

        return builder.build();
    }

    @Override
    protected Class<?> getErrorReportDtoClass() {
        return AssetTypeErrorReportDto.class;
    }
}
