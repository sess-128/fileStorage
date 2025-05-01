package com.rrtyui.filestorage.minio.service.impl;

import com.rrtyui.filestorage.util.MinioResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageOperationHandler<T> {
    List<T> getInfo(String path);

    void delete(String path);

    void download(String path);

    MinioResponse move(String from, String to);

    List<T> search(String query);

    List<T> upload(List<MultipartFile> files, String path);

    T createEmptyFolder(String path);

    void createRootFolderByUserId(Long id);
}
