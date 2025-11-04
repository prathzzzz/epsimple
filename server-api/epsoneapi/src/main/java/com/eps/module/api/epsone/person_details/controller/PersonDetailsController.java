package com.eps.module.api.epsone.person_details.controller;

import com.eps.module.api.epsone.person_details.dto.PersonDetailsRequestDto;
import com.eps.module.api.epsone.person_details.dto.PersonDetailsResponseDto;
import com.eps.module.api.epsone.person_details.service.PersonDetailsService;
import com.eps.module.common.response.ApiResponse;
import com.eps.module.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/person-details")
@RequiredArgsConstructor
public class PersonDetailsController {

    private final PersonDetailsService personDetailsService;

    @PostMapping
    public ResponseEntity<ApiResponse<PersonDetailsResponseDto>> createPersonDetails(@Valid @RequestBody PersonDetailsRequestDto requestDto) {
        log.info("POST /api/person-details - Creating new person details");
        PersonDetailsResponseDto response = personDetailsService.createPersonDetails(requestDto);
        return ResponseBuilder.success(response, "Person details created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PersonDetailsResponseDto>>> getAllPersonDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/person-details - Fetching all person details with pagination");
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.getAllPersonDetails(pageable);
        return ResponseBuilder.success(personDetails, "Person details retrieved successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PersonDetailsResponseDto>>> searchPersonDetails(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/person-details/search - Searching person details with term: {}", searchTerm);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.searchPersonDetails(searchTerm, pageable);
        return ResponseBuilder.success(personDetails, "Person details search completed successfully");
    }

    @GetMapping("/person-type/{personTypeId}")
    public ResponseEntity<ApiResponse<Page<PersonDetailsResponseDto>>> getPersonDetailsByPersonType(
            @PathVariable Long personTypeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        log.info("GET /api/person-details/person-type/{} - Fetching person details by person type", personTypeId);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PersonDetailsResponseDto> personDetails = personDetailsService.getPersonDetailsByPersonType(personTypeId, pageable);
        return ResponseBuilder.success(personDetails, "Person details retrieved successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PersonDetailsResponseDto>>> getPersonDetailsList() {
        log.info("GET /api/person-details/list - Fetching all person details as list");
        List<PersonDetailsResponseDto> personDetails = personDetailsService.getPersonDetailsList();
        return ResponseBuilder.success(personDetails, "Person details list retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonDetailsResponseDto>> getPersonDetailsById(@PathVariable Long id) {
        log.info("GET /api/person-details/{} - Fetching person details by ID", id);
        PersonDetailsResponseDto personDetails = personDetailsService.getPersonDetailsById(id);
        return ResponseBuilder.success(personDetails, "Person details retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonDetailsResponseDto>> updatePersonDetails(
            @PathVariable Long id,
            @Valid @RequestBody PersonDetailsRequestDto requestDto) {
        log.info("PUT /api/person-details/{} - Updating person details", id);
        PersonDetailsResponseDto updatedPersonDetails = personDetailsService.updatePersonDetails(id, requestDto);
        return ResponseBuilder.success(updatedPersonDetails, "Person details updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePersonDetails(@PathVariable Long id) {
        log.info("DELETE /api/person-details/{} - Deleting person details", id);
        personDetailsService.deletePersonDetails(id);
        return ResponseBuilder.success(null, "Person details deleted successfully");
    }
}
