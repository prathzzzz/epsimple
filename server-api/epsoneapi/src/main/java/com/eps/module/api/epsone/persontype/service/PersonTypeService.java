package com.eps.module.api.epsone.persontype.service;

import com.eps.module.api.epsone.persontype.dto.PersonTypeRequestDto;
import com.eps.module.api.epsone.persontype.dto.PersonTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonTypeService {
    PersonTypeResponseDto createPersonType(PersonTypeRequestDto requestDto);
    PersonTypeResponseDto updatePersonType(Long id, PersonTypeRequestDto requestDto);
    void deletePersonType(Long id);
    PersonTypeResponseDto getPersonTypeById(Long id);
    Page<PersonTypeResponseDto> getAllPersonTypes(Pageable pageable);
    Page<PersonTypeResponseDto> searchPersonTypes(String search, Pageable pageable);
    List<PersonTypeResponseDto> getAllPersonTypesList();
}
