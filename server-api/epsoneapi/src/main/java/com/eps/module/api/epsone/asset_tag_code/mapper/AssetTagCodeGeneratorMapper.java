package com.eps.module.api.epsone.asset_tag_code.mapper;

import com.eps.module.api.epsone.asset_tag_code.dto.AssetTagCodeGeneratorRequestDto;
import com.eps.module.api.epsone.asset_tag_code.dto.AssetTagCodeGeneratorResponseDto;
import com.eps.module.api.epsone.asset_tag_code.dto.GeneratedAssetTagDto;
import com.eps.module.asset.AssetCategory;
import com.eps.module.asset.AssetTagCodeGenerator;
import com.eps.module.bank.Bank;
import com.eps.module.vendor.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssetTagCodeGeneratorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assetCategory", source = "assetCategory")
    @Mapping(target = "vendor", source = "vendor")
    @Mapping(target = "bank", source = "bank")
    @Mapping(target = "maxSeqDigit", source = "dto.maxSeqDigit")
    @Mapping(target = "runningSeq", source = "dto.runningSeq")
    AssetTagCodeGenerator toEntity(
        AssetTagCodeGeneratorRequestDto dto,
        AssetCategory assetCategory,
        Vendor vendor,
        Bank bank
    );

    @Mapping(target = "assetCategoryId", source = "generator.assetCategory.id")
    @Mapping(target = "assetCategoryName", source = "generator.assetCategory.categoryName")
    @Mapping(target = "assetCategoryCode", source = "generator.assetCategory.categoryCode")
    @Mapping(target = "vendorId", source = "generator.vendor.id")
    @Mapping(target = "vendorName", expression = "java(buildVendorName(generator.getVendor()))")
    @Mapping(target = "vendorCodeAlt", source = "generator.vendor.vendorCodeAlt")
    @Mapping(target = "bankId", source = "generator.bank.id")
    @Mapping(target = "bankName", source = "generator.bank.bankName")
    @Mapping(target = "bankCode", source = "generator.bank.bankCodeAlt")
    AssetTagCodeGeneratorResponseDto toResponseDto(AssetTagCodeGenerator generator);

    default GeneratedAssetTagDto buildGeneratedTag(
        AssetCategory category,
        Vendor vendor,
        Bank bank,
        Integer sequence,
        Integer maxSeqDigit
    ) {
        String assetTag = buildAssetTag(category, vendor, bank, sequence, maxSeqDigit);
        
        return GeneratedAssetTagDto.builder()
            .assetTag(assetTag)
            .assetCategoryCode(category.getCategoryCode())
            .vendorCode(vendor.getVendorCodeAlt())
            .bankCode(bank.getBankCodeAlt())
            .sequence(sequence)
            .nextSequence(sequence + 1)
            .build();
    }

    default String buildAssetTag(
        AssetCategory category,
        Vendor vendor,
        Bank bank,
        Integer sequence,
        Integer maxSeqDigit
    ) {
        String categoryCode = category.getCategoryCode() != null ? category.getCategoryCode() : "";
        String vendorCode = vendor.getVendorCodeAlt() != null ? vendor.getVendorCodeAlt() : "";
        String bankCode = bank.getBankCodeAlt() != null ? bank.getBankCodeAlt() : "";
        String seqStr = String.format("%0" + maxSeqDigit + "d", sequence);
        
        return categoryCode + vendorCode + bankCode + seqStr;
    }

    default String buildVendorName(Vendor vendor) {
        if (vendor == null || vendor.getVendorDetails() == null) {
            return "";
        }
        
        var details = vendor.getVendorDetails();
        
        // Build from firstName, middleName, lastName
        StringBuilder name = new StringBuilder();
        if (details.getFirstName() != null && !details.getFirstName().trim().isEmpty()) {
            name.append(details.getFirstName());
        }
        if (details.getMiddleName() != null && !details.getMiddleName().trim().isEmpty()) {
            if (name.length() > 0) name.append(" ");
            name.append(details.getMiddleName());
        }
        if (details.getLastName() != null && !details.getLastName().trim().isEmpty()) {
            if (name.length() > 0) name.append(" ");
            name.append(details.getLastName());
        }
        
        // Fallback to email if no name parts
        if (name.length() == 0 && details.getEmail() != null) {
            return details.getEmail();
        }
        
        return name.toString();
    }
}
