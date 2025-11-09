package com.eps.module.api.epsone.generic_status_type.service;

import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeBulkUploadDto;
import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeRequestDto;
import com.eps.module.api.epsone.generic_status_type.dto.GenericStatusTypeResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.status.GenericStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericStatusTypeService extends BulkUploadService<GenericStatusTypeBulkUploadDto, GenericStatusType> {

    GenericStatusTypeResponseDto createGenericStatusType(GenericStatusTypeRequestDto genericStatusTypeRequestDto);

    Page<GenericStatusTypeResponseDto> getAllGenericStatusTypes(Pageable pageable);

    Page<GenericStatusTypeResponseDto> searchGenericStatusTypes(String searchTerm, Pageable pageable);

    List<GenericStatusTypeResponseDto> getGenericStatusTypeList();

    GenericStatusTypeResponseDto getGenericStatusTypeById(Long id);

    GenericStatusTypeResponseDto updateGenericStatusType(Long id, GenericStatusTypeRequestDto genericStatusTypeRequestDto);

    void deleteGenericStatusType(Long id);
}
