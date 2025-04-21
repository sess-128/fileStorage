package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import com.rrtyui.filestorage.security.MyUserDetails;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service

public class DeleteService extends BaseService{

    public DeleteService(MinioUtils minioUtils, MinioRepository minioRepository) {
        super(minioUtils, minioRepository);
    }

    @SneakyThrows
    public void deleteDirectory(String path, MyUserDetails myUserDetails) {
        String prefix = minioUtils.getCurrentUserPath(myUserDetails) + path;
        Iterable<Result<Item>> objects = minioRepository.getContentsDirectoryRecursively(prefix);

        for (Result<Item> result : objects) {
            String toRemove = result.get().objectName();
            minioRepository.deleteFile(toRemove);
        }

        try {
            minioRepository.deleteFile(prefix);
        } catch (Exception ignored) {
        }
    }

    public void deleteResource(String path, MyUserDetails userDetails) {
        String name = minioUtils.getCurrentUserPath(userDetails) + path;
        minioRepository.deleteResource(name);
    }
}
