package com.eps.module.api.epsone.vendortype.mapper;

import com.eps.module.api.epsone.vendortype.dto.VendorTypeRequestDto;
import com.eps.module.api.epsone.vendortype.dto.VendorTypeResponseDto;
import com.eps.module.vendor.VendorType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-20T20:55:15+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Microsoft)"
)
@Component
public class VendorTypeMapperImpl implements VendorTypeMapper {

    @Override
    public VendorTypeResponseDto toResponseDto(VendorType vendorType) {
        if ( vendorType == null ) {
            return null;
        }

        VendorTypeResponseDto.VendorTypeResponseDtoBuilder vendorTypeResponseDto = VendorTypeResponseDto.builder();

        vendorTypeResponseDto.id( vendorType.getId() );
        vendorTypeResponseDto.typeName( vendorType.getTypeName() );
        vendorTypeResponseDto.vendorCategory( vendorType.getVendorCategory() );
        vendorTypeResponseDto.description( vendorType.getDescription() );
        vendorTypeResponseDto.createdAt( vendorType.getCreatedAt() );
        vendorTypeResponseDto.updatedAt( vendorType.getUpdatedAt() );

        return vendorTypeResponseDto.build();
    }

    @Override
    public VendorType toEntity(VendorTypeRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        VendorType.VendorTypeBuilder vendorType = VendorType.builder();

        vendorType.typeName( requestDto.getTypeName() );
        vendorType.vendorCategory( requestDto.getVendorCategory() );
        vendorType.description( requestDto.getDescription() );

        return vendorType.build();
    }

    @Override
    public void updateEntityFromDto(VendorTypeRequestDto requestDto, VendorType vendorType) {
        if ( requestDto == null ) {
            return;
        }

        vendorType.setTypeName( requestDto.getTypeName() );
        vendorType.setVendorCategory( requestDto.getVendorCategory() );
        vendorType.setDescription( requestDto.getDescription() );
    }

    @Override
    public List<VendorTypeResponseDto> toResponseDtoList(List<VendorType> vendorTypes) {
        if ( vendorTypes == null ) {
            return null;
        }

        List<VendorTypeResponseDto> list = new ArrayList<VendorTypeResponseDto>( vendorTypes.size() );
        for ( VendorType vendorType : vendorTypes ) {
            list.add( toResponseDto( vendorType ) );
        }

        return list;
    }
}
