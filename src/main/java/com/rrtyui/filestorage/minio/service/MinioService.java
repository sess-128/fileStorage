package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.util.response.MinioResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final DownloadService downloadService;
    private final RenameAndMoveService renameAndMoveService;
    private final DeleteService deleteService;
    private final InfoService infoService;
    private final UploadService uploadService;
    private final SearchService searchService;
    private final CreateService createService;

    public MinioResponse getResourceInfo(String path, MyUserDetails userDetails) {
        return infoService.getResourceInfo(path, userDetails);
    }

    @SneakyThrows
    public List<MinioResponse> directoryInfo(String path, MyUserDetails myUserDetails) {
        return infoService.directoryInfo(path, myUserDetails);
    }

    public void deleteResource(String path, MyUserDetails userDetails) {
        deleteService.deleteResource(path, userDetails);
    }

    @SneakyThrows
    public void createRootFolderForUser(Long id) {
        createService.createRootFolderForUser(id);
    }

    @SneakyThrows
    public MinioResponse createEmptyFolder(String path, MyUserDetails userDetails) {
        return createService.createEmptyFolder(path, userDetails);
    }

    @SneakyThrows
    public void downloadResource(String path, MyUserDetails userDetails) {
        downloadService.downloadResource(path, userDetails);
    }

    @SneakyThrows
    public void renameOrMoveFile(String from, String to, MyUserDetails myUserDetails) {
        renameAndMoveService.renameOrMoveFile(from, to, myUserDetails);
    }

    @SneakyThrows
    public List<MinioResponse> search(String query, MyUserDetails myUserDetails) {
        return searchService.search(query, myUserDetails);
    }

    @SneakyThrows
    public void uploadFileOrFolder(MultipartFile file, String targetPath, MyUserDetails userDetails) {
        uploadService.uploadFileOrFolder(file, targetPath, userDetails);
    }
}
