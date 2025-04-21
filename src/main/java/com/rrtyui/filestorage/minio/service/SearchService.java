package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.util.MinioUtils;
import com.rrtyui.filestorage.minio.util.ResourceType;
import com.rrtyui.filestorage.security.MyUserDetails;
import com.rrtyui.filestorage.util.response.MinioResponse;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService extends BaseService{

    public SearchService(MinioUtils minioUtils, MinioRepository minioRepository) {
        super(minioUtils, minioRepository);
    }

    @SneakyThrows
    public List<MinioResponse> search(String query, MyUserDetails myUserDetails) {
        List<MinioResponse> result = new ArrayList<>();
        String userPrefix = minioUtils.getCurrentUserPath(myUserDetails);

        Iterable<Result<Item>> objects = minioRepository.getContentsDirectoryRecursively(userPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fullObjectName = item.objectName();
            String relativePath = fullObjectName.substring(userPrefix.length());

            boolean isDirectory = minioUtils.isDirectoryPath(fullObjectName) || item.isDir();
            String objectName = isDirectory
                    ? minioUtils.getLastSegmentPrefix(relativePath)
                    : minioUtils.createFileName(relativePath);

            if (objectName.toLowerCase().contains(query.toLowerCase())) {
                MinioResponse response = MinioResponse.builder()
                        .path(minioUtils.getParentPrefix(relativePath))
                        .name(objectName)
                        .type(isDirectory ? ResourceType.DIRECTORY : ResourceType.FILE)
                        .build();

                if (!isDirectory) {
                    response.setSize(item.size());
                }
                result.add(response);
            }
        }

        return result;
    }
}
