package com.eps.module.api.epsone.genericstatustype.service;

import com.eps.module.api.epsone.genericstatustype.dto.GenericStatusTypeRequestDto;
import com.eps.module.api.epsone.genericstatustype.dto.GenericStatusTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericStatusTypeService {

    GenericStatusTypeResponseDto createGenericStatusType(GenericStatusTypeRequestDto genericStatusTypeRequestDto);

    Page<GenericStatusTypeResponseDto> getAllGenericStatusTypes(Pageable pageable);

    Page<GenericStatusTypeResponseDto> searchGenericStatusTypes(String searchTerm, Pageable pageable);

    List<GenericStatusTypeResponseDto> getGenericStatusTypeList();

    GenericStatusTypeResponseDto getGenericStatusTypeById(Long id);

    GenericStatusTypeResponseDto updateGenericStatusType(Long id, GenericStatusTypeRequestDto genericStatusTypeRequestDto);

    void deleteGenericStatusType(Long id);
}
