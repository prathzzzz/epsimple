package com.eps.module.api.epsone.landlord.mapper;

import com.eps.module.api.epsone.landlord.dto.LandlordRequestDto;
import com.eps.module.api.epsone.landlord.dto.LandlordResponseDto;
import com.eps.module.vendor.Landlord;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LandlordMapper {

    @Mapping(target = "landlordDetails", ignore = true)
    Landlord toEntity(LandlordRequestDto dto);

    @Mapping(source = "landlordDetails.id", target = "landlordDetailsId")
    @Mapping(target = "landlordName", expression = "java(buildFullName(entity))")
    @Mapping(source = "landlordDetails.email", target = "landlordEmail")
    @Mapping(source = "landlordDetails.contactNumber", target = "landlordPhone")
    LandlordResponseDto toDto(Landlord entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "landlordDetails", ignore = true)
    void updateEntityFromDto(LandlordRequestDto dto, @MappingTarget Landlord entity);

    default String buildFullName(Landlord landlord) {
        if (landlord.getLandlordDetails() == null) {
            return null;
        }
        StringBuilder fullName = new StringBuilder();
        if (landlord.getLandlordDetails().getFirstName() != null && !landlord.getLandlordDetails().getFirstName().isEmpty()) {
            fullName.append(landlord.getLandlordDetails().getFirstName());
        }
        if (landlord.getLandlordDetails().getMiddleName() != null && !landlord.getLandlordDetails().getMiddleName().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(landlord.getLandlordDetails().getMiddleName());
        }
        if (landlord.getLandlordDetails().getLastName() != null && !landlord.getLandlordDetails().getLastName().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(landlord.getLandlordDetails().getLastName());
        }
        return fullName.length() > 0 ? fullName.toString() : null;
    }
}
