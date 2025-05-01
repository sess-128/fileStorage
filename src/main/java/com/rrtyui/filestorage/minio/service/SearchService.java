package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.service.impl.BaseService;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import com.rrtyui.filestorage.util.MinioResponse;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService extends BaseService {

    public SearchService(MinioUtil minioUtil, MinioRepository minioRepository) {
        super(minioUtil, minioRepository);
    }

    @SneakyThrows
    public List<MinioResponse> search(String query) {
        List<MinioResponse> result = new ArrayList<>();
        String userPrefix = minioUtil.getCurrentUserPath();

        Iterable<Result<Item>> objects = minioRepository.getContentsDirectoryRecursively(userPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fullObjectName = item.objectName();
            String relativePath = fullObjectName.substring(userPrefix.length());

            MinioResponse minioResponse = ResponseMapper.toMinioResponse(item, relativePath, query, minioUtil);
            result.add(minioResponse);
        }

        return result;
    }
}
