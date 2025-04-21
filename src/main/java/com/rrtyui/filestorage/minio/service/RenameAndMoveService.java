package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import com.rrtyui.filestorage.security.MyUserDetails;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class RenameAndMoveService extends BaseService{

    private final DeleteService deleteService;

    public RenameAndMoveService(MinioUtils minioUtils, MinioRepository minioRepository, DeleteService deleteService) {
        super(minioUtils, minioRepository);
        this.deleteService = deleteService;
    }

    @SneakyThrows
    public void renameOrMoveFile(String from, String to, MyUserDetails myUserDetails) {
        String fullFromPath = minioUtils.getCurrentUserPath(myUserDetails) + from;
        String fullToPath = minioUtils.getCurrentUserPath(myUserDetails) + to;

        if (fullFromPath.equals(fullToPath)) {
            throw new IllegalArgumentException("Source and destination paths are identical");
        }

        if (minioUtils.isDirectoryPath(from)) {
            handleDirectoryOperation(from, to, myUserDetails);
            return;
        }
        if (minioUtils.isRenameOperation(from, to)) {
            renameFile(from, to, myUserDetails);
            return;
        }
        if (minioUtils.isMoveOperation(from, to)) {
            moveFile(from, to, myUserDetails);
        } else {
            throw new UnsupportedOperationException("Combined rename+move operations are not supported for files");
        }

    }

    private void handleDirectoryOperation(String from, String to, MyUserDetails myUserDetails) {
        if (minioUtils.isRenameOperation(from, to)) {
            renameDirectory(from, to, myUserDetails);
        } else if (minioUtils.isMoveOperation(from, to)) {
            moveDirectory(from, to, myUserDetails);
        } else {
            throw new UnsupportedOperationException("Combined rename+move operations for directories require manual handling");
        }
    }

    @SneakyThrows
    private void renameDirectory(String from, String to, MyUserDetails myUserDetails) {
        String sourcePrefix = minioUtils.getCurrentUserPath(myUserDetails) + from;
        String targetPrefix = minioUtils.getCurrentUserPath(myUserDetails) + to;

        var objects = minioRepository.getContentsDirectoryRecursively(sourcePrefix);

        for (Result<Item> result : objects) {
            Item item = result.get();
            String name = item.objectName();
            String relativePath = name.substring(sourcePrefix.length());
            String newObjectName = targetPrefix + relativePath;

            minioRepository.copyFile(newObjectName, name);
        }

       deleteService.deleteDirectory(from, myUserDetails);
    }

    @SneakyThrows
    private void moveDirectory(String from, String to, MyUserDetails myUserDetails) {
        String dirName = minioUtils.getLastSegmentPrefix(from);
        String newPath = minioUtils.getParentPrefix(to) + dirName;

        renameDirectory(from, newPath, myUserDetails);
    }

    @SneakyThrows
    private void renameFile(String from, String to, MyUserDetails myUserDetails) {
        String fromSource = minioUtils.getCurrentUserPath(myUserDetails) + from;
        String toSource = minioUtils.getCurrentUserPath(myUserDetails) + to;
        minioRepository.copyFile(fromSource, toSource);

        deleteService.deleteResource(from, myUserDetails);
    }

    @SneakyThrows
    private void moveFile(String from, String to, MyUserDetails myUserDetails) {
        String originalFileName = minioUtils.getLastSegmentPrefix(from);
        String newPath = minioUtils.getParentPrefix(to) + originalFileName;

        renameFile(from, newPath, myUserDetails);
    }
}
