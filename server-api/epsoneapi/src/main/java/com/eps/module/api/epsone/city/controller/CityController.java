package com.eps.module.api.epsone.city.controller;

import com.eps.module.api.epsone.city.dto.CityRequestDto;
import com.eps.module.api.epsone.city.dto.CityResponseDto;
import com.eps.module.api.epsone.city.service.CityService;
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
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<CityResponseDto> createCity(@Valid @RequestBody CityRequestDto requestDto) {
        CityResponseDto response = cityService.createCity(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<CityResponseDto>> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<CityResponseDto> cities = cityService.getAllCities(pageable);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CityResponseDto>> searchCities(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<CityResponseDto> cities = cityService.searchCities(searchTerm, pageable);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/state/{stateId}")
    public ResponseEntity<Page<CityResponseDto>> getCitiesByState(
            @PathVariable Long stateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<CityResponseDto> cities = cityService.getCitiesByState(stateId, pageable);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CityResponseDto>> getCityList() {
        List<CityResponseDto> cities = cityService.getCityList();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponseDto> getCityById(@PathVariable Long id) {
        CityResponseDto city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponseDto> updateCity(
            @PathVariable Long id,
            @Valid @RequestBody CityRequestDto requestDto) {
        CityResponseDto updatedCity = cityService.updateCity(id, requestDto);
        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseBuilder.success(null, "City deleted successfully");
    }
}
