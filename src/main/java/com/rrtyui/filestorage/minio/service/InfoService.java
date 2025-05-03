package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.exception.InvalidPathException;
import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.service.impl.BaseService;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import com.rrtyui.filestorage.util.MinioResponse;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InfoService extends BaseService {


    public InfoService(MinioUtil minioUtil, MinioRepository minioRepository) {
        super(minioUtil, minioRepository);
    }

    public List<MinioResponse> getInfo(String path) {
        if (minioUtil.isDirectoryPath(path)) {
            return directoryInfo(path);
        } else {
            return List.of(resourceInfo(path));
        }
    }

    private MinioResponse resourceInfo(String path) {
        String fullPath = minioUtil.getCurrentUserPath() + path;
        StatObjectResponse resourceInfo = minioRepository.getResourceInfo(fullPath);
        return ResponseMapper.toMinioResponse(resourceInfo, path);
    }

    @SneakyThrows
    private List<MinioResponse> directoryInfo(String path) {
        List<MinioResponse> directoryContent = new ArrayList<>();
        String normalizedPath = minioUtil.normalizePath(path);
        String sourcePrefix = minioUtil.getCurrentUserPath() + normalizedPath;

        Iterable<Result<Item>> items = minioRepository.getContentsDirectory(sourcePrefix);

        if (!items.iterator().hasNext()) {
            throw new InvalidPathException("Такой директории нет");
        }

        for (Result<Item> itemResult : items) {
            Item item = itemResult.get();
            String relativePath = minioUtil.getRelativePathByItem(item);

            if (minioUtil.shouldSkip(item, relativePath, path)) {
                continue;
            }

            MinioResponse minioResponse = ResponseMapper.toMinioResponse(item, relativePath, path, minioUtil);
            directoryContent.add(minioResponse);
        }

        return directoryContent;
    }
}
