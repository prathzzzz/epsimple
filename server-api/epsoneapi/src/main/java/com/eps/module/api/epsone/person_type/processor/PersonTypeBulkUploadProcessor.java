package com.eps.module.api.epsone.person_type.processor;

import com.eps.module.api.epsone.person_type.dto.PersonTypeBulkUploadDto;
import com.eps.module.api.epsone.person_type.validator.PersonTypeBulkUploadValidator;
import com.eps.module.api.epsone.person_type.repository.PersonTypeRepository;
import com.eps.module.common.bulk.processor.BulkUploadProcessor;
import com.eps.module.common.bulk.validator.BulkRowValidator;
import com.eps.module.person.PersonType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonTypeBulkUploadProcessor extends BulkUploadProcessor<PersonTypeBulkUploadDto, PersonType> {

    private final PersonTypeBulkUploadValidator validator;
    private final PersonTypeRepository personTypeRepository;

    @Override
    protected BulkRowValidator<PersonTypeBulkUploadDto> getValidator() {
        return validator;
    }

    @Override
    protected PersonType convertToEntity(PersonTypeBulkUploadDto dto) {
        return PersonType.builder()
                .typeName(capitalizeFirstLetter(dto.getTypeName()))
                .description(dto.getDescription())
                .build();
    }

    @Override
    protected void saveEntity(PersonType entity) {
        personTypeRepository.save(entity);
    }

    @Override
    protected Map<String, Object> getRowDataAsMap(PersonTypeBulkUploadDto dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("typeName", dto.getTypeName());
        map.put("description", dto.getDescription());
        return map;
    }

    @Override
    protected boolean isEmptyRow(PersonTypeBulkUploadDto dto) {
        return (dto.getTypeName() == null || dto.getTypeName().trim().isEmpty()) &&
               (dto.getDescription() == null || dto.getDescription().trim().isEmpty());
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        String trimmed = input.trim();
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}
