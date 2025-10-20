package com.eps.module.api.epsone.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponseDto {

    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
}
