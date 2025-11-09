package com.eps.module.api.epsone.person_details.bulk;

import com.eps.module.api.epsone.person_details.dto.PersonDetailsBulkUploadDto;
import com.eps.module.api.epsone.person_details.repository.PersonDetailsRepository;
import com.eps.module.api.epsone.person_type.repository.PersonTypeRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.person.PersonDetails;
import com.eps.module.person.PersonType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PersonDetailsBulkUploadProcessor extends BulkUploadProcessor<PersonDetailsBulkUploadDto, PersonDetails> {

    private final PersonDetailsBulkUploadValidator validator;
    private final PersonDetailsRepository personDetailsRepository;
    private final PersonTypeRepository personTypeRepository;

    @Override
    public BulkRowValidator<PersonDetailsBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    public PersonDetails convertToEntity(PersonDetailsBulkUploadDto dto) {
        // Find person type by name
        PersonType personType = personTypeRepository.findByTypeNameIgnoreCase(dto.getPersonTypeName())
                .orElseThrow(() -> new IllegalArgumentException("Person type not found: " + dto.getPersonTypeName()));

        return PersonDetails.builder()
                .personType(personType)
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .contactNumber(dto.getContactNumber())
                .permanentAddress(dto.getPermanentAddress())
                .correspondenceAddress(dto.getCorrespondenceAddress())
                .build();
    }

    @Override
    public void saveEntity(PersonDetails entity) {
        personDetailsRepository.save(entity);
    }

    @Override
    public Map<String, Object> getRowDataAsMap(PersonDetailsBulkUploadDto dto) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("personTypeName", dto.getPersonTypeName());
        data.put("firstName", dto.getFirstName());
        data.put("middleName", dto.getMiddleName());
        data.put("lastName", dto.getLastName());
        data.put("contactNumber", dto.getContactNumber());
        data.put("permanentAddress", dto.getPermanentAddress());
        data.put("correspondenceAddress", dto.getCorrespondenceAddress());
        return data;
    }

    @Override
    public boolean isEmptyRow(PersonDetailsBulkUploadDto dto) {
        return (dto.getPersonTypeName() == null || dto.getPersonTypeName().isBlank()) &&
               (dto.getFirstName() == null || dto.getFirstName().isBlank()) &&
               (dto.getMiddleName() == null || dto.getMiddleName().isBlank()) &&
               (dto.getLastName() == null || dto.getLastName().isBlank()) &&
               (dto.getContactNumber() == null || dto.getContactNumber().isBlank()) &&
               (dto.getPermanentAddress() == null || dto.getPermanentAddress().isBlank()) &&
               (dto.getCorrespondenceAddress() == null || dto.getCorrespondenceAddress().isBlank());
    }
}
