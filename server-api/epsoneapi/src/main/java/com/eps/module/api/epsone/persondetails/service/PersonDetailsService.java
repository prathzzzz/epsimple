package com.eps.module.api.epsone.persondetails.service;

import com.eps.module.api.epsone.persondetails.dto.PersonDetailsRequestDto;
import com.eps.module.api.epsone.persondetails.dto.PersonDetailsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonDetailsService {

    PersonDetailsResponseDto createPersonDetails(PersonDetailsRequestDto requestDto);

    Page<PersonDetailsResponseDto> getAllPersonDetails(Pageable pageable);

    Page<PersonDetailsResponseDto> searchPersonDetails(String searchTerm, Pageable pageable);

    Page<PersonDetailsResponseDto> getPersonDetailsByPersonType(Long personTypeId, Pageable pageable);

    List<PersonDetailsResponseDto> getPersonDetailsList();

    PersonDetailsResponseDto getPersonDetailsById(Long id);

    PersonDetailsResponseDto updatePersonDetails(Long id, PersonDetailsRequestDto requestDto);

    void deletePersonDetails(Long id);
}
