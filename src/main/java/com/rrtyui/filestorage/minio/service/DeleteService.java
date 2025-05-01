package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.service.impl.BaseService;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class DeleteService extends BaseService {

    public DeleteService(MinioUtil minioUtil, MinioRepository minioRepository) {
        super(minioUtil, minioRepository);
    }

    public void delete(String path) {
        if (minioUtil.isDirectoryPath(path)) {
            deleteDirectory(path);
        } else {
            deleteFile(path);
        }
    }

    @SneakyThrows
    private void deleteDirectory(String path) {
        String prefix = minioUtil.getCurrentUserPath() + path;
        Iterable<Result<Item>> objects = minioRepository.getContentsDirectoryRecursively(prefix);

        for (Result<Item> result : objects) {
            String toRemove = result.get().objectName();
            minioRepository.deleteFile(toRemove);
        }

        try {
            minioRepository.deleteFile(prefix);
        } catch (Exception e) {
            System.out.println("Ошибка при удалении папки-объекта: " + e.getMessage());
        }
    }

    private void deleteFile(String path) {
        String name = minioUtil.getCurrentUserPath() + path;
        minioRepository.deleteResource(name);
    }
}
