package com.rrtyui.filestorage.minio.service;

import com.rrtyui.filestorage.mapper.ResponseMapper;
import com.rrtyui.filestorage.minio.repository.MinioRepository;
import com.rrtyui.filestorage.minio.service.impl.BaseService;
import com.rrtyui.filestorage.minio.util.MinioUtil;
import com.rrtyui.filestorage.util.MinioResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UploadService extends BaseService {

    public UploadService(MinioUtil minioUtil, MinioRepository minioRepository) {
        super(minioUtil, minioRepository);
    }

    public List<MinioResponse> upload(List<MultipartFile> files, String targetPath) {
        List<MinioResponse> result = new ArrayList<>();
        String userPrefix = minioUtil.getCurrentUserPath();
        String fullBasePath = userPrefix + targetPath;

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String relativePath = file.getOriginalFilename();
            String fullPath = fullBasePath + "/" + relativePath;

            MinioResponse minioResponse = uploadSingleFile(file, fullPath);
            result.add(minioResponse);
        }
        return result;
    }

    @SneakyThrows
    private MinioResponse uploadSingleFile(MultipartFile file, String targetPath) {
        String contentType = file.getContentType();
        long size = file.getSize();

        try (InputStream inputStream = file.getInputStream()) {
            minioRepository.uploadFile(inputStream, targetPath, contentType, size);
        }
        return ResponseMapper.toMinioResponse(file, targetPath, minioUtil);
    }
}
