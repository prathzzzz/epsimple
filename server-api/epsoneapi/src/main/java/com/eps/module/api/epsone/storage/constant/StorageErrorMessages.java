package com.eps.module.api.epsone.storage.constant;

public class StorageErrorMessages {
    public static final String FILE_UPLOAD_FAILED = "Failed to upload file: %s";
    public static final String FILE_DELETE_FAILED = "Failed to delete file: %s";
    public static final String FILE_DOWNLOAD_FAILED = "Failed to download file: %s";
    public static final String FILE_NOT_FOUND = "File not found: %s";
    public static final String FILE_EMPTY = "File is empty";
    public static final String FILE_SIZE_EXCEEDED = "File size exceeds maximum limit of 5MB";
    public static final String INVALID_FILE_TYPE = "Invalid file type. Allowed types: PNG, JPEG, JPG, SVG, WEBP";
    public static final String INVALID_FILENAME = "Invalid filename";
    
    private StorageErrorMessages() {}
}
