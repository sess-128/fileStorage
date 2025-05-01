package com.rrtyui.filestorage.minio.service.impl;

import com.rrtyui.filestorage.util.MinioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioFileStorageService {
    private final StorageOperationHandler<MinioResponse> operationHandler;

    public List<MinioResponse> getInfo(String path) {
        return operationHandler.getInfo(path);
    }

    public void delete(String path) {
        operationHandler.delete(path);
    }

    public void download(String path) {
        operationHandler.download(path);
    }

    public MinioResponse move(String from, String to) {
        return operationHandler.move(from, to);
    }

    public List<MinioResponse> search(String query) {
        return operationHandler.search(query);
    }

    public List<MinioResponse> upload(List<MultipartFile> files, String path) {
        return operationHandler.upload(files, path);
    }

    public MinioResponse createEmptyFolder(String path) {
        return operationHandler.createEmptyFolder(path);
    }

    public void createRootFolderByUserId(Long id) {
        operationHandler.createRootFolderByUserId(id);
    }
}
