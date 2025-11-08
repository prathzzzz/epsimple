package com.eps.module.api.epsone.data_center.service;

import com.eps.module.api.epsone.data_center.dto.DatacenterBulkUploadDto;
import com.eps.module.api.epsone.data_center.dto.DatacenterRequestDto;
import com.eps.module.api.epsone.data_center.dto.DatacenterResponseDto;
import com.eps.module.common.bulk.service.BulkUploadService;
import com.eps.module.warehouse.Datacenter;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DatacenterService extends BulkUploadService<DatacenterBulkUploadDto, Datacenter> {

    DatacenterResponseDto createDatacenter(DatacenterRequestDto requestDto);

    Page<DatacenterResponseDto> getAllDatacenters(int page, int size, String sortBy, String sortOrder);

    Page<DatacenterResponseDto> searchDatacenters(String searchTerm, int page, int size, String sortBy, String sortOrder);

    List<DatacenterResponseDto> getAllDatacentersList();

    DatacenterResponseDto getDatacenterById(Long id);

    DatacenterResponseDto updateDatacenter(Long id, DatacenterRequestDto requestDto);

    void deleteDatacenter(Long id);
}
