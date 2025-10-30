package com.eps.module.api.epsone.asset.mapper;

import com.eps.module.api.epsone.asset.context.AssetRequestDto;
import com.eps.module.api.epsone.asset.context.AssetResponseDto;
import com.eps.module.asset.Asset;
import com.eps.module.asset.AssetCategory;
import com.eps.module.asset.AssetType;
import com.eps.module.bank.Bank;
import com.eps.module.person.PersonDetails;
import com.eps.module.status.GenericStatusType;
import com.eps.module.status.OwnershipStatus;
import com.eps.module.vendor.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assetType", source = "assetTypeId")
    @Mapping(target = "assetCategory", source = "assetCategoryId")
    @Mapping(target = "vendor", source = "vendorId")
    @Mapping(target = "lenderBank", source = "lenderBankId")
    @Mapping(target = "statusType", source = "statusTypeId")
    @Mapping(target = "ownershipStatus", source = "ownershipStatusId")
    Asset toEntity(AssetRequestDto dto);

    @Mapping(target = "assetTypeId", source = "asset.assetType.id")
    @Mapping(target = "assetTypeName", source = "asset.assetType.typeName")
    @Mapping(target = "assetTypeCode", source = "asset.assetType.typeCode")
    @Mapping(target = "assetCategoryId", source = "asset.assetCategory.id")
    @Mapping(target = "assetCategoryName", source = "asset.assetCategory.categoryName")
    @Mapping(target = "assetCategoryCode", source = "asset.assetCategory.categoryCode")
    @Mapping(target = "vendorId", source = "asset.vendor.id")
    @Mapping(target = "vendorName", expression = "java(buildVendorName(asset.getVendor()))")
    @Mapping(target = "vendorCode", source = "asset.vendor.vendorCodeAlt")
    @Mapping(target = "lenderBankId", source = "asset.lenderBank.id")
    @Mapping(target = "lenderBankName", source = "asset.lenderBank.bankName")
    @Mapping(target = "lenderBankCode", source = "asset.lenderBank.bankCodeAlt")
    @Mapping(target = "statusTypeId", source = "asset.statusType.id")
    @Mapping(target = "statusTypeName", source = "asset.statusType.statusName")
    @Mapping(target = "ownershipStatusId", source = "asset.ownershipStatus.id")
    @Mapping(target = "ownershipStatusName", source = "asset.ownershipStatus.typeName")
    AssetResponseDto toDto(Asset asset);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assetType", source = "assetTypeId")
    @Mapping(target = "assetCategory", source = "assetCategoryId")
    @Mapping(target = "vendor", source = "vendorId")
    @Mapping(target = "lenderBank", source = "lenderBankId")
    @Mapping(target = "statusType", source = "statusTypeId")
    @Mapping(target = "ownershipStatus", source = "ownershipStatusId")
    void updateEntityFromDto(AssetRequestDto dto, @MappingTarget Asset asset);

    default AssetType mapAssetType(Long id) {
        if (id == null) return null;
        AssetType assetType = new AssetType();
        assetType.setId(id);
        return assetType;
    }

    default AssetCategory mapAssetCategory(Long id) {
        if (id == null) return null;
        AssetCategory assetCategory = new AssetCategory();
        assetCategory.setId(id);
        return assetCategory;
    }

    default Vendor mapVendor(Long id) {
        if (id == null) return null;
        Vendor vendor = new Vendor();
        vendor.setId(id);
        return vendor;
    }

    default Bank mapBank(Long id) {
        if (id == null) return null;
        Bank bank = new Bank();
        bank.setId(id);
        return bank;
    }

    default GenericStatusType mapStatusType(Long id) {
        if (id == null) return null;
        GenericStatusType statusType = new GenericStatusType();
        statusType.setId(id);
        return statusType;
    }

    default OwnershipStatus mapOwnershipStatus(Long id) {
        if (id == null) return null;
        OwnershipStatus ownershipStatus = new OwnershipStatus();
        ownershipStatus.setId(id);
        return ownershipStatus;
    }

    default String buildVendorName(Vendor vendor) {
        if (vendor == null || vendor.getVendorDetails() == null) {
            return null;
        }
        
        PersonDetails details = vendor.getVendorDetails();
        StringBuilder name = new StringBuilder();
        
        if (details.getFirstName() != null && !details.getFirstName().isEmpty()) {
            name.append(details.getFirstName());
        }
        
        if (details.getMiddleName() != null && !details.getMiddleName().isEmpty()) {
            if (name.length() > 0) name.append(" ");
            name.append(details.getMiddleName());
        }
        
        if (details.getLastName() != null && !details.getLastName().isEmpty()) {
            if (name.length() > 0) name.append(" ");
            name.append(details.getLastName());
        }
        
        return name.length() > 0 ? name.toString() : details.getEmail();
    }
}
