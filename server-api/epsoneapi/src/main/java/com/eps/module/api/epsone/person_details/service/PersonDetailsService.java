package com.eps.module.api.epsone.person_details.service;

import com.eps.module.api.epsone.person_details.dto.PersonDetailsBulkUploadDto;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsRequestDto;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.person.PersonDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonDetailsService extends BulkUploadService<PersonDetailsBulkUploadDto, PersonDetails> {

    PersonDetailsResponseDto createPersonDetails(PersonDetailsRequestDto requestDto);

    Page<PersonDetailsResponseDto> getAllPersonDetails(Pageable pageable);

    Page<PersonDetailsResponseDto> searchPersonDetails(String searchTerm, Pageable pageable);

    Page<PersonDetailsResponseDto> getPersonDetailsByPersonType(Long personTypeId, Pageable pageable);

    List<PersonDetailsResponseDto> getPersonDetailsList();

    PersonDetailsResponseDto getPersonDetailsById(Long id);

    PersonDetailsResponseDto updatePersonDetails(Long id, PersonDetailsRequestDto requestDto);

    void deletePersonDetails(Long id);
}
