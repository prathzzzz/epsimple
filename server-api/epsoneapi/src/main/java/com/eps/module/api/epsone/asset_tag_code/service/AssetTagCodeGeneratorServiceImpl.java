package com.eps.module.api.epsone.asset_tag_code.service;

import com.eps.module.api.epsone.asset.repository.AssetRepository;
import com.eps.module.api.epsone.asset_category.repository.AssetCategoryRepository;
import com.eps.module.api.epsone.asset_tag_code.dto.AssetTagCodeGeneratorRequestDto;
import com.eps.module.api.epsone.asset_tag_code.dto.AssetTagCodeGeneratorResponseDto;
import com.eps.module.api.epsone.asset_tag_code.dto.GeneratedAssetTagDto;
import com.eps.module.api.epsone.asset_tag_code.mapper.AssetTagCodeGeneratorMapper;
import com.eps.module.api.epsone.asset_tag_code.repository.AssetTagCodeGeneratorRepository;
import com.eps.module.api.epsone.bank.repository.BankRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.asset.AssetCategory;
import com.eps.module.asset.AssetTagCodeGenerator;
import com.eps.module.bank.Bank;
import com.eps.module.common.constants.ErrorMessages;
import com.eps.module.common.exception.ResourceNotFoundException;
import com.eps.module.common.util.ValidationUtils;
import com.eps.module.vendor.Vendor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetTagCodeGeneratorServiceImpl implements AssetTagCodeGeneratorService {

    private final AssetTagCodeGeneratorRepository generatorRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final VendorRepository vendorRepository;
    private final BankRepository bankRepository;
    private final AssetRepository assetRepository;
    private final AssetTagCodeGeneratorMapper mapper;

    @Override
    @Transactional
    public AssetTagCodeGeneratorResponseDto createGenerator(AssetTagCodeGeneratorRequestDto dto) {
        log.info("Creating asset tag code generator for category: {}, vendor: {}, bank: {}",
            dto.getAssetCategoryId(), dto.getVendorId(), dto.getBankId());

        // Check if generator already exists
        if (generatorRepository.existsByAssetCategoryIdAndVendorIdAndBankId(
                dto.getAssetCategoryId(), dto.getVendorId(), dto.getBankId())) {
            throw new IllegalArgumentException(
                String.format(ErrorMessages.CODE_GENERATOR_ALREADY_EXISTS, 
                    "Asset tag", "category-vendor-bank")
            );
        }

        AssetCategory category = assetCategoryRepository.findById(dto.getAssetCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.ASSET_CATEGORY_NOT_FOUND, dto.getAssetCategoryId())
            ));

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.VENDOR_NOT_FOUND, dto.getVendorId())
            ));

        Bank bank = bankRepository.findById(dto.getBankId())
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.BANK_NOT_FOUND_WITH_ID, dto.getBankId())
            ));

        AssetTagCodeGenerator generator = mapper.toEntity(dto, category, vendor, bank);
        AssetTagCodeGenerator saved = generatorRepository.save(generator);

        log.info("Asset tag code generator created successfully with id: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetTagCodeGeneratorResponseDto> getAllGenerators(Pageable pageable) {
        log.info("Fetching all asset tag code generators with pagination");
        return generatorRepository.findAll(pageable)
            .map(mapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetTagCodeGeneratorResponseDto> searchGenerators(String searchTerm, Pageable pageable) {
        log.info("Searching asset tag code generators with term: {}", searchTerm);
        return generatorRepository.search(searchTerm, pageable)
            .map(mapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetTagCodeGeneratorResponseDto> getAllGeneratorsAsList() {
        log.info("Fetching all asset tag code generators as list");
        return generatorRepository.findAllWithDetails().stream()
            .map(mapper::toResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AssetTagCodeGeneratorResponseDto getGeneratorById(Long id) {
        log.info("Fetching asset tag code generator with id: {}", id);
        AssetTagCodeGenerator generator = generatorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.CODE_GENERATOR_NOT_FOUND, "Asset tag", id)
            ));
        return mapper.toResponseDto(generator);
    }

    @Override
    @Transactional
    public AssetTagCodeGeneratorResponseDto updateGenerator(Long id, AssetTagCodeGeneratorRequestDto dto) {
        log.info("Updating asset tag code generator with id: {}", id);

        AssetTagCodeGenerator existing = generatorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.CODE_GENERATOR_NOT_FOUND, "Asset tag", id)
            ));

        // Check for duplicate if changing the combination
        if (!existing.getAssetCategory().getId().equals(dto.getAssetCategoryId()) ||
            !existing.getVendor().getId().equals(dto.getVendorId()) ||
            !existing.getBank().getId().equals(dto.getBankId())) {
            
            if (generatorRepository.existsByAssetCategoryIdAndVendorIdAndBankId(
                    dto.getAssetCategoryId(), dto.getVendorId(), dto.getBankId())) {
                throw new IllegalArgumentException(
                    String.format(ErrorMessages.CODE_GENERATOR_ALREADY_EXISTS, 
                        "Asset tag", "category-vendor-bank")
                );
            }
        }

        AssetCategory category = assetCategoryRepository.findById(dto.getAssetCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.ASSET_CATEGORY_NOT_FOUND, dto.getAssetCategoryId())
            ));

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.VENDOR_NOT_FOUND, dto.getVendorId())
            ));

        Bank bank = bankRepository.findById(dto.getBankId())
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.BANK_NOT_FOUND_WITH_ID, dto.getBankId())
            ));

        existing.setAssetCategory(category);
        existing.setVendor(vendor);
        existing.setBank(bank);
        existing.setMaxSeqDigit(dto.getMaxSeqDigit());
        existing.setRunningSeq(dto.getRunningSeq());

        AssetTagCodeGenerator updated = generatorRepository.save(existing);
        log.info("Asset tag code generator updated successfully");
        return mapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteGenerator(Long id) {
        log.info("Deleting asset tag code generator with id: {}", id);
        
        AssetTagCodeGenerator generator = generatorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.CODE_GENERATOR_NOT_FOUND, "Asset tag", id)
            ));

        // Check if any assets are using this generator's pattern
        AssetCategory category = generator.getAssetCategory();
        Vendor vendor = generator.getVendor();
        Bank bank = generator.getBank();
        
        // Build the prefix pattern that this generator creates
        String categoryCode = category.getCategoryCode() != null ? category.getCategoryCode() : "";
        String vendorCode = vendor.getVendorCodeAlt() != null ? vendor.getVendorCodeAlt() : "";
        String bankCode = bank.getBankCodeAlt() != null ? bank.getBankCodeAlt() : "";
        String tagPrefix = categoryCode + vendorCode + bankCode;
        
        // Check if any assets exist with tags starting with this pattern
        List<String> existingTags = assetRepository.findAssetTagIdsByCategoryVendorBank(
            category.getId(), vendor.getId(), bank.getId()
        );
        
        if (!existingTags.isEmpty()) {
            throw new IllegalStateException(
                "Cannot delete asset tag code generator. " + existingTags.size() + 
                " asset(s) are using tags generated by this pattern (prefix: " + tagPrefix + ")."
            );
        }
        
        generatorRepository.deleteById(id);
        log.info("Asset tag code generator deleted successfully");
    }

    @Override
    @Transactional
    public GeneratedAssetTagDto generateAssetTag(Long assetCategoryId, Long vendorId, Long bankId) {
        log.info("Generating asset tag for category: {}, vendor: {}, bank: {}", 
            assetCategoryId, vendorId, bankId);

        // Find or create generator with pessimistic lock for thread-safety
        AssetTagCodeGenerator generator = generatorRepository
            .findByCategoryVendorBankWithLock(assetCategoryId, vendorId, bankId)
            .orElseGet(() -> createGeneratorIfNotExists(assetCategoryId, vendorId, bankId));

        AssetCategory category = generator.getAssetCategory();
        Vendor vendor = generator.getVendor();
        Bank bank = generator.getBank();

        // Check existing asset tags to find max sequence (handles bulk uploads)
        Integer nextSequence = findNextAvailableSequence(
            category, vendor, bank, generator.getRunningSeq()
        );

        // Generate the tag using the safe sequence
        GeneratedAssetTagDto result = mapper.buildGeneratedTag(
            category, vendor, bank, nextSequence, generator.getMaxSeqDigit()
        );

        // Update generator with new sequence
        generator.setRunningSeq(nextSequence + 1);
        generatorRepository.save(generator);

        log.info("Successfully generated asset tag: {}", result.getAssetTag());
        
        // Adjust sequences (currentSequence was used, nextSequence is now current)
        result.setSequence(nextSequence);
        result.setNextSequence(nextSequence + 1);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public GeneratedAssetTagDto previewAssetTag(Long assetCategoryId, Long vendorId, Long bankId) {
        log.info("Previewing asset tag for category: {}, vendor: {}, bank: {}", 
            assetCategoryId, vendorId, bankId);

        // Find existing generator or use default values
        AssetTagCodeGenerator generator = generatorRepository
            .findByCategoryVendorBank(assetCategoryId, vendorId, bankId)
            .orElseGet(() -> {
                // Create temporary generator for preview (not saved)
                return createTemporaryGenerator(assetCategoryId, vendorId, bankId);
            });

        AssetCategory category = generator.getAssetCategory();
        Vendor vendor = generator.getVendor();
        Bank bank = generator.getBank();

        // Generate preview tag using current sequence (without incrementing)
        GeneratedAssetTagDto result = mapper.buildGeneratedTag(
            category, vendor, bank, generator.getRunningSeq(), generator.getMaxSeqDigit()
        );

        log.info("Successfully previewed asset tag: {}", result.getAssetTag());
        return result;
    }

    private AssetTagCodeGenerator createTemporaryGenerator(
        Long assetCategoryId,
        Long vendorId,
        Long bankId
    ) {
        AssetCategory category = assetCategoryRepository.findById(assetCategoryId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.ASSET_CATEGORY_NOT_FOUND, assetCategoryId)
            ));

        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.VENDOR_NOT_FOUND, vendorId)
            ));

        Bank bank = bankRepository.findById(bankId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.BANK_NOT_FOUND_WITH_ID, bankId)
            ));

        return AssetTagCodeGenerator.builder()
            .assetCategory(category)
            .vendor(vendor)
            .bank(bank)
            .maxSeqDigit(5)
            .runningSeq(1)
            .build();
    }

    private AssetTagCodeGenerator createGeneratorIfNotExists(
        Long assetCategoryId,
        Long vendorId,
        Long bankId
    ) {
        log.info("Auto-creating asset tag code generator for category: {}, vendor: {}, bank: {}", 
            assetCategoryId, vendorId, bankId);

        AssetCategory category = assetCategoryRepository.findById(assetCategoryId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.ASSET_CATEGORY_NOT_FOUND, assetCategoryId)
            ));

        Vendor vendor = vendorRepository.findById(vendorId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.VENDOR_NOT_FOUND, vendorId)
            ));

        Bank bank = bankRepository.findById(bankId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.BANK_NOT_FOUND_WITH_ID, bankId)
            ));

        AssetTagCodeGenerator generator = AssetTagCodeGenerator.builder()
            .assetCategory(category)
            .vendor(vendor)
            .bank(bank)
            .maxSeqDigit(5)
            .runningSeq(1)
            .build();

        return generatorRepository.save(generator);
    }

    /**
     * Find the next available sequence by checking existing asset tags in the database.
     * This ensures we never generate duplicate codes, even after bulk uploads.
     */
    private Integer findNextAvailableSequence(
        AssetCategory category,
        Vendor vendor,
        Bank bank,
        Integer runningSeq
    ) {
        // Get all existing asset tag IDs for this category, vendor, and bank combination
        List<String> existingTags = assetRepository.findAssetTagIdsByCategoryVendorBank(
            category.getId(),
            vendor.getId(),
            bank.getId()
        );
        
        if (existingTags.isEmpty()) {
            log.debug("No existing assets found, using running sequence: {}", runningSeq);
            return runningSeq;
        }
        
        // Extract numeric sequences from existing tags and find the maximum
        int maxSequence = 0;
        Pattern sequencePattern = Pattern.compile("\\d+$"); // Match trailing digits
        
        for (String tag : existingTags) {
            Matcher matcher = sequencePattern.matcher(tag);
            if (matcher.find()) {
                try {
                    int sequence = Integer.parseInt(matcher.group());
                    maxSequence = Math.max(maxSequence, sequence);
                    log.trace("Found sequence {} in tag: {}", sequence, tag);
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse sequence from asset tag: {}", tag);
                }
            }
        }
        
        // Return the higher of: (max existing sequence + 1) or generator's running sequence
        int nextSequence = Math.max(maxSequence + 1, runningSeq);
        
        if (nextSequence != runningSeq) {
            log.info("Adjusted sequence from {} to {} based on {} existing asset tags", 
                runningSeq, nextSequence, existingTags.size());
        }
        
        return nextSequence;
    }
}
