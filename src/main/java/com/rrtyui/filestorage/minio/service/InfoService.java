package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import com.rrtyui.filestorage.minio.util.ResourceType;
import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.util.response.MinioResponse;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InfoService extends BaseService{

    public InfoService(MinioUtils minioUtils, MinioRepository minioRepository) {
        super(minioUtils, minioRepository);
    }

    public MinioResponse getResourceInfo(String path, MyUserDetails userDetails) {
        String name = minioUtils.getCurrentUserPath(userDetails) + path;
        StatObjectResponse resourceInfo = minioRepository.getResourceInfo(name);
        return ResponseMapper.toMinioResponse(resourceInfo, path);
    }

    @SneakyThrows
    public List<MinioResponse> directoryInfo(String path, MyUserDetails myUserDetails) {
        List<MinioResponse> directoryContent = new ArrayList<>();
        String normalizedPath = minioUtils.normalizePath(path);
        String sourcePrefix = minioUtils.getCurrentUserPath(myUserDetails) + normalizedPath;

        Iterable<Result<Item>> items = minioRepository.getContentsDirectory(sourcePrefix);

        for (Result<Item> itemResult : items) {
            Item item = itemResult.get();
            String objectName = item.objectName();
            String relativePath = objectName.substring(minioUtils.getCurrentUserPath(myUserDetails).length());

            if (relativePath.isEmpty() || relativePath.equals(path)) {
                continue;
            }

            if (!item.isDir() && item.size() == 0 && relativePath.endsWith("/")) {
                continue;
            }

            if (item.isDir()) {
                MinioResponse minioResponse = MinioResponse.builder()
                        .path(path)
                        .name(minioUtils.getLastSegmentPrefix(relativePath))
                        .type(ResourceType.DIRECTORY)
                        .build();
                directoryContent.add(minioResponse);
            } else {
                MinioResponse minioResponse = MinioResponse.builder()
                        .path(path)
                        .name(minioUtils.createFileName(relativePath))
                        .size(item.size())
                        .type(ResourceType.FILE)
                        .build();
                directoryContent.add(minioResponse);
            }
        }

        return directoryContent;
    }
}
