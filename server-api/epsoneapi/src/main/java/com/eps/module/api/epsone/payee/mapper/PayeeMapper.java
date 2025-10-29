package com.eps.module.api.epsone.payee.mapper;

import com.eps.module.payment.Payee;
import com.eps.module.payment.PayeeDetails;
import com.eps.module.payment.PayeeType;
import com.eps.module.vendor.Landlord;
import com.eps.module.vendor.Vendor;
import com.eps.module.api.epsone.payee.dto.PayeeRequestDto;
import com.eps.module.api.epsone.payee.dto.PayeeResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PayeeMapper {

    public Payee toEntity(PayeeRequestDto dto, PayeeType payeeType, PayeeDetails payeeDetails, Vendor vendor, Landlord landlord) {
        return Payee.builder()
                .payeeType(payeeType)
                .payeeDetails(payeeDetails)
                .vendor(vendor)
                .landlord(landlord)
                .build();
    }

    public void updateEntity(Payee payee, PayeeRequestDto dto, PayeeType payeeType, PayeeDetails payeeDetails, Vendor vendor, Landlord landlord) {
        payee.setPayeeType(payeeType);
        payee.setPayeeDetails(payeeDetails);
        payee.setVendor(vendor);
        payee.setLandlord(landlord);
    }

    public PayeeResponseDto toDto(Payee payee) {
        return PayeeResponseDto.builder()
                .id(payee.getId())
                .payeeTypeId(payee.getPayeeType().getId())
                .payeeTypeName(payee.getPayeeType().getPayeeType())
                .payeeDetailsId(payee.getPayeeDetails().getId())
                .payeeName(payee.getPayeeDetails().getPayeeName())
                .accountNumber(payee.getPayeeDetails().getAccountNumber())
                .bankName(payee.getPayeeDetails().getBank() != null ? payee.getPayeeDetails().getBank().getBankName() : null)
                .vendorId(payee.getVendor() != null ? payee.getVendor().getId() : null)
                .vendorName(payee.getVendor() != null ? buildVendorName(payee.getVendor()) : null)
                .landlordId(payee.getLandlord() != null ? payee.getLandlord().getId() : null)
                .landlordName(payee.getLandlord() != null ? buildLandlordName(payee.getLandlord()) : null)
                .createdAt(payee.getCreatedAt())
                .updatedAt(payee.getUpdatedAt())
                .build();
    }

    private String buildVendorName(Vendor vendor) {
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

    private String buildLandlordName(Landlord landlord) {
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
