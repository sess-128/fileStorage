package com.rrtyui.filestorage.minio.service.impl;

import com.rrtyui.filestorage.minio.service.*;
import com.rrtyui.filestorage.util.MinioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioOperationHandler implements StorageOperationHandler<MinioResponse> {

    private final InfoService infoService;
    private final DownloadService downloadService;
    private final MoveService moveService;
    private final DeleteService deleteService;

    private final UploadService uploadService;
    private final SearchService searchService;
    private final CreateService createService;

    @Override
    public List<MinioResponse> getInfo(String path) {
        return infoService.getInfo(path);
    }

    @Override
    public void delete(String path) {
        deleteService.delete(path);
    }

    @Override
    public void download(String path) {
        downloadService.download(path);
    }

    @Override
    public MinioResponse move(String from, String to) {
        return moveService.move(from, to);
    }

    @Override
    public List<MinioResponse> search(String query) {
        return searchService.search(query);
    }

    @Override
    public List<MinioResponse> upload(List<MultipartFile> files, String path) {
        return uploadService.upload(files, path);
    }

    @Override
    public MinioResponse createEmptyFolder(String path) {
        return createService.createEmptyFolder(path);
    }

    @Override
    public void createRootFolderByUserId(Long id) {
        createService.createRootFolderForUser(id);
    }
}
