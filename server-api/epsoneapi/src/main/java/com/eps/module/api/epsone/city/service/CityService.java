package com.eps.module.api.epsone.city.service;

import com.eps.module.api.epsone.city.dto.CityRequestDto;
import com.eps.module.api.epsone.city.dto.CityResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService {

    CityResponseDto createCity(CityRequestDto requestDto);

    Page<CityResponseDto> getAllCities(Pageable pageable);

    Page<CityResponseDto> searchCities(String searchTerm, Pageable pageable);

    Page<CityResponseDto> getCitiesByState(Long stateId, Pageable pageable);

    List<CityResponseDto> getCityList();

    CityResponseDto getCityById(Long id);

    CityResponseDto updateCity(Long id, CityRequestDto requestDto);

    void deleteCity(Long id);
}
