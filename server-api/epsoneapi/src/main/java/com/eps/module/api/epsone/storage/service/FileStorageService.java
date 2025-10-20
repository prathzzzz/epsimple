package com.eps.module.api.epsone.storage.service;

import com.eps.module.api.epsone.storage.dto.FileUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileUploadResponseDto uploadFile(MultipartFile file, String directory);

    void deleteFile(String filePath);

    byte[] downloadFile(String filePath);

    boolean fileExists(String filePath);
}
