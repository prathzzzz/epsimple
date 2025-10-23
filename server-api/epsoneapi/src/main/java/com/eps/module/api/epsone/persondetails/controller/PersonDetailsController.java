package com.eps.module.api.epsone.persondetails.controller;

import com.eps.module.api.epsone.persondetails.dto.PersonDetailsRequestDto;
import com.eps.module.api.epsone.persondetails.dto.PersonDetailsResponseDto;
import com.eps.module.api.epsone.persondetails.service.PersonDetailsService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person-details")
@RequiredArgsConstructor
public class PersonDetailsController {

    private final PersonDetailsService personDetailsService;

    @PostMapping
    public ResponseEntity<PersonDetailsResponseDto> createPersonDetails(@Valid @RequestBody PersonDetailsRequestDto requestDto) {
        PersonDetailsResponseDto response = personDetailsService.createPersonDetails(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PersonDetailsResponseDto>> getAllPersonDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.getAllPersonDetails(pageable);
        return ResponseEntity.ok(personDetails);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PersonDetailsResponseDto>> searchPersonDetails(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.searchPersonDetails(searchTerm, pageable);
        return ResponseEntity.ok(personDetails);
    }

    @GetMapping("/person-type/{personTypeId}")
    public ResponseEntity<Page<PersonDetailsResponseDto>> getPersonDetailsByPersonType(
            @PathVariable Long personTypeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.getPersonDetailsByPersonType(personTypeId, pageable);
        return ResponseEntity.ok(personDetails);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PersonDetailsResponseDto>> getPersonDetailsList() {
        List<PersonDetailsResponseDto> personDetails = personDetailsService.getPersonDetailsList();
        return ResponseEntity.ok(personDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDetailsResponseDto> getPersonDetailsById(@PathVariable Long id) {
        PersonDetailsResponseDto personDetails = personDetailsService.getPersonDetailsById(id);
        return ResponseEntity.ok(personDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDetailsResponseDto> updatePersonDetails(
            @PathVariable Long id,
            @Valid @RequestBody PersonDetailsRequestDto requestDto) {
        PersonDetailsResponseDto updatedPersonDetails = personDetailsService.updatePersonDetails(id, requestDto);
        return ResponseEntity.ok(updatedPersonDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePersonDetails(@PathVariable Long id) {
        personDetailsService.deletePersonDetails(id);
        return ResponseBuilder.success(null, "Person details deleted successfully");
    }
}
