package com.eps.module.api.epsone.person_type.service;

import com.eps.module.api.epsone.person_type.dto.PersonTypeBulkUploadDto;
import com.eps.module.api.epsone.person_type.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.person_type.dto.PersonTypeResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.person.PersonType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonTypeService extends BulkUploadService<PersonTypeBulkUploadDto, PersonType> {
    PersonTypeResponseDto createPersonType(PersonTypeRequestDto requestDto);
    PersonTypeResponseDto updatePersonType(Long id, PersonTypeRequestDto requestDto);
    void deletePersonType(Long id);
    PersonTypeResponseDto getPersonTypeById(Long id);
    Page<PersonTypeResponseDto> getAllPersonTypes(Pageable pageable);
    Page<PersonTypeResponseDto> searchPersonTypes(String search, Pageable pageable);
    List<PersonTypeResponseDto> getAllPersonTypesList();
}
