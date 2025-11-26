package com.eps.module.api.epsone.vendor.mapper;

import com.eps.module.api.epsone.vendor.dto.VendorRequestDto;
import com.eps.module.api.epsone.vendor.dto.VendorResponseDto;
import com.eps.module.auth.audit.AuditFieldMapper;
import com.eps.module.vendor.Vendor;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AuditFieldMapper.class})
public interface VendorMapper {

    @Mapping(target = "vendorType", ignore = true)
    @Mapping(target = "vendorDetails", ignore = true)
    Vendor toEntity(VendorRequestDto dto);

    @Mapping(source = "vendorType.id", target = "vendorTypeId")
    @Mapping(source = "vendorType.typeName", target = "vendorTypeName")
    @Mapping(source = "vendorType.vendorCategory.categoryName", target = "vendorCategoryName")
    @Mapping(source = "vendorDetails.id", target = "vendorDetailsId")
    @Mapping(target = "vendorName", expression = "java(buildFullName(entity))")
    @Mapping(source = "vendorDetails.contactNumber", target = "vendorContact")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapCreatedBy")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapUpdatedBy")
    VendorResponseDto toDto(Vendor entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "vendorType", ignore = true)
    @Mapping(target = "vendorDetails", ignore = true)
    void updateEntityFromDto(VendorRequestDto dto, @MappingTarget Vendor entity);

    default String buildFullName(Vendor vendor) {
        if (vendor.getVendorDetails() == null) {
            return null;
        }
        StringBuilder fullName = new StringBuilder();
        if (vendor.getVendorDetails().getFirstName() != null && !vendor.getVendorDetails().getFirstName().isEmpty()) {
            fullName.append(vendor.getVendorDetails().getFirstName());
        }
        if (vendor.getVendorDetails().getMiddleName() != null && !vendor.getVendorDetails().getMiddleName().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(vendor.getVendorDetails().getMiddleName());
        }
        if (vendor.getVendorDetails().getLastName() != null && !vendor.getVendorDetails().getLastName().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(vendor.getVendorDetails().getLastName());
        }
        return fullName.length() > 0 ? fullName.toString() : null;
    }
}
