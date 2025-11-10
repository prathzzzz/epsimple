package com.eps.module.api.epsone.payee.processor;

import com.eps.module.api.epsone.landlord.repository.LandlordRepository;
import com.eps.module.api.epsone.payee.dto.PayeeBulkUploadDto;
import com.eps.module.api.epsone.payee.repository.PayeeRepository;
import com.eps.module.api.epsone.payee.validator.PayeeBulkUploadValidator;
import com.eps.module.api.epsone.payee_details.repository.PayeeDetailsRepository;
import com.eps.module.api.epsone.payee_type.repository.PayeeTypeRepository;
import com.eps.module.api.epsone.vendor.repository.VendorRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.payment.Payee;
import com.eps.module.payment.PayeeDetails;
import com.eps.module.payment.PayeeType;
import com.eps.module.vendor.Landlord;
import com.eps.module.vendor.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PayeeBulkUploadProcessor extends BulkUploadProcessor<PayeeBulkUploadDto, Payee> {

    private final PayeeRepository payeeRepository;
    private final PayeeTypeRepository payeeTypeRepository;
    private final PayeeDetailsRepository payeeDetailsRepository;
    private final VendorRepository vendorRepository;
    private final LandlordRepository landlordRepository;
    private final PayeeBulkUploadValidator validator;

    @Override
    protected BulkRowValidator<PayeeBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected Payee convertToEntity(PayeeBulkUploadDto dto) {
        Payee.PayeeBuilder builder = Payee.builder();

        // Set Payee Type (required)
        PayeeType payeeType = payeeTypeRepository.findByPayeeTypeIgnoreCase(dto.getPayeeType()).orElse(null);
        builder.payeeType(payeeType);

        // Set Payee Details (required)
        PayeeDetails payeeDetails = payeeDetailsRepository.findByPayeeNameIgnoreCase(dto.getPayeeName()).orElse(null);
        builder.payeeDetails(payeeDetails);

        // Set Vendor if provided
        if (dto.getVendorContactNumber() != null && !dto.getVendorContactNumber().trim().isEmpty()) {
            Vendor vendor = vendorRepository.findByVendorDetailsContactNumber(dto.getVendorContactNumber()).orElse(null);
            builder.vendor(vendor);
        }

        // Set Landlord if provided
        if (dto.getLandlordContactNumber() != null && !dto.getLandlordContactNumber().trim().isEmpty()) {
            Landlord landlord = landlordRepository.findByLandlordDetailsContactNumber(dto.getLandlordContactNumber()).orElse(null);
            builder.landlord(landlord);
        }

        return builder.build();
    }

    @Override
    protected void saveEntity(Payee entity) {
        payeeRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(PayeeBulkUploadDto dto) {
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Payee Type", dto.getPayeeType());
        rowData.put("Payee Name", dto.getPayeeName());
        rowData.put("Vendor Contact Number", dto.getVendorContactNumber());
        rowData.put("Landlord Contact Number", dto.getLandlordContactNumber());
        return rowData;
    }

    @Override
    protected boolean isEmptyRow(PayeeBulkUploadDto dto) {
        return (dto.getPayeeType() == null || dto.getPayeeType().trim().isEmpty()) &&
               (dto.getPayeeName() == null || dto.getPayeeName().trim().isEmpty()) &&
               (dto.getVendorContactNumber() == null || dto.getVendorContactNumber().trim().isEmpty()) &&
               (dto.getLandlordContactNumber() == null || dto.getLandlordContactNumber().trim().isEmpty());
    }
}
